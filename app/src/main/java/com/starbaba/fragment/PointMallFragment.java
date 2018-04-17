package com.starbaba.fragment;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.app.R;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;

public class PointMallFragment extends BaseFragment {

    private static final String AD_URL = "https://auto.news18a.com/m/price/index/index/chejiao/";
    private WebView                       mWebView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:{
                    webViewGoBack();
                }break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pointmall_fragment_layout, container, false);

        mWebView = (WebView) view.findViewById(R.id.worthWebView);
        if(mWebView == null){
            Toast.makeText(this.getContext(),"",Toast.LENGTH_LONG);
            return view;
        }else {
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
                }

            };
            mWebView.setWebChromeClient(wvcc);
            //
            mWebView.setWebViewClient(new NoAdWebViewClient(this.getContext(), mWebView));
            mWebView.loadUrl(AD_URL);

            mWebView.setDownloadListener(new DownloadListener() {
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    downloadApk(url);
                }
            });
            mWebView.setOnKeyListener(new View.OnKeyListener(){

                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && event.getAction() == MotionEvent.ACTION_UP
                            && mWebView.canGoBack()) {
                        handler.sendEmptyMessage(1);
                        return true;
                    }

                    return false;
                }

            });
            return view;
        }
    }

    @Override
    public boolean j() {
        return false;
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
        DownloadManager downloadManager = (DownloadManager) this.getActivity().getSystemService(DOWNLOAD_SERVICE);
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

    private void webViewGoBack(){
        mWebView.goBack();
    }
}
