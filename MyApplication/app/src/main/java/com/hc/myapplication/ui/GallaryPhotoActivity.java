package com.hc.myapplication.ui;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hc.myapplication.R;
import com.hc.myapplication.ui.fragment.GallaryPhotoFragment;
import com.hc.myapplication.utils.Downloader;
import com.xys.libzxing.zxing.activity.CaptureActivity;

public class GallaryPhotoActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 614;

    private Toolbar mToolbar;
    private void initToolBar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("相册");
        mToolbar.setLogo(R.mipmap.ic_collections_white_36dp);
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        initToolBar();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, GallaryPhotoFragment.newInstance())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_fragment,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_back){
            finish();
        } else if (item.getItemId() == R.id.action_scan_two_dimension) {
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivityForResult(intent,REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // TODO: 2016/7/23 完成二维码扫描下载功能
            String urlSpec = data.getStringExtra("result");
            if (urlSpec != null && urlSpec.length() > 0) {
                Downloader.downFile(urlSpec);
                Snackbar.make(getCurrentFocus(), "扫描完成，下载即将开始", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(getCurrentFocus(), "失败，地址有问题", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
