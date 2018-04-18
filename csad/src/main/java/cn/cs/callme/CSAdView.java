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
    private Context context;
    private String adUrl;
    private String picUrl;


    public CSAdView(Context context) {
        super(context);
        this.context = context;
    }

    public CSAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CSAdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.cs_ad_view, this);
        this.floatIconView = (WebView) findViewById(R.id.icon);
        floatIconView.setScrollBarStyle(View.SCREEN_STATE_OFF);
        //background color
        setBackgroundColor(Color.TRANSPARENT);
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
