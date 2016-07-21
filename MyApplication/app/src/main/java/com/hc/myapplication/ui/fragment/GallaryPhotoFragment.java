package com.hc.myapplication.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hc.myapplication.ui.model.PhotoItem;
import com.hc.myapplication.R;
import com.hc.myapplication.utils.FileManager;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 诚 on 2016/7/19.
 */
public class GallaryPhotoFragment extends Fragment {
    public final static int id = 1;

    private static final String TAG = "GridPhotoFragment";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<PhotoItem> mItems = new ArrayList<>();
    private PhotoAdapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;

    private int page = 1;
    private int photoNumPer = 10;
    private boolean isBottom;

    public static GallaryPhotoFragment newInstance() {
        GallaryPhotoFragment fragment = new GallaryPhotoFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void addPhotoItems(File root){
        File[] allFileList = root.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                File f = new File(file,s);
                //Log.i(TAG, "accept: 扫描到的文件名："+f.getName());
                return !f.getName().equals("html");
            }
        });
        if (allFileList.length <= 0)
            return;

        for (File file : allFileList) {
            if (!file.isDirectory()) {
                PhotoItem item = new PhotoItem();
                item.setTitle(file.getName());
                item.setDate(new Date(file.lastModified()));
                item.setPath(file.getPath());
                //Log.i(TAG, "addPhotoItems: 文件Path:"+item.getPath());
                mItems.add(item);
            } else {
                addPhotoItems(file);
            }
        }
    }

    private void setupAdapter(){
        List<PhotoItem> items;
        if (mItems.size() > photoNumPer)
            items = mItems.subList((page - 1) * photoNumPer,photoNumPer);
        else items = mItems;
        mAdapter = new PhotoAdapter(items);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getMoreItems() {
        synchronized (this){page ++;}
        int remain = mItems.size() - (page - 1) * photoNumPer;
        Log.i(TAG, "getMoreItems: remain:"+remain);
        if (remain <= 0) {
            Snackbar.make(getView(),"没有可以再加载的东东咯！",Snackbar.LENGTH_SHORT).show();
            return;
        }
        int toGet;
        if (remain > photoNumPer) {
            toGet = page * photoNumPer;
        } else {
            toGet = (page - 1) * photoNumPer + remain;
        }
//        List<PhotoItem> toAdds = mItems.subList((page-1)*photoNumPer,toGet);
        List<PhotoItem> toAdds = new ArrayList<>();
        for (int i = (page - 1) * photoNumPer; i < toGet; i++) {
            toAdds.add(mItems.get(i));
        }
        //mAdapter.items.addAll(toAdds);
        PhotoItem[] photoItems = toAdds.toArray(new PhotoItem[]{});
        for (PhotoItem p : photoItems) {
            mAdapter.items.add(p);
        }
        mAdapter.notifyItemRangeInserted((page-1)*photoNumPer,toGet);
        Snackbar.make(getView(),"加载完成啦！",Snackbar.LENGTH_SHORT).show();
    }
    private void initViews(final View view) {
        initRecyclerView(view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initRecyclerView(view);
                Snackbar.make(view,"刷新完成~",Snackbar.LENGTH_SHORT).show();
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        Log.i(TAG, "onCreate: 跳出循环了！");
    }

    private void initRecyclerView(View view) {
        page = 1;
        mItems.clear();
        addPhotoItems(new File(FileManager.getmCurrentDir()));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.photo_recycler_view);
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int[] lastCompletelyVisibleItemPositions = mLayoutManager.findLastCompletelyVisibleItemPositions(new int[2]);
                //Log.i(TAG, "onScrolled: 最后一个可见的pisition："+lastCompletelyVisibleItemPositions[1]);
                int lastVisiblePosition = lastCompletelyVisibleItemPositions[1];
                //Log.i(TAG, "onScrolled: mItems.SIze:"+mItems.size());
                if (!mSwipeRefreshLayout.isRefreshing()
                        && (lastVisiblePosition == page * photoNumPer - 1 || lastVisiblePosition == mItems.size()-1)) {
                    getMoreItems();
                    Log.i(TAG, "onScrolled: mItems.Size:"+mItems.size());
                }
            }
        });
        setupAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_photo,container,false);
        initViews(view);
        return view;
    }

    private class PhotoHolder extends RecyclerView.ViewHolder{

        private ImageView mItemImageView;
        private TextView mTitle;

        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_gallery_image_view);
            mTitle = (TextView) itemView.findViewById(R.id.tv_photo_name);
        }

        public void bindDrawable(Drawable drawable){
            mItemImageView.setImageDrawable(drawable);
        }

        public void bindWithGlide(PhotoItem item){
            Glide.with(getActivity()).load(new File(item.getPath())).into(mItemImageView);
        }

        public void bindBitmap(Bitmap bitmap) {
            mItemImageView.setImageBitmap(bitmap);
        }
        public void bindTitle(String title) {
            mTitle.setText(title);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder>{

        public List<PhotoItem> items = new ArrayList<>();

        public PhotoAdapter(List<PhotoItem> l){
            for (PhotoItem p : l) {
                items.add(p);
            }
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            View view = inflater.inflate(R.layout.photo_item,parent,false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            PhotoItem mItem = items.get(position);
            holder.bindWithGlide(mItem);
            holder.bindTitle(mItem.getTitle());
            Log.i(TAG, "onBindViewHolder: 要解析的路径:"+mItem.getPath());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
