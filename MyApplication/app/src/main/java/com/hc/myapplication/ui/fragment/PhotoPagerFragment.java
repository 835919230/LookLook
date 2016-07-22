package com.hc.myapplication.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hc.myapplication.R;
import com.hc.myapplication.ui.model.PhotoItem;
import com.hc.myapplication.ui.model.PhotoItemLab;

import java.io.File;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by 诚 on 2016/7/21.
 */
public class PhotoPagerFragment extends Fragment {

    private static String key = "com.hc.myapplication.PhotoPagerFragment";

    public static PhotoPagerFragment newInstance(UUID id){
        PhotoPagerFragment fragment = new PhotoPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(key,id);
        fragment.setArguments(bundle);
        return fragment;
    }

    private ImageView mImageView;
    private Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_pager,container,false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        mImageView = (ImageView) view.findViewById(R.id.fragment_photo_pager_image_view);
        UUID id = (UUID) getArguments().getSerializable(key);
        PhotoItem item = PhotoItemLab.findById(id);
        mToolbar.setTitle(item.getTitle());
        Glide.with(getActivity()).load(new File(item.getPath())).into(mImageView);
        return view;
    }
}
