package cn.cs.callme.sdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class CsAdSDK {
    private ConnectionQueue connectionQueue;
    private String appId;
    private String defaultUrl= "http://www.sprzny.com/api/xiaoshitou/1";
    private String defaultPic= "http://www.sprzny.com/css/appfox/fubiao/26.gif";
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
        this.appId = appKey;
        connectionQueue.setAppKey_(appKey);
        connectionQueue.loadData();
        connectionQueue.loadFloatFlag();
    }

    /**
     * @param context
     */
    public synchronized void init(Context context) {
        String appId;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            appId = applicationInfo.metaData.getString("CSAD_ID");
            appId = appId.replaceAll("\\.", "_");
        } catch (Exception e) {
            String pkg = context.getPackageName().replaceAll("\\.", "_");
            appId = pkg + "_baidu";
        }
        init(context, appId);
    }

    /**
     * @return
     */
    public synchronized AdInfo getAdInfo() {
        AdInfo adInfo;
        try {
            adInfo = (AdInfo) connectionQueue.getConnectionProcessorFuture_().get(5, TimeUnit.SECONDS);
            if(adInfo == null){
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
        } catch(NullPointerException e){
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
            return res.booleanValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch(NullPointerException e){
            throw new RuntimeException("please init sdk in Application");
        }
        return false;
    }

    public String getAppId() {
        return appId;
    }
}