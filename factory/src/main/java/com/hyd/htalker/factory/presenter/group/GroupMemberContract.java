package com.hyd.htalker.factory.presenter.group;

import com.hyd.common.factory.presenter.BaseContract;
import com.hyd.htalker.factory.model.db.MemberUserModel;

/**
 * 群成员界面的契约
 * Created by hydCoder on 2019/12/4.
 * 以梦为马，明日天涯。
 */
public interface GroupMemberContract {

    interface Presenter extends BaseContract.Presenter {

        void refresh();
    }

    interface View extends BaseContract.RecyclerView<Presenter, MemberUserModel> {

        String getGroupId();
    }
}
