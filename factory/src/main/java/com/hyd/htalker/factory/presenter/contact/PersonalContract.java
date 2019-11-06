package com.hyd.htalker.factory.presenter.contact;

import com.hyd.common.factory.presenter.BaseContract;
import com.hyd.htalker.factory.model.db.User;

/**
 * Created by hydCoder on 2019/11/6.
 * 以梦为马，明日天涯。
 */
public interface PersonalContract {

    interface Presenter extends BaseContract.Presenter {
        // 获取用户信息
        User getUserPersonal();
    }

    interface View extends BaseContract.View<Presenter> {

        // 得到userId
        String getUserId();

        // 加载数据完成
        void onLoadDone(User user);

        // 是否发起聊天
        void allowSayHello(boolean isAllow);

        // 设置关注状态
        void setFollowStatus(boolean isFollow);
    }
 }
