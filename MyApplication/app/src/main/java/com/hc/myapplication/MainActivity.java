package com.hc.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
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


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.hc.myapplication.R.id.btn_send;


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

    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Log.i(TAG, "onCreate: 根目录:"+FileManager.getmCurrentDir());

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

    private Handler mHandler = new Handler();

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

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String host = (String) ipTextView.getText();
                        if (host == null || host.length() <= 0){
                            Toast.makeText(MainActivity.this,"还没启动服务器",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            URL url = new URL("http://"+getLocalIpStr(MainActivity.this)+":8080/123.jpg");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            InputStream in = conn.getInputStream();
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            int byteRead = 0;
                            byte[] buffer = new byte[1024];
                            while ((byteRead = in.read(buffer)) > 0)
                                out.write(buffer,0,byteRead);

                            final Bitmap bitmap = BitmapFactory.decodeByteArray(out.toByteArray(),0,out.toByteArray().length);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView)findViewById(R.id.downloadPhoto)).setImageBitmap(bitmap);
                                }
                            });
                        } catch (MalformedURLException e) {
                            Log.e(TAG, "onClick: ",e );
                        } catch (IOException e) {
                            Log.e(TAG, "onClick: ", e);
                        }
                    }
                }).start();
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
        btnSend = (Button) findViewById(btn_send);
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

    @Override
    protected void onStop() {
        super.onStop();
        if (mHelloServer.isAlive()) {
            Log.i(TAG, "onStop: Server is Alive!!!!!!!!!");
            mHelloServer.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mHelloServer.isAlive()) {
            Log.i(TAG, "onResume: Server 不是活着的！！！！！");
            try {
                mHelloServer.start();
            } catch (IOException e) {
                Log.e(TAG, "onResume: 发生错误了！", e);
            }
        }
    }
}
