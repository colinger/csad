package com.starbaba.fragment;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.app.R;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;

public class WorthFragment extends BaseFragment {

    private static final java.lang.String AD_URL = "https://product.haibaobaoxian.com/index?pcode=chawz-h5-all";
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
        View view = inflater.inflate(R.layout.worth_main_layout, container, false);

        mWebView = (WebView) view.findViewById(R.id.worthWebView);
        if(mWebView == null){
            Toast.makeText(this.getContext(),"1111111",Toast.LENGTH_LONG);
            return view;
        }else {
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setSupportMultipleWindows(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setBlockNetworkImage(false);
            mWebView.getSettings().setUseWideViewPort(true);
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
                mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }

            WebChromeClient wvcc = new WebChromeClient() {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                }

            };
            mWebView.setWebChromeClient(wvcc);
            //
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url == null) return false;

                    try {
                        if (url.startsWith("weixin://") //微信
                                || url.startsWith("alipays://") //支付宝
                                || url.startsWith("mailto://") //邮件
                                || url.startsWith("tel://")//电话
                                || url.startsWith("dianping://")//大众点评
                                || url.startsWith("tbopen://")//大众点评
                            //其他自定义的scheme
                                ) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                            return true;
                        }
                    } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                        return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                    }

                    //处理http和https开头的url
                    view.loadUrl(url);
                    return true;
                }
            });
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
