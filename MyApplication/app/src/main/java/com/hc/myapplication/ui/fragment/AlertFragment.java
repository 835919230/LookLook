package com.hc.myapplication.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.hc.myapplication.R;
import com.hc.myapplication.ui.model.PhotoItem;
import com.hc.myapplication.ui.model.PhotoItemLab;
import com.hc.myapplication.utils.FileManager;

import java.io.File;
import java.util.UUID;

/**
 * Created by 诚 on 2016/7/23.
 */
public class AlertFragment extends DialogFragment {

    private static final String key = "com.hc.myapplication.ui.fragment.AlertFragment";
    private PhotoItem item;

    public static AlertFragment newInstance(UUID id){
        Bundle bundle = new Bundle();
        bundle.putSerializable(key,id);
        AlertFragment fragment = new AlertFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final UUID id = (UUID) getArguments().getSerializable(key);
        item = PhotoItemLab.findById(id);
        return new AlertDialog.Builder(getActivity())
                .setTitle("确认要删除"+item.getTitle()+"吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File file = new File(item.getPath());
                        if (file.exists())
                            file.delete();

                        PhotoItemLab.refresh();
                        Intent intent = new Intent();
                        intent.putExtra("position",PhotoItemLab.getPosition(id));
                        getTargetFragment().onActivityResult(PhotoPagerFragment.REQUEST_CODE,getActivity().RESULT_OK,intent);
                    }
                })
                .setNegativeButton("不了",null)
                .create();
    }
}
