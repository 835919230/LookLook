package com.hc.myapplication.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hc.myapplication.R;
import com.hc.myapplication.ui.fragment.GallaryPhotoFragment;

public class GallaryPhotoActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private void initToolBar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("相册");
        mToolbar.setLogo(R.mipmap.ic_menu_gallery);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
