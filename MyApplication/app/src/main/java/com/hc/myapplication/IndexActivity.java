package com.hc.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by caizejian on 16/7/13.
 */
public class IndexActivity extends AppCompatActivity {
    private Button startServer ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        getSupportActionBar().hide();

        startServer = (Button)findViewById(R.id.startServer);
        startServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
