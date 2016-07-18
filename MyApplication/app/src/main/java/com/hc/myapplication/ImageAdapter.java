package com.hc.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by caizejian on 16/7/16.
 */
public class ImageAdapter extends ArrayAdapter<Image> {
    private int resourceId;

    private ViewHolder viewHolder;

    public ImageAdapter(Context context , int textViewResourceId , List<Image> objects){

        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position , View convertView , ViewGroup parent) {

        final Image image = getItem(position);
        /*
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView imageList = (ImageView)view.findViewById(R.id.imageList);
      //  Uri path = image.getImageUri();
       // Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(image.getImageUri()));
        imageList.setImageBitmap(image.getBitmap());
        return view;
        */
        View view ;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.imageList = (ImageView)view.findViewById(R.id.imageList);
            viewHolder.deleteImage = (Button)view.findViewById(R.id.deleteImage);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.imageList.setImageBitmap(image.getBitmap());
        viewHolder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               image.deleteImage();

            }
        });
        return view;
    }

    class ViewHolder{
        ImageView imageList;
        Button deleteImage;
    }
}
