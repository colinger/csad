package cn.cs.callme.sdk;

/**
 *
 */
public interface SplashAdListener {

    /**
     * 出错了
     * @param msg
     */
    void onNoAD(String msg);

    /**
     * 开屏AD已显示
     * @param
     */
    void onADPresent();
}
