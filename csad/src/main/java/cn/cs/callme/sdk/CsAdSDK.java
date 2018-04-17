package cn.cs.callme.sdk;

import android.content.Context;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class CsAdSDK {
    private ConnectionQueue connectionQueue;

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
        connectionQueue.setAppKey_(appKey);
        connectionQueue.loadData();
        connectionQueue.loadFloatFlag();
    }

    /**
     *
     * @return
     */
    public synchronized AdInfo getAdInfo() {
        try {
            return (AdInfo) connectionQueue.getConnectionProcessorFuture_().get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
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
        }
        return false;
    }
}