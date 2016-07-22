package com.hc.myapplication.ui.model;

import com.hc.myapplication.utils.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 诚 on 2016/7/21.
 */
public class PhotoItemLab {
    public static List<PhotoItem> sPhotoItems;
    static {
        sPhotoItems = new ArrayList<>();
        FileManager.addPhotoItems(new File(FileManager.getmCurrentDir()),sPhotoItems);
    }

    public static PhotoItem findById(UUID id) {
        for (PhotoItem photoItem : sPhotoItems) {
            if (id.equals(photoItem.getId())) {
                return photoItem;
            }
        }
        return null;
    }

    public static PhotoItem get (int position) {
        return sPhotoItems.get(position);
    }

    public static int getPosition(UUID id) {
        int pos = 0;
        for (int i = 0;i < sPhotoItems.size(); i++) {
            if (sPhotoItems.get(i).getId().equals(id))
                pos = i ;
        }
        return pos;
    }


    public static void refresh(){
        sPhotoItems.clear();
        FileManager.addPhotoItems(new File(FileManager.getmCurrentDir()),sPhotoItems);
    }
}
