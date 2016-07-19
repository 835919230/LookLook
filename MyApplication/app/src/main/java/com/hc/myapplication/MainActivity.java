package com.hc.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

//    @Bind(R.id.toolbar)
    private Toolbar mToolbar;

    private void initToolBar(){
//        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("传图App");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,new GridPhotoFragment())
                .commit();
    }
}
