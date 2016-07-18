package com.hc.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by caizejian on 16/7/13.
 */
public class TitleLayout extends LinearLayout{
    private Button index;

    private Button photoManagement;


    private Context context;

    public TitleLayout(Context context , AttributeSet attrs){

        super(context , attrs);

        LayoutInflater.from(context).inflate(R.layout.title,this);

        index = (Button)findViewById(R.id.index);

        photoManagement = (Button)findViewById(R.id.photoManagement);

        index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.yxhapp.INDEX");
                // intent.addCategory("android.intent.category.LAUNCHER");
                ((Activity)getContext()).startActivity(intent);
            }
        });

        photoManagement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.yxhapp.IMAGELIST");
                ((Activity)getContext()).startActivity(intent);
            }
        });

    }
}
