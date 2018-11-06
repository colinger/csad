package cn.cs.callme;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import cn.cs.callme.sdk.AdInfo;
import cn.cs.callme.sdk.CsAdSDK;

/**
 * 广告组件
 * 组件的浮标使用WebView加载
 */
public class CSAdView extends RelativeLayout {
    private WebView floatIconView;
    private LayoutParams layoutParams; //以下6行就是接口的回调！
    private String adUrl;
    private String picUrl;


    public CSAdView(Context context) {
        super(context);
    }

    public CSAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CSAdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(final Context context, AttributeSet attrs) {
        //
        proInit(context);
    }

    private void proInit(final Context context) {
        floatIconView = new WebView(context);
        floatIconView.setScrollBarStyle(View.SCREEN_STATE_OFF);
        //background color
        setBackgroundColor(Color.TRANSPARENT);
        //
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        addView(floatIconView, layoutParams);

        //
        floatIconView.getSettings().setJavaScriptEnabled(true);

        floatIconView.setBackgroundColor(Color.TRANSPARENT);

        floatIconView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        floatIconView.getSettings().setDomStorageEnabled(true);
        floatIconView.getSettings().setDatabaseEnabled(true);

        if(CsAdSDK.getInstance().isShowFloatIcon()){
            floatIconView.setVisibility(VISIBLE);
        }else{
            floatIconView.setVisibility(GONE);
        }

        //这两值要从远程获取
        picUrl = "http://www.sprzny.com/css/appfox/fubiao/26.gif";
        adUrl = "http://m.bianxianmao.com?appKey=3dfe434877e44560afb56068d1cb91f2&appType=app&appEntrance=5&business=money&i=__IMEI__&f=__IDFA__";
        AdInfo adInfo = CsAdSDK.getInstance().getAdInfo();
        if(adInfo != null){
            picUrl = adInfo.getPic();
            adUrl = adInfo.getUrl();
        }
        String data = "<HTML><Div align=\"center\" margin=\"0px\"><a href=\"http://www.baidu.com\"><IMG src=\"" + picUrl + "\" margin=\"0px\" height=\"60\" width=\"60\" /></a></Div></HTML>";//设置图片位于webview的中间位置
        floatIconView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);

        floatIconView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent webView = new Intent(context, CSAdDetailActivity.class);
                webView.putExtra("adUrl", adUrl);
                context.startActivity(webView);
                return true;
            }
        });
    }
}
