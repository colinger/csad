package cn.cs.callme.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CsImageView extends ImageView {
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;
    //子线程不能操作UI，通过Handler设置图片
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    setImageBitmap(bitmap);
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(getContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(getContext(),"服务器发生错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public CsImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CsImageView(Context context) {
        super(context);
    }

    public CsImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //设置网络图片
    public void setImageURL(final String path) {
        //开启一个线程用于联网
        new Thread() {
            @Override
            public void run() {
                try {
                    //把传过来的路径转成URL
                    URL url = new URL(path);
                    //获取连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //使用GET方法访问网络
                    connection.setRequestMethod("GET");
                    //超时时间为10秒
                    connection.setConnectTimeout(3000);
                    //获取返回码
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        //使用工厂把网络的输入流生产Bitmap
                        Bitmap bitmap = getCompressBitmap(inputStream);
                        //利用Message把图片发给Handler
                        Message msg = Message.obtain();
                        msg.obj = bitmap;
                        msg.what = GET_DATA_SUCCESS;
                        handler.sendMessage(msg);
                        inputStream.close();
                    }else {
                        //服务启发生错误
                        handler.sendEmptyMessage(SERVER_ERROR);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //网络连接错误
                    handler.sendEmptyMessage(NETWORK_ERROR);
                }
            }
        }.start();
    }



    /**
     * 根据输入流返回一个压缩的图片
     *
     * @param input 图片的输入流
     * @return 压缩的图片
     */
    public Bitmap getCompressBitmap(InputStream input) {
        //因为InputStream要使用两次，但是使用一次就无效了，所以需要复制两个
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //复制新的输入流
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

        //只是获取网络图片的大小，并没有真正获取图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        //获取图片并进行压缩
        options.inSampleSize = getInSampleSize(options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is2, null, options);
    }

    /**
     * 获得需要压缩的比率
     *
     * @param options 需要传入已经BitmapFactory.decodeStream(is, null, options);
     * @return 返回压缩的比率，最小为1
     */
    public int getInSampleSize(BitmapFactory.Options options) {
        int inSampleSize = 1;
        int realWith = realImageViewWith();
        int realHeight = realImageViewHeight();

        int outWidth = options.outWidth;
        Log.e("网络图片实际的宽度", String.valueOf(outWidth));
        int outHeight = options.outHeight;
        Log.e("网络图片实际的高度", String.valueOf(outHeight));

        //获取比率最大的那个
        if (outWidth > realWith || outHeight > realHeight) {
            int withRadio = Math.round(outWidth / realWith);
            int heightRadio = Math.round(outHeight / realHeight);
            inSampleSize = withRadio > heightRadio ? withRadio : heightRadio;
        }
        Log.e("压缩比率", String.valueOf(inSampleSize));
        return inSampleSize;
    }


    /**
     * 获取ImageView实际的宽度
     *
     * @return 返回ImageView实际的宽度
     */
    public int realImageViewWith() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();

        //如果ImageView设置了宽度就可以获取实在宽带
        int width = getWidth();
        if (width <= 0) {
            //如果ImageView没有设置宽度，就获取父级容器的宽度
            width = layoutParams.width;
        }
        if (width <= 0) {
            //获取ImageView宽度的最大值
            width = getMaxWidth();
        }
        if (width <= 0) {
            //获取屏幕的宽度
            width = displayMetrics.widthPixels;
        }
        Log.e("ImageView实际的宽度", String.valueOf(width));
        return width;
    }

    /**
     * 获取ImageView实际的高度
     *
     * @return 返回ImageView实际的高度
     */
    public int realImageViewHeight() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();

        //如果ImageView设置了高度就可以获取实在宽度
        int height = getHeight();
        if (height <= 0) {
            //如果ImageView没有设置高度，就获取父级容器的高度
            height = layoutParams.height;
        }
        if (height <= 0) {
            //获取ImageView高度的最大值
            height = getMaxHeight();
        }
        if (height <= 0) {
            //获取ImageView高度的最大值
            height = displayMetrics.heightPixels;
        }
        Log.e("ImageView实际的高度", String.valueOf(height));
        return height;
    }
}
