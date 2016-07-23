package com.hc.myapplication.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hc.myapplication.R;
import com.hc.myapplication.ui.PhotoPagerActivity;
import com.hc.myapplication.ui.model.PhotoItem;
import com.hc.myapplication.ui.model.PhotoItemLab;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by 诚 on 2016/7/21.
 */
public class PhotoPagerFragment extends Fragment {

    private static String key = "com.hc.myapplication.PhotoPagerFragment";

    private static final String ConfirmDialogTag = "com.hc.myapplication.ui.PhotoPagerFragment.Tag";

    public static final int REQUEST_CODE = 19960614;

    public UUID mId;

    public static PhotoPagerFragment newInstance(UUID id){
        PhotoPagerFragment fragment = new PhotoPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(key,id);
        fragment.setArguments(bundle);
        return fragment;
    }

    private ImageView mImageView;
    private Toolbar mToolbar;

    private PhotoItem mItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_pager,container,false);
        mImageView = (ImageView) view.findViewById(R.id.fragment_photo_pager_image_view);
        UUID id = (UUID) getArguments().getSerializable(key);
        mId = id;
        mItem = PhotoItemLab.findById(id);
        Glide.with(getActivity()).load(new File(mItem.getPath())).into(mImageView);
        initToolBar(view,id);
//        getFragmentManager().getFragments().remove(this);
//        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        return view;
    }

    private void initToolBar(View view , final UUID id){
        PhotoItem item = PhotoItemLab.findById(id);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        mToolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_36dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        mToolbar.inflateMenu(R.menu.menu_photo_pager_activity);
        mToolbar.setTitle(item.getTitle());
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_delete:
                        AlertFragment alertFragment = AlertFragment.newInstance(id);
                        alertFragment.setTargetFragment(PhotoPagerFragment.this,PhotoPagerFragment.REQUEST_CODE);
                        alertFragment.show(getActivity().getSupportFragmentManager(),ConfirmDialogTag);
                        break;

                    case R.id.action_share:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        Uri uri = Uri.fromFile(new File(mItem.getPath()));
                        intent.putExtra(Intent.EXTRA_STREAM,uri);
                        startActivity(intent);
                        break;
                    
                    case R.id.action_generate_two_dimension:
                        // TODO: 2016/7/23  完成生成二维码功能
                        break;
                }
                return false;
            }
        });
        view.findViewById(R.id.app_bar_layout).setAlpha(0.5f);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == getActivity().RESULT_OK)
        {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            PhotoPagerActivity.mAdapter.notifyDataSetChanged();
            PhotoPagerActivity.mAdapter = null;
            PhotoPagerActivity.mAdapter = new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    PhotoItem photoItem = PhotoItemLab.get(position);
                    return new PhotoPagerFragment().newInstance(photoItem.getId());
                }

                @Override
                public int getCount() {
                    return PhotoItemLab.sPhotoItems.size();
                }
            };
            PhotoPagerActivity.mViewPager.setAdapter(PhotoPagerActivity.mAdapter);
            PhotoPagerActivity.mViewPager.setCurrentItem((data.getExtras().getInt("position")),true);
            if (getActivity().getSupportFragmentManager().getFragments().size() <= 0)
                getActivity().finish();
        }
    }
}
