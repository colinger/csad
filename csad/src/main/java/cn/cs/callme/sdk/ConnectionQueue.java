package cn.cs.callme.sdk;

import android.content.Context;

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
        connectionProcessorFuture_ = executor_.submit(new AdInfoProcessor(this.appKey_));
    }

    public void loadFloatFlag() {
        flagProcessorFuture = executor_.submit(new FloatIconProcessor(this.appKey_));
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

    public Future<?> getConnectionProcessorFuture_() {
        return connectionProcessorFuture_;
    }

    public Future<Boolean> getFlagProcessorFuture() {
        return flagProcessorFuture;
    }

    public Future<TBCode> getTbCodeFuture() {
        return tbCodeFuture;
    }
}
