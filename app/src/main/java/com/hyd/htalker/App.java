package com.hyd.htalker;

import android.content.Context;

import com.hyd.common.app.BaseApplication;
import com.hyd.htalker.activities.AccountActivity;
import com.hyd.htalker.factory.Factory;
import com.igexin.sdk.PushManager;

/**
 * Created by hydCoder on 2019/10/28.
 * 以梦为马，明日天涯。
 */
public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        // 调用Factory进行初始化
        Factory.setup();
        // 推送进行初始化
        PushManager.getInstance().initialize(this);
    }

    @Override
    protected void showAccountActivity(Context context) {
        //登录界面的显示
        AccountActivity.show(context);
    }
}
