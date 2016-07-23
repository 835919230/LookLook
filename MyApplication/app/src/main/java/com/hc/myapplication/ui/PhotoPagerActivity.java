package com.hc.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.hc.myapplication.R;
import com.hc.myapplication.ui.fragment.AlertFragment;
import com.hc.myapplication.ui.fragment.PhotoPagerFragment;
import com.hc.myapplication.ui.model.PhotoItem;
import com.hc.myapplication.ui.model.PhotoItemLab;
import com.hc.myapplication.utils.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PhotoPagerActivity extends AppCompatActivity {

    private static final String extra = "com.hc.myapplication.ui.PhotoPagerActivity";
    private static final String TAG = "PhotoPagerActivity";

    private static final String ConfirmDialogTag = "com.hc.myapplication.ui.PhotoPagerActivity.Tag";

    public static ViewPager mViewPager;
    public static FragmentStatePagerAdapter mAdapter;
    private List<PhotoItem> mItems;

    private int curItemIndex;
    private UUID curId;

    public static Intent newIntent(Context context , UUID id) {
        Intent intent = new Intent(context,PhotoPagerActivity.class);
        intent.putExtra(extra,id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pager);
        UUID id = (UUID) getIntent().getSerializableExtra(extra);
        initViewPager();
        curItemIndex = PhotoItemLab.getPosition(id);
        curId = id;
        initViewPager();
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.photo_view_pager);
        mItems = PhotoItemLab.sPhotoItems;
        FragmentManager fragmentManager = getSupportFragmentManager();
        mAdapter = new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                PhotoItem photoItem = PhotoItemLab.get(position);
                return new PhotoPagerFragment().newInstance(photoItem.getId());
            }

            @Override
            public int getCount() {
                return mItems.size();
            }
        };
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PhotoItem photoItem = PhotoItemLab.get(position);
                Log.i(TAG, "getItem: position:"+position);
                curItemIndex = position;
                curId = photoItem.getId();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(curItemIndex);
    }
}
