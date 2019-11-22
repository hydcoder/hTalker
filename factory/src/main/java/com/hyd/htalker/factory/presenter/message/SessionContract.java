package com.hyd.htalker.factory.presenter.message;

import com.hyd.common.factory.presenter.BaseContract;
import com.hyd.htalker.factory.model.db.Session;

/**
 * Created by hydCoder on 2019/11/22.
 * 以梦为马，明日天涯。
 */
public interface SessionContract {
    // 什么都不需要额外定义，开始就是调用start即可
    interface Presenter extends BaseContract.Presenter {

    }

    interface View extends BaseContract.RecyclerView<Presenter, Session> {

    }
}
