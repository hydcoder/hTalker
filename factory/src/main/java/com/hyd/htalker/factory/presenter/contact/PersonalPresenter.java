package com.hyd.htalker.factory.presenter.contact;

import com.hyd.common.factory.presenter.BasePresenter;
import com.hyd.htalker.factory.Factory;
import com.hyd.htalker.factory.data.helper.UserHelper;
import com.hyd.htalker.factory.model.db.User;
import com.hyd.htalker.factory.persistence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by hydCoder on 2019/11/6.
 * 以梦为马，明日天涯。
 */
public class PersonalPresenter extends BasePresenter<PersonalContract.View> implements PersonalContract.Presenter {

    private String userId;
    private User user;

    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();

        userId = getView().getUserId();
        // 个人界面用户数据优先从网络拉取
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view = getView();
                if (view != null) {
                    User user = UserHelper.searchFirstOfNet(userId);
                    onLoaded(view, user);
                }
            }
        });
    }

    private void onLoaded(final PersonalContract.View view, final User user) {
        this.user = user;
        // 是否就是我自己
        boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        // 是否已经关注
        final boolean isFollow = isSelf || user.isFollow();

        final boolean allowSayFollow = isFollow && !isSelf;

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                 view.onLoadDone(user);
                 view.allowSayHello(allowSayFollow);
                 view.setFollowStatus(isFollow);
            }
        });

    }

    @Override
    public User getUserPersonal() {
        return user;
    }
}
