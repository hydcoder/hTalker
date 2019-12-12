package com.hyd.common.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import net.qiujuer.widget.airpanel.AirPanelLinearLayout;

/**
 * 解决添加fitsSystemWindows属性后，状态栏无法被沉浸的问题
 *
 * Created by hydCoder on 2019/11/18.
 * 以梦为马，明日天涯。
 */
public class MessageLayout extends AirPanelLinearLayout {
    public MessageLayout(Context context) {
        super(context);
    }

    public MessageLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MessageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            insets.left = 0;
            insets.right = 0;
            insets.top = 0;
        }
        return super.fitSystemWindows(insets);
    }
}
