package com.hc.myapplication.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hc.myapplication.R;
import com.hc.myapplication.ui.fragment.ServerFragment;
import com.hc.myapplication.utils.FileManager;

public class ServerActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private void initToolBar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("传图App");
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_server_fragment,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_server);
        initToolBar();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, ServerFragment.newInstance())
                .commit();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_gallery) {
            Intent intent = new Intent(this,GallaryPhotoActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
