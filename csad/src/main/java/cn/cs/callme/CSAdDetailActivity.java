package cn.cs.callme;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

public class CSAdDetailActivity extends Activity {
    private WebView mWebView;
    private String currentUrl = "";
    private RelativeLayout mScrollToolBar;
    private TextView mTitle;
    private Button mBtnBack;
    private String AD_URL = "http://m.bianxianmao.com?appKey=3dfe434877e44560afb56068d1cb91f2&appType=app&appEntrance=5&business=money&i=__IMEI__&f=__IDFA__";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cs_ad_main);
        //
        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        mTitle = (TextView) findViewById(R.id.txtTitle);
        mBtnBack = (Button) findViewById(R.id.btnBack);
        mScrollToolBar = (RelativeLayout) findViewById(R.id.titbar);
        //
        mScrollToolBar.setBackgroundResource(R.color.colorPrimary);

        /**注册下载完成广播**/
        registerReceiver(downloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setUseWideViewPort(true);
        //
        String adUrl = getIntent().getStringExtra("adUrl");
        if (!TextUtils.isEmpty(adUrl)) {
            AD_URL = adUrl;
        }
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
        mWebView.loadUrl(AD_URL);

        mWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                downloadApk(url);
            }
        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });
    }

    /***
     *
     * @param view
     */
    public void returnBack(View view) {
            this.finish();
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
                case KeyEvent.KEYCODE_BACK: {
                    finish();
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     */
    private void installApk() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        String filePath = "/sdcard/download/download.apk";
        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        startActivity(i);
    }

    /**
     * @param apkUrl
     */
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

    /**
     *
     */
    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /**下载完成后安装APK**/
            installApk();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(downloadCompleteReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}