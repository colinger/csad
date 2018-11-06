package cn.cs.callme.sdk;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class AdInfoProcessor implements Callable<AdInfo> {
    private static final int CONNECT_TIMEOUT_IN_MILLISECONDS = 3000;
    private static final int READ_TIMEOUT_IN_MILLISECONDS = 3000;
    private String appKey;
    private AdInfo adInfo;
    private Context context;
    public AdInfoProcessor(Context context, String appKey) {
        this.appKey = appKey;
        this.context = context;
    }

    /**
     * @return
     * @throws IOException
     */
    private URLConnection urlConnectionForEventData(String serverUrl) throws IOException {
        final URL url = new URL(serverUrl + "?appId=" + appKey);
        final HttpURLConnection conn;
        //
        conn = (HttpURLConnection) url.openConnection();
        //
        conn.setConnectTimeout(CONNECT_TIMEOUT_IN_MILLISECONDS);
        conn.setReadTimeout(READ_TIMEOUT_IN_MILLISECONDS);
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setRequestMethod("GET");

        return conn;
    }

    @Override
    public AdInfo call() {
        URLConnection conn = null;
        while (true) {
            try {
                String pkg =  context.getPackageName();
                String id = new String(Base64.encode(pkg.getBytes(), Base64.DEFAULT));
                // initialize and open connection
                conn = urlConnectionForEventData("http://www.sprzny.com/api/appfox/2?id="+id);
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
                    }
                    return adInfo;
                } else if (responseCode >= 400 && responseCode < 500) {
                    return null;
                } else {
                    // warning was logged above, stop processing, let next loadData take care of retrying
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // if exception occurred, stop processing, let next loadData take care of retrying
                return null;
            } finally {
                // free connection resources
                if (conn != null && conn instanceof HttpURLConnection) {
                    ((HttpURLConnection) conn).disconnect();
                }
            }
        }

    }
}
