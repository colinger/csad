package cn.cs.callme.sdk;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.cs.callme.CSAdDetailActivity;

/**
 *
 */
public class SplashAD {
    private Class targetClass;
    private ViewGroup container;
    private TextView skipView;
    private SplashAdListener adListener;
    private CsImageView mWebView;
    private boolean isClicked = false;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(skipView == null){
                return;
            }
            if(msg.what - 1 == 0){
                skipView.setText("跳过");
            }else {
                skipView.setText("剩余 " + (msg.what - 1));
            }
        }
    };

    public SplashAD(Class mainClass, ViewGroup adContainer, TextView skipContainer, String appId, SplashAdListener adListener) {
        this.targetClass = mainClass;
        this.container = adContainer;
        this.skipView = skipContainer;
        this.adListener = adListener;
        //访问请求
        if(skipView != null){
            skipView.setVisibility(View.INVISIBLE);
        }
        load(appId, adListener, skipView);
    }

    /**
     * @param appId
     * @param adListener
     * @param skipView
     */
    private void load(final String appId, final SplashAdListener adListener, TextView skipView) {
        //
        LoadTask myAsyThread = new LoadTask(appId);
        myAsyThread.execute(container);//相当于Thread.start(传入参数);
    }

    /**
     * @param adInfo
     * @param adListener
     * @param container
     */
    private void fetchAndShow(final AdInfo adInfo, final SplashAdListener adListener, final ViewGroup container) {
        if (container != null) {
            adListener.onADPresent();
        } else {
            adListener.onNoAD("error");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 6; i > 0; i--) {
                    handler.sendEmptyMessage(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //
                if(!isClicked) {
                    adListener.onNoAD("");
                }
                //
            }
        }).start();
    }

    private URLConnection urlConnectionForEventData(String appId) throws IOException {

        final URL url = new URL("http://www.sprzny.com/api/kaiping?appid=" + appId +"&tt="+ encodeX(container.getContext().getPackageName()));
        final HttpURLConnection conn;
        //
        conn = (HttpURLConnection) url.openConnection();
        //
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setRequestMethod("GET");

        return conn;
    }

    private String encodeX(String packageName) {
        return EncryptUtil.encryptDES(packageName);
    }

    private class LoadTask extends AsyncTask<ViewGroup, Integer, AdInfo> {
        private String appId;

        public LoadTask(String appId) {
            this.appId = appId;
        }

        @Override
        protected AdInfo doInBackground(ViewGroup... viewGroups) {
            URLConnection conn = null;
            AdInfo adInfo = null;
            try {
                // initialize and open connection
                conn = urlConnectionForEventData(appId);
                conn.connect();

                // response code has to be 2xx to be considered a success
                boolean success = true;
                final int responseCode;
                if (conn instanceof HttpURLConnection) {
                    final HttpURLConnection httpConn = (HttpURLConnection) conn;
                    responseCode = httpConn.getResponseCode();
                    success = responseCode >= 200 && responseCode < 300;
                } else {
                    responseCode = 0;
                }

                // HTTP response code was good, check response JSON contains {"result":"Success"}
                if (success) {
                    adInfo = new AdInfo();
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
                    String[] content = sb.toString().split(",");
                    Pattern _pattern = Pattern.compile("(http://.*)\"");
                    for (String each : content) {
                        if (each.contains("url")) {
                            Matcher _match = _pattern.matcher(each);
                            if (_match.find()) {
                                String _url = _match.group();
                                adInfo.setUrl(_url.substring(0, _url.length() - 1));
                            }
                        }
                        if (each.contains("pic")) {
                            Matcher _match = _pattern.matcher(each);
                            if (_match.find()) {
                                String _url = _match.group();
                                adInfo.setPic(_url.substring(0, _url.length() - 1));
                            }
                        }
                        if (each.contains("close")) {
                            adInfo.setClose(each.substring(8, each.length()-1));
                        }
                    }
                } else if (responseCode >= 400 && responseCode < 500) {
                    //
                    adListener.onNoAD("400 Error");
                } else {
                    // warning was logged above, stop processing, let next loadData take care of retrying
                    adListener.onNoAD("400 Error");
                }
            } catch (Exception e) {
                e.printStackTrace();
                // if exception occurred, stop processing, let next loadData take care of retrying
                adListener.onNoAD(e.getLocalizedMessage());
            } finally {
                // free connection resources
                if (conn != null && conn instanceof HttpURLConnection) {
                    ((HttpURLConnection) conn).disconnect();
                }
            }
            return adInfo;
        }

        /**
         * doInBackground方法返回后，会调用该方法，
         * 并将 doInBackground方法的返回值作为该方法的参数；
         *
         * @param adInfo doInBackground方法的返回值;
         */
        @Override
        protected void onPostExecute(final AdInfo adInfo) {
            super.onPostExecute(adInfo);
            if (adInfo != null && "false".toLowerCase().equals(adInfo.getClose())) {
                //显示图片
                mWebView = new CsImageView(container.getContext());
                mWebView.setImageURL(adInfo.getPic());
                mWebView.setScaleType(ImageView.ScaleType.FIT_XY);
                mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                //
                container.addView(mWebView);
                mWebView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isClicked = true;
                        Intent intent = new Intent(container.getContext(), CSAdDetailActivity.class);
                        intent.putExtra("adUrl", adInfo.getUrl());
                        intent.putExtra("targetClass", targetClass);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        container.getContext().startActivity(intent);
                    }
                });
                //
                fetchAndShow(adInfo, adListener, container);
                //
                if(skipView != null){
                    skipView.setVisibility(View.VISIBLE);
                }
            }else{
                adListener.onNoAD("switch closed");
            }
        }
    }
}
