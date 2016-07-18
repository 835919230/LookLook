package com.hc.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caizejian on 16/7/16.
 */
public class ImageListActivity extends AppCompatActivity {
    private List<Image> imageList = new ArrayList<Image>();

  //  private Button deleteImage;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.image_list_view);
        getSupportActionBar().hide();
        File outputImage = new File(Environment.getExternalStorageDirectory(), "youqubao");
        // Uri fileUri = Uri.fromFile(outputImage);
        initImages(outputImage);
        ImageAdapter adapter = new ImageAdapter(ImageListActivity.this,R.layout.image_list,imageList);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Image image = imageList.get(position);
                switch (view.getId()){
                    case R.id.imageList:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ImageListActivity.this);
                        dialog.setTitle("图片管理");
                        dialog.setMessage("选择操作");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("删除图片",new DialogInterface.OnClickListener(){
                        @Override
                                public void onClick(DialogInterface dialog,int which){
                            image.delete();
                        }
                    });
                        dialog.setNegativeButton("取消操作",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog,int which) {

                            }
                        });
                        dialog.show();
                        break;
                    default:
                        break;
                }
            }
        });*/

    //    deleteImage = (Button)findViewById(R.id.deleteImage);
        
    }

    private void initImages(File outputImage){

        File[] subFile = outputImage.listFiles();
        for(int iFileLength = 0 ; iFileLength < subFile.length ; iFileLength++){
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                if(filename.trim().toLowerCase().endsWith(".jpg")){
                    Uri imageUri = Uri.fromFile(subFile[iFileLength]);
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        // picture.setImageBitmap(bitmap);
                        Image image = new Image(imageUri,bitmap);
                        imageList.add(image);
                        //将图片通过广播存至本地相册
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri uri = Uri.fromFile(subFile[iFileLength]);
                        intent.setData(uri);
                        sendBroadcast(intent);

                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
