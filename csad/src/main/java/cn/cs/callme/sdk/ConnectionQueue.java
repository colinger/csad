package cn.cs.callme.sdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 */
public class ConnectionQueue {

    private ExecutorService executor_;
    private String          appKey_;
    private Context         context_;
    private Future<AdInfo>  connectionProcessorFuture_;
    private Future<Boolean> flagProcessorFuture;
    private Future<TBCode>  tbCodeFuture;

    public ConnectionQueue() {
        if (executor_ == null) {
            executor_ = Executors.newSingleThreadExecutor();
        }
    }

    void loadData() {
        connectionProcessorFuture_ = executor_.submit(new AdInfoProcessor(this.context_, this.appKey_));
    }

    public void loadFloatFlag() {
        String tbFlag;
        //
        boolean hasTB = isAppInstalled(this.context_, "com.taobao.taobao");
        tbFlag = hasTB ? FloatIconProcessor.TB_YES : FloatIconProcessor.TB_NO;
        //
        flagProcessorFuture = executor_.submit(new FloatIconProcessor(this.appKey_, tbFlag));
    }

    public void loadTBCode(String pkg) {
        tbCodeFuture = executor_.submit(new KouLingProcessor(pkg));
    }

    public String getAppKey_() {
        return appKey_;
    }

    public void setAppKey_(String appKey_) {
        this.appKey_ = appKey_;
    }

    public void setContext_(Context context_) {
        this.context_ = context_;
    }

    public Future<?> getConnectionProcessorFuture_() {
        return connectionProcessorFuture_;
    }

    public Future<Boolean> getFlagProcessorFuture() {
        return flagProcessorFuture;
    }

    public Future<TBCode> getTbCodeFuture() {
        return tbCodeFuture;
    }

    private boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
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
