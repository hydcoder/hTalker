package com.hyd.htalker.factory.presenter.friend;

import com.hyd.common.factory.presenter.BaseContract;
import com.hyd.htalker.factory.model.card.FriendCircleCard;

import java.util.List;

/**
 * 获取朋友圈信息
 * 朋友圈契约
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class FriendCircleContract {

    public interface Presenter extends BaseContract.Presenter {
        void friendCircle();
    }

    //用户界面
    public interface View extends BaseContract.View<Presenter> {
        void onFriendCircleDone(List<FriendCircleCard> friendCircles);
    }
}
