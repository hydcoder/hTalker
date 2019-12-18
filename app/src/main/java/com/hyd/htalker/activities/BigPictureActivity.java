package com.hyd.htalker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hyd.common.app.ToolbarActivity;
import com.hyd.htalker.R;

import butterknife.BindView;

/**
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class BigPictureActivity extends ToolbarActivity {

    private final static String BIG_PICTURE_PIC = "BIG_PICTURE_PIC";
    private String url = "";

    @BindView(R.id.im_header)
    ImageView mHeader;

    public static void show(Activity activity, View view, String url) {
        Intent intent = new Intent(activity, BigPictureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BIG_PICTURE_PIC, url);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        url = bundle.getString(BIG_PICTURE_PIC);
        return super.initArgs(bundle);

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_big_picture;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        Glide.with(this).asBitmap().load(url).placeholder(R.drawable.default_portrait).into(mHeader);
    }
}
