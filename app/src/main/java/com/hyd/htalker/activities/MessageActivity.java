package com.hyd.htalker.activities;

import android.content.Context;

import com.hyd.common.app.BaseActivity;
import com.hyd.common.factory.model.Author;
import com.hyd.htalker.R;

public class MessageActivity extends BaseActivity {

    /**
     * 显示人的聊天界面
     * @param context 上下文
     * @param author 人的信息
     */
    public static void show(Context context, Author author) {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

}
