package cn.cs.callme;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *
 */
public class CSAdView extends RelativeLayout {
    private Button leftButton, rightButton;
    private TextView tvTitile; //左边的Button一些属性
    private int leftTextColor;
    private Drawable leftBackground;
    private String leftText; //右边的Button的属性
    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText; //中间的属性
    private float titleTextSize;
    private int titleTextColor;
    private String title; //布局管理器 左Button ,右边Button ,中间的TextView
    private LayoutParams leftParams, rigthParams, titleParams; //以下6行就是接口的回调！
    TopberListener listener;

    public CSAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //TypeArray get attr of layout
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AdView);
        //color of left font
        leftTextColor = ta.getColor(R.styleable.AdView_leftTextColor, 0);
        //background color of left button
        leftBackground = ta.getDrawable(R.styleable.AdView_leftBackground);
        //font style of left button
        leftText = ta.getString(R.styleable.AdView_leftText);

        //color of right font
        rightTextColor = ta.getColor(R.styleable.AdView_rightTextColor, 0);
        //background color of right button
        rightBackground = ta.getDrawable(R.styleable.AdView_rightBackground);
        //font style of right button
        rightText = ta.getString(R.styleable.AdView_rightText);

        //middle of title
        title = ta.getString(R.styleable.AdView_title);
        //
        titleTextColor = ta.getColor(R.styleable.AdView_titleTextColor, 0);
        //
        titleTextSize = ta.getDimension(R.styleable.AdView_titleTextSize, 0);

        //recyle the TypedArray
        ta.recycle();

        //
        leftButton = new Button(context);
        rightButton = new Button(context);
        tvTitile = new TextView(context);

        //apply the attr
        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBackground);
        leftButton.setText(leftText);

        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBackground);
        rightButton.setText(rightText);

        tvTitile.setText(title);
        tvTitile.setTextColor(titleTextColor);
        tvTitile.setTextSize(titleTextSize);
        tvTitile.setGravity(Gravity.CENTER);

        //background color
        setBackgroundColor(0xfff59563);
        //
        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        addView(leftButton, leftParams);

        rigthParams = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rigthParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        addView(rightButton, rigthParams);

        titleParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(tvTitile, titleParams);

        //listener
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftClick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightClick();
            }
        });

    }

    public interface TopberListener {
        public void leftClick();

        public void rightClick();
    }

    public void setOnTopberClickListener(TopberListener listener) {
        this.listener = listener;
    }

}
