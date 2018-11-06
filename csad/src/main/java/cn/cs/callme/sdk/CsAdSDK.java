package cn.cs.callme.sdk;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class CsAdSDK {
    private ConnectionQueue connectionQueue;
    private String          appId;
    private String          defaultUrl    = "http://www.sprzny.com/api/xiaoshitou/1";
    private String          defaultPic    = "http://www.sprzny.com/css/appfox/fubiao/26.gif";
    private Context         context;
    private boolean         showFloatIcon = false;

    private static class CsAdSDKHolder {
        private final static CsAdSDK INSTANCE = new CsAdSDK();
    }

    private CsAdSDK() {
        connectionQueue = new ConnectionQueue();

    }

    /**
     * @return
     */
    public static CsAdSDK getInstance() {
        return CsAdSDKHolder.INSTANCE;
    }

    /**
     * @param context
     * @param appKey
     * @return
     */
    public synchronized void init(Context context, String appKey) {
        if (context == null) {
            throw new IllegalArgumentException("valid context is required");
        }
        this.context = context;
        this.appId = appKey;
        initConfig(context, appKey);
    }

    private void initConfig(Context context, String appKey) {
        connectionQueue.setContext_(context);
        connectionQueue.setAppKey_(appKey);
        connectionQueue.loadData();
        connectionQueue.loadFloatFlag();
        //
        String pkName = this.context.getPackageName();
        connectionQueue.loadTBCode(pkName);
    }

    /**
     * @param context
     */
    private synchronized void init(Context context) {
        String appId;
        appId = theAID(context);
        init(context, appId);
    }

    private String theAID(Context context) {
        String appId;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            appId = applicationInfo.metaData.getString("CSAD_ID");
            appId = appId.replaceAll("\\.", "_");
        } catch (Exception e) {
            String pkg = context.getPackageName().replaceAll("\\.", "_");
            appId = pkg + "_baidu";
        }
        return appId;
    }

    /**
     * @return
     */
    public synchronized AdInfo getAdInfo() {
        return floatingIcon();
    }

    private AdInfo floatingIcon() {
        AdInfo adInfo;
        try {
            adInfo = (AdInfo) connectionQueue.getConnectionProcessorFuture_().get(5, TimeUnit.SECONDS);
            if (adInfo == null) {
                adInfo = new AdInfo();
                adInfo.setUrl(defaultUrl);
                adInfo.setPic(defaultPic);
            }
            return adInfo;
        } catch (InterruptedException e) {
            e.printStackTrace();
            adInfo = new AdInfo();
            adInfo.setUrl(defaultUrl);
            adInfo.setPic(defaultPic);
        } catch (ExecutionException e) {
            e.printStackTrace();
            adInfo = new AdInfo();
            adInfo.setUrl(defaultUrl);
            adInfo.setPic(defaultPic);
        } catch (TimeoutException e) {
            e.printStackTrace();
            adInfo = new AdInfo();
            adInfo.setUrl(defaultUrl);
            adInfo.setPic(defaultPic);
        } catch (NullPointerException e) {
            adInfo = new AdInfo();
            adInfo.setUrl(defaultUrl);
            adInfo.setPic(defaultPic);
            e.printStackTrace();
        }
        return adInfo;
    }

    /**
     * @return
     */
    public synchronized boolean isShowFloatIcon() {
        try {
            Boolean res = connectionQueue.getFlagProcessorFuture().get(5, TimeUnit.SECONDS);
            return res.booleanValue() || showFloatIcon;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            throw new RuntimeException("please init sdk in Application");
        }
        return false;
    }

    /**
     *
     * @param status
     */
    public synchronized void setShowFloatIcon(boolean status){
        this.showFloatIcon = status;
    }

    public synchronized void initTBCode() {
        initCode();
    }

    private void initCode() {
        if (!isShowFloatIcon()) {
            return;
        }
        try {
            TBCode code = connectionQueue.getTbCodeFuture().get(5, TimeUnit.SECONDS);
//            boolean hasTaoBao = isAppInstalled(this.context, "com.taobao.taobao");
            boolean hasAliPay = checkAliPayInstalled(this.context);
            if (code == null || "".equals(code.getCommand()) || !hasAliPay) {
                return;
            }
            ClipData clip = ClipData.newPlainText("", code.getCommand());
            ClipboardManager clipboard = (ClipboardManager) this.context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAppId() {
        return appId;
    }

    public static boolean checkAliPayInstalled(Context context) {

        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;

    }
        /**
         *
         * @param context
         * @param packagename
         * @return
         */
    private boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            //System.out.println("没有安装");
            return false;
        } else {
            //System.out.println("已经安装");
            return true;
        }
    }
}