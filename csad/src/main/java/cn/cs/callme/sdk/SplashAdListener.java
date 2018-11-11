package cn.cs.callme.sdk;

/**
 *
 */
public interface SplashAdListener {
    /**
     * 没有显示
     */
    void onADDismissed();

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

    /**
     * 点击AD
     */
    void onADClicked();
}
