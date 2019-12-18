package com.hyd.htalker.factory.presenter.friend;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.StringRes;

import com.hyd.common.factory.data.DataSource;
import com.hyd.common.factory.presenter.BasePresenter;
import com.hyd.htalker.factory.Factory;
import com.hyd.htalker.factory.R;
import com.hyd.htalker.factory.data.helper.FriendCircleHelper;
import com.hyd.htalker.factory.model.api.friend.ReleaseFriendCircleModel;
import com.hyd.htalker.factory.model.card.FriendCircleCard;
import com.hyd.htalker.factory.net.UploadHelper;
import com.hyd.htalker.factory.persistence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

/**
 * 发布朋友圈的逻辑
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class ReleaseFriendCirclePresenter extends BasePresenter<ReleaseFriendCircleContract.View> implements ReleaseFriendCircleContract.Presenter, DataSource.Callback<FriendCircleCard> {

    private static final String TAG = "ReleaseFriendCirclePres";

    public ReleaseFriendCirclePresenter(ReleaseFriendCircleContract.View view) {
        super(view);
    }

    @Override
    public void release(final String content, final List<String> paths) {
        start();
        final ReleaseFriendCircleContract.View view = getView();
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(content.trim())) {
            view.showError(R.string.data_rsp_error_release_content_null);
        } else if (paths == null || paths.size() == 0) {
            view.showError(R.string.data_rsp_error_release_image_null);
        } else {
            //上传图片
            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    String urls = uploadPicture(paths);
                    if (TextUtils.isEmpty(urls))
                        return;
                    ReleaseFriendCircleModel model = new ReleaseFriendCircleModel(content,
                            Account.getUserId(), urls);
                    Log.e(TAG, model.toString());
                    FriendCircleHelper.release(model, ReleaseFriendCirclePresenter.this);
                }
            });

        }
    }

    //同步上传
    private String uploadPicture(List<String> path) {
        // 一种情况 当他只有一个的时候也就是只有ADD的时候不操作
        StringBuilder img = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            String url = UploadHelper.uploadPortrait(path.get(i));
            img.append(url);
            if (i != path.size() - 1) {
                img.append(",");
            }
        }
        if (TextUtils.isEmpty(img.toString())) {
            //切换到UI 线程，提示信息
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    ReleaseFriendCircleContract.View view = getView();
                    if (view != null) {
                        view.showError(R.string.data_upload_error);
                    }
                }
            });
        }
        return img.toString();
    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {

        final ReleaseFriendCircleContract.View view = getView();
        if (view == null) return;
        //强制执行在主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }

    @Override
    public void onDataLoaded(final FriendCircleCard friendCircleCard) {
        final ReleaseFriendCircleContract.View view = getView();
        if (view == null) return;
        //强制执行在主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.onReleaseDone(friendCircleCard);
            }
        });
    }
}
