package com.hc.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hc.myapplication.Server.MultipartServer;
import com.hc.myapplication.utils.FileManager;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;


import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private Button mButton;
    private TextView mTextView;
    private ImageView mImageView;
    private Button makeButton;
    private EditText mEditText;

    private Button startButton;
    private TextView ipTextView;
    private MultipartServer mHelloServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mHelloServer = new MultipartServer(8080);//默认8080
        initView();
        initEvent();
        initFiles();

        try {
            mHelloServer.start();
            ipTextView.setText("请在浏览器上输入以下地址"+getLocalIpStr(getApplicationContext())+":8080");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //内部文件 openFileOutput;
        //       openFileInput;

    }
    private void initFiles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.writeFile(MainActivity.this,R.raw.hello,FileManager.INDEX,mHelloServer);
                FileManager.writeFile(MainActivity.this,R.raw.bootstrap,FileManager.BOOTSTRAP,mHelloServer);
                FileManager.writeFile(MainActivity.this,R.raw.jquery,FileManager.JQUERY,mHelloServer);
                FileManager.writeFile(MainActivity.this,R.raw.favicon,FileManager.FAVICON,mHelloServer);
                FileManager.writeFile(MainActivity.this,R.raw.jquery_form,FileManager.JQEURY_FORM,mHelloServer);
                FileManager.writeFile(MainActivity.this,R.raw.uploader,FileManager.UPLOADER,mHelloServer);
                FileManager.writeFile(MainActivity.this,R.raw.app,FileManager.APP,mHelloServer);
                FileManager.judgeFileExsits();
            }
        }).start();
    }

    private void initEvent() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), CaptureActivity.class),0);
            }
        });

        makeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditText.getText().toString();
                if (input.equals(""))
                    Toast.makeText(getApplicationContext(),"输入不能为空",Toast.LENGTH_SHORT).show();
                else {
                    Bitmap bitmap = EncodingUtils.createQRCode(input,500,500,null);
                    mImageView.setImageBitmap(bitmap);
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mHelloServer.start();
                    ipTextView.setText(getLocalIpStr(getApplicationContext()));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"阿偶出现异常了，要不重启下应用吧亲",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            Log.d("scan",result);
            mTextView.setText(result);
        }
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.btn_scanner);
        mTextView = (TextView) findViewById(R.id.tv_result);
        mImageView = (ImageView) findViewById(R.id.iv_qrcode);
        makeButton = (Button) findViewById(R.id.btn_maker);
        mEditText = (EditText) findViewById(R.id.et_content);

        startButton = (Button) findViewById(R.id.btn_start);
        ipTextView = (TextView) findViewById(R.id.tv_ip);
    }

    /**
     * 拿到手机连接wifi后的IP地址
     * @param context
     * @author HC
     * @return
     */
    private String getLocalIpStr(Context context) {
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return intToIpAddr(wifiInfo.getIpAddress());
    }

    /**
     * 拿到IP地址后输出
     * @author HC
     * @param ip
     * @return
     */
    private String intToIpAddr(int ip) {
        return (ip & 0xff) + "." + ((ip>>8)&0xff) + "." + ((ip>>16)&0xff) + "." + ((ip>>24)&0xff);
    }

    @Override
    protected void onDestroy() {
        if (mHelloServer.isAlive())
            mHelloServer.stop();
        super.onDestroy();
    }
}
