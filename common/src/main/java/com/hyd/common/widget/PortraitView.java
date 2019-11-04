package com.hyd.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;
import com.hyd.common.factory.model.Author;
import com.hyd.htalker.common.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 头像控件
 * Created by hydCoder on 2019/10/25.
 * 以梦为马，明日天涯。
 */
public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setUp(RequestManager manager, Author author) {
        if (author != null) {
            setUp(manager, author.getPortrait());
        }
    }

    public void setUp(RequestManager manager, String url) {
        setUp(manager, R.drawable.default_portrait, url);
    }

    public void setUp(RequestManager manager, int resourceId, String url) {
        if (url == null) {
            url = "";
        }
        manager.load(url)
                .placeholder(resourceId)
                .centerCrop()
                .dontAnimate()    // CircleImageView空间中不能使用渐变动画，会导致显示延迟
                .into(this);
    }
}
