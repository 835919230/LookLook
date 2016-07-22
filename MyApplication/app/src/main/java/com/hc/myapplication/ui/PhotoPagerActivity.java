package com.hc.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hc.myapplication.R;
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

    private ViewPager mViewPager;
    private List<PhotoItem> mItems = new ArrayList<>();

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
//        PhotoItem item = PhotoItemLab.findById(id);
        mViewPager = (ViewPager) findViewById(R.id.photo_view_pager);
        FileManager.addPhotoItems(new File(FileManager.getmCurrentDir()),mItems);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                PhotoItem photoItem = PhotoItemLab.get(position);
                return new PhotoPagerFragment().newInstance(photoItem.getId());
            }

            @Override
            public int getCount() {
                return mItems.size();
            }
        });

        mViewPager.setCurrentItem(PhotoItemLab.getPosition(id));
    }
}
