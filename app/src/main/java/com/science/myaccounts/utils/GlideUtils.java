package com.science.myaccounts.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.science.myaccounts.R;

/**
 * @author 幸运Science-陈土燊
 * @description Glide工具类
 * @school University of South China
 * @company wiwide.com
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2015/10/25
 */
public class GlideUtils {

    private Context mContext;

    private GlideUtils(Context context) {
        mContext = context;
    }

    private static GlideUtils mGlideUtils;

    public static GlideUtils getInstance(Context context) {
        if (mGlideUtils == null) {
            return new GlideUtils(context);
        } else {
            return mGlideUtils;
        }
    }

    public void setImage(String url, final ImageView imageView) {
        // 解决CircleImageView（第三方自定义的圆形Imageview）在使用Glide的占位图是不显示问题
        Glide.with(mContext).load(url).centerCrop().placeholder(R.mipmap.ic_launcher).crossFade().
                into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        imageView.setImageDrawable(resource);
                    }
                });
    }
}
