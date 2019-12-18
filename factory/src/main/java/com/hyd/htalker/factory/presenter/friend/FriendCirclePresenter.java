package com.hyd.htalker.factory.presenter.friend;

import androidx.annotation.StringRes;

import com.hyd.common.factory.data.DataSource;
import com.hyd.common.factory.presenter.BasePresenter;
import com.hyd.htalker.factory.data.helper.FriendCircleHelper;
import com.hyd.htalker.factory.model.card.FriendCircleCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/**
 * 朋友圈的逻辑处理
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class FriendCirclePresenter extends BasePresenter<FriendCircleContract.View> implements FriendCircleContract.Presenter, DataSource.Callback<List<FriendCircleCard>> {

    private Call mCall;

    public FriendCirclePresenter(FriendCircleContract.View view) {
        super(view);
    }

    @Override
    public void friendCircle() {
        start();
        Call call = mCall;
        if (call != null && !call.isCanceled()) {
            //如果有上一次的请求，并且没有取消，
            //则调用取消请求操作
            call.cancel();
        }
        mCall = FriendCircleHelper.friendCircle(this);
    }

    @Override
    public void onDataLoaded(final List<FriendCircleCard> friendCircleCards) {
        final FriendCircleContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFriendCircleDone(friendCircleCards);
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        final FriendCircleContract.View view = getView();
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
