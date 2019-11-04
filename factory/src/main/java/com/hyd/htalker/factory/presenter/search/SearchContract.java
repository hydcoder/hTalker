package com.hyd.htalker.factory.presenter.search;

import com.hyd.common.factory.presenter.BaseContract;
import com.hyd.htalker.factory.model.card.GroupCard;
import com.hyd.htalker.factory.model.card.UserCard;

import java.util.List;

/**
 * Created by hydCoder on 2019/11/4.
 * 以梦为马，明日天涯。
 */
public interface SearchContract {

    interface Presenter extends BaseContract.Presenter {
        // 搜索内容
        void search(String content);
    }

    // 搜索人的View
    interface UserView extends BaseContract.View<Presenter> {
        void onSearchDone(List<UserCard> userCards);
    }

    // 搜索群的View
    interface GroupView extends BaseContract.View<Presenter> {
        void onSearchDone(List<GroupCard> groupCards);
    }
}
