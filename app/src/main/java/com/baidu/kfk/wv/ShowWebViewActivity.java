package com.baidu.kfk.wv;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.app.R;

import java.io.File;

public class ShowWebViewActivity extends Activity {

    private WebView  mWebView;
    private TextView mTitle;
    private String   currentUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_main);
        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        mTitle = (TextView) findViewById(R.id.activity_main_webview_title);

        /**注册下载完成广播**/
        registerReceiver(downloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setUseWideViewPort(true);

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTitle.setText(title);
                currentUrl = view.getOriginalUrl();
            }

        };
        mWebView.setWebChromeClient(wvcc);
        //
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mWebView.loadUrl("http://m.bianxianmao.com?appKey=3dfe434877e44560afb56068d1cb91f2&appType=app&appEntrance=5&business=money&i=__IMEI__&f=__IDFA__");

        mWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                downloadApk(url);
            }
        });

    }

    /***
     *
     * @param view
     */
    public void returnBack(View view) {
        if (this.currentUrl != null && this.currentUrl.contains("bianxianmao.com")) {
            this.finish();
        } else {
            mWebView.loadUrl("http://m.bianxianmao.com?appKey=3dfe434877e44560afb56068d1cb91f2&appType=app&appEntrance=5&business=money&i=__IMEI__&f=__IDFA__");
        }
    }

    // Prevent the back-button from closing the app
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                //                    if (mWebView.canGoBack()) {
                //                        mWebView.goBack();
                //                    } else
                {
                    finish();
                }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void installApk() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        String filePath = "/sdcard/download/download.apk";
        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        startActivity(i);
    }

    private void downloadApk(String apkUrl) {
        File f = new File("/sdcard/download/download.apk");
        if (f.exists()) {
            f.delete();
        }
        Uri uri = Uri.parse(apkUrl);
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(request.NETWORK_MOBILE | request.NETWORK_WIFI);
        //设置是否允许漫游
        request.setAllowedOverRoaming(false);
        //设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(apkUrl));
        request.setMimeType(mimeString);
        //在通知栏中显示
        request.setNotificationVisibility(request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //        request.setTitle("download...");
        request.setVisibleInDownloadsUi(true);
        //sdcard目录下的download文件夹
        request.setDestinationInExternalPublicDir("/download", "download.apk");
        // 将下载请求放入队列
        downloadManager.enqueue(request);
    }

    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /**下载完成后安装APK**/
            installApk();
        }
    };
}