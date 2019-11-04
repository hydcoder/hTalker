package com.hyd.htalker.factory.presenter.contact;

import com.hyd.common.factory.presenter.BaseContract;
import com.hyd.htalker.factory.model.card.UserCard;

/**
 * 关注的契约类
 * Created by hydCoder on 2019/11/4.
 * 以梦为马，明日天涯。
 */
public class FollowContract {

    public interface Presenter extends BaseContract.Presenter {
        // 关注某个用户
        void follow(String id);
    }

    public interface View extends BaseContract.View<Presenter> {
        // 关注成功返回被关注着的用户信息
        void onFollowSucceed(UserCard userCard);
    }
}
