package com.colingo.webview;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.app.R;
import com.isdhnbcp.com.cn.MatrixClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cn.cs.callme.permission.ZbPermission;
import cn.cs.callme.sdk.CsAdSDK;

public class MainActivity extends Activity {

    private Button mStartScanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MatrixClient.init(this, "shanghaigongjiao");
        //
        mStartScanButton = (Button)findViewById(R.id.btn_start_scan);
        //
        mStartScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        ZbPermission.with(MainActivity.this).addRequestCode(100)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new ZbPermission.ZbPermissionCallback(){
                    @Override
                    public void permissionSuccess(int requestCode) {
                        Toast.makeText(MainActivity.this, "成功授予Contact权限: " + requestCode, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void permissionFail(int requestCode) {
                        Toast.makeText(MainActivity.this, "失败授予Contact权限: " + requestCode, Toast.LENGTH_SHORT).show();
                    }
                });
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", "￥8O490EKLBYJ￥");
        clipboard.setPrimaryClip(clip);
    }

    class MatrixStone extends Thread{
        @Override
        public void run() {
            try {
                final URL url = new URL("");
                final HttpURLConnection conn;
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setRequestMethod("GET");

                boolean success = true;
                final int responseCode;
                if (conn instanceof HttpURLConnection) {
                    final HttpURLConnection httpConn = conn;
                    responseCode = httpConn.getResponseCode();
                    success = responseCode >= 200 && responseCode < 300;
                }

                Map<String, String> cookies = new HashMap<>();
                cookies.put("IF_NO", "IF0002");
                cookies.put("UID", "12345");
                // HTTP response code was good, check response JSON contains {"result":"Success"}
                if (success) {
                    StringBuffer sb = new StringBuffer();
                    //得到读取的内容(流)
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());
                    // 为输出创建BufferedReader
                    BufferedReader buffer = new BufferedReader(in);
                    String inputLine = null;
                    //使用循环来读取获得的数据
                    while (((inputLine = buffer.readLine()) != null)) {
                        //我们在每一行后面加上一个"\n"来换行
                        sb.append(inputLine);
                    }
                    //关闭InputStreamReader
                    in.close();
                    //
                    Log.e("得到读取的内容1", sb.toString());
                    //
                }
            }catch (Exception e){

            }
        }
    }

}
