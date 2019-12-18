package com.hyd.htalker.factory.presenter.friend;

import com.hyd.common.factory.presenter.BaseContract;
import com.hyd.htalker.factory.model.card.FriendCircleCard;

import java.util.List;

/**
 * 发布朋友圈
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class ReleaseFriendCircleContract {

    public interface Presenter extends BaseContract.Presenter {
        void release(String content, List<String> imgs);
    }

    //用户界面
    public interface View extends BaseContract.View<Presenter> {
        void onReleaseDone(FriendCircleCard friendCircleCard);
    }
}
