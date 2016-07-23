package com.hc.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.hc.myapplication.R;
import com.hc.myapplication.ui.fragment.TwoDimensionCodeFragment;
import com.hc.myapplication.ui.model.PhotoItem;
import com.hc.myapplication.ui.model.PhotoItemLab;

import java.util.UUID;

public class TwoDimensionCodeActivity extends AppCompatActivity {

    private final static String key = "com.hc.myapplication.ui.TwoDimensionCodeActivity";

    private UUID mId;
    private Toolbar mToolbar;

    public static Intent newIntent(Context context, UUID id){
        Intent intent = new Intent(context,TwoDimensionCodeActivity.class);
        intent.putExtra(key,id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_dimension_code);
        UUID id = (UUID) getIntent().getSerializableExtra(key);
        mId = id;
        initToolBar();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, TwoDimensionCodeFragment.newInstance(mId))
                .commit();
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        PhotoItem item = PhotoItemLab.findById(mId);
        mToolbar.setTitle("来扫码下载"+item.getTitle()+"吧！");
        mToolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_36dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
