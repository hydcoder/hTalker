package com.hyd.htalker.factory.presenter.contact;

import com.hyd.common.factory.data.DataSource;
import com.hyd.common.factory.presenter.BasePresenter;
import com.hyd.htalker.factory.data.helper.UserHelper;
import com.hyd.htalker.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * 关注的逻辑实现
 * Created by hydCoder on 2019/11/4.
 * 以梦为马，明日天涯。
 */
public class FollowPresenter extends BasePresenter<FollowContract.View> implements FollowContract.Presenter, DataSource.Callback<UserCard> {

    public FollowPresenter(FollowContract.View view) {
        super(view);
    }

    @Override
    public void follow(String id) {
        start();

        UserHelper.follow(id, this);
    }

    @Override
    public void onDataLoaded(final UserCard userCard) {
        final FollowContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSucceed(userCard);
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final FollowContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
