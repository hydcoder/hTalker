package com.hyd.htalker.factory.presenter.user;

import com.hyd.common.factory.presenter.BaseContract;

/**
 * 更新用户信息的基本的契约
 * Created by hydCoder on 2019/10/29.
 * 以梦为马，明日天涯。
 */
public interface UpdateInfoContract {

    interface Presenter extends BaseContract.Presenter {
        // 更新
        void update(String photoFilePath, String desc, boolean isMan);
    }

    interface View extends BaseContract.View<Presenter> {
        // 回调成功
        void updateSucceed();
    }
}
