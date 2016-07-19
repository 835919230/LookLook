package com.hc.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hc.myapplication.utils.FileManager;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 诚 on 2016/7/19.
 */
public class GridPhotoFragment extends Fragment {

    private static final String TAG = "GridPhotoFragment";
    private RecyclerView mRecyclerView;

    private List<PhotoItem> mItems = new ArrayList<>();

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
        if (isAdded())
            mRecyclerView.setAdapter(new PhotoAdapter(mItems));

        Log.i(TAG, "setupAdapter: Adapter的Size："+mItems.size());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo,container,false);
        addPhotoItems(new File(FileManager.getmCurrentDir()));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.photo_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        Log.i(TAG, "onCreate: 跳出循环了！");
        setupAdapter();
        return view;
    }

    private class PhotoHolder extends RecyclerView.ViewHolder{

        private ImageView mItemImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_gallery_image_view);
        }

        public void bindDrawable(Drawable drawable){
            mItemImageView.setImageDrawable(drawable);
        }

        public void bindBitmap(Bitmap bitmap) {
            mItemImageView.setImageBitmap(bitmap);
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
            holder.bindDrawable(drawable);
            Log.i(TAG, "onBindViewHolder: 要解析的路径:"+mItem.getPath());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
