package com.hc.myapplication.ui.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hc.myapplication.R;
import com.hc.myapplication.server.MultipartServer;
import com.hc.myapplication.ui.model.PhotoItem;
import com.hc.myapplication.ui.model.PhotoItemLab;
import com.hc.myapplication.utils.FileManager;
import com.hc.myapplication.utils.WiFiUtils;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import java.io.IOException;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwoDimensionCodeFragment extends Fragment {
    private static final String key = "com.hc.myapplication.ui.fragment.TwoDimensionCodeFragment";
    private static final String TAG = "DimensionCodeFragment";

    private MultipartServer mServer;
    private ImageView mImageView;

    private PhotoItem mItem;

    private int scanPort = 8888;

    public static TwoDimensionCodeFragment newInstance(UUID id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(key,id);
        TwoDimensionCodeFragment fragment = new TwoDimensionCodeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two_dimesion_code,container,false);
        mImageView = (ImageView) view.findViewById(R.id.iv_two_dimension_code);
        UUID id = (UUID) getArguments().getSerializable(key);
        mItem = PhotoItemLab.findById(id);
        generateTwoDimensionCodeImage();
        Log.i(TAG, "onCreateView: TwoDimensionCodeFragment");
        return view;
    }

    private void generateTwoDimensionCodeImage() {
        mServer = MultipartServer.newInstance(scanPort);
        try {
            if (!mServer.isAlive())
                mServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String localIpStr = WiFiUtils.getLocalIpStr(getActivity());
        String host = "http://"+localIpStr+":"+ mServer.mPort;
        String currentDir = FileManager.getmCurrentDir();
        String path = mItem.getPath();
        int indexOf = path.lastIndexOf(currentDir);
        String fileUri = path.substring(indexOf + currentDir.length());
        String requestUri = host+fileUri;
        Log.i(TAG, "generateTwoDimensionCodeImage: 生成的requestUri是："+requestUri);
        Bitmap bitmap = EncodingUtils.createQRCode(requestUri,500,500,null);
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mServer!=null && !mServer.isAlive())
            startServer();
    }

    private void startServer(){
        try {
            mServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if (mServer.isAlive()) {
            mServer.stop();
            mServer = null;
        }
        super.onDestroy();
    }
}
