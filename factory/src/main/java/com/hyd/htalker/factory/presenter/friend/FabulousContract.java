package com.hyd.htalker.factory.presenter.friend;

import com.hyd.common.factory.presenter.BaseContract;

/**
 * 点赞评论契约
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class FabulousContract {

    public interface Presenter extends BaseContract.Presenter {
        //点赞请求
        void fabulous(String friendCircleId);
    }

    public interface FabulousView extends BaseContract.View<Presenter> {
        //点赞成功
        void onFabulousDone();
    }
}
