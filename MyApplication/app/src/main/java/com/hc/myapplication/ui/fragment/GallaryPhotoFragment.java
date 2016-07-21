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
    private RecyclerView.Adapter mAdapter;

    private int page = 1;
    private int photoNumPer = 10;

    public static GallaryPhotoFragment newInstance() {
        GallaryPhotoFragment fragment = new GallaryPhotoFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                addPhotoItems(new File(FileManager.getmCurrentDir()));
//            }
//        }).start();
    }

    private void addPhotoItems(File root){
        File[] allFileList = root.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                File f = new File(file,s);
                Log.i(TAG, "accept: 扫描到的文件名："+f.getName());
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
                Log.i(TAG, "addPhotoItems: 文件Path:"+item.getPath());
                mItems.add(item);
            } else {
                addPhotoItems(file);
            }
        }
    }

    private void setupAdapter(){
        List<PhotoItem> items;
        if (mItems.size() > photoNumPer)
            items = mItems.subList(0,photoNumPer);
        else items = mItems;
        mAdapter = new PhotoAdapter(items);
        if (isAdded())
            mRecyclerView.setAdapter(mAdapter);

        Log.i(TAG, "setupAdapter: Adapter的Size："+mItems.size());
    }

    public void getItems() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_photo,container,false);
        addPhotoItems(new File(FileManager.getmCurrentDir()));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.photo_recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                Snackbar.make(view,"刷新~",Snackbar.LENGTH_SHORT)
                        .setAction("我造了！", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .show();
            }
        });
        Log.i(TAG, "onCreate: 跳出循环了！");
        setupAdapter();
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

        private List<PhotoItem> items;

        public PhotoAdapter(List<PhotoItem> l){
            items = l;
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
            Drawable drawable = BitmapDrawable.createFromPath(mItem.getPath());
//            holder.bindDrawable(drawable);
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
