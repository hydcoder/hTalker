package com.hyd.htalker.factory.presenter.group;

import com.hyd.common.factory.presenter.BaseContract;

/**
 * 群成员添加的契约
 * Created by hydCoder on 2019/12/4.
 * 以梦为马，明日天涯。
 */
public interface GroupMemberAddContract {
    interface Presenter extends BaseContract.Presenter {
        // 提交成员
        void submit();

        // 更改一个Model的选中状态
        void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected);
    }

    // 界面
    interface View extends BaseContract.RecyclerView<Presenter, GroupCreateContract.ViewModel> {
        // 添加群成员成功
        void onAddedSucceed();

        // 获取群的Id
        String getGroupId();
    }
}
