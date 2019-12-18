package com.hyd.htalker.factory.presenter.friend;

import android.text.TextUtils;

import androidx.annotation.StringRes;

import com.hyd.common.factory.data.DataSource;
import com.hyd.common.factory.presenter.BasePresenter;
import com.hyd.htalker.factory.data.helper.FriendCircleHelper;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * 评论的逻辑
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class FabulousPresenter extends BasePresenter<FabulousContract.FabulousView>
        implements FabulousContract.Presenter, DataSource.Callback {

    public FabulousPresenter(FabulousContract.FabulousView view) {
        super(view);
    }

    @Override
    public void fabulous(String friendCircleId) {
        if (!TextUtils.isEmpty(friendCircleId)) {
            //点赞
            FriendCircleHelper.fabulous(friendCircleId, this);
        }
    }


    @Override
    public void onDataLoaded(Object user) {
        final FabulousContract.FabulousView view = getView();
        if (view == null) return;
        //强制执行在主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.onFabulousDone();
            }
        });
    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        final FabulousContract.FabulousView view = getView();
        if (view == null) return;
        //强制执行在主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
