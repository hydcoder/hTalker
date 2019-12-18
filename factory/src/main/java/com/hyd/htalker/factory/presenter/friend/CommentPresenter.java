package com.hyd.htalker.factory.presenter.friend;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.StringRes;

import com.hyd.common.factory.data.DataSource;
import com.hyd.common.factory.presenter.BasePresenter;
import com.hyd.htalker.factory.R;
import com.hyd.htalker.factory.data.helper.FriendCircleHelper;
import com.hyd.htalker.factory.model.api.friend.CommentModel;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * 评论的逻辑
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class CommentPresenter extends BasePresenter<CommentContract.CommentView> implements CommentContract.Presenter, DataSource.Callback {

    public CommentPresenter(CommentContract.CommentView view) {
        super(view);
    }

    @Override
    public void comment(String friendCircleId, String content) {
        start();
        final CommentContract.CommentView view = getView();
        if (TextUtils.isEmpty(friendCircleId)) {
            view.showError(R.string.hint_comment_info);
        } else {
            //评论
            CommentModel commentModel = new CommentModel(friendCircleId, content);
            Log.e("commentModel", commentModel.toString());
            FriendCircleHelper.comment(commentModel, this);
        }
    }

    @Override
    public void onDataLoaded(Object user) {
        final CommentContract.CommentView view = getView();
        if (view == null) return;
        //强制执行在主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showLoading();
            }
        });
    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        final CommentContract.CommentView view = getView();
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
