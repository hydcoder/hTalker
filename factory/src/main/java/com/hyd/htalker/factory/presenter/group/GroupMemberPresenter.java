package com.hyd.htalker.factory.presenter.group;

import com.hyd.common.factory.presenter.BaseRecyclerPresenter;
import com.hyd.htalker.factory.Factory;
import com.hyd.htalker.factory.data.helper.GroupHelper;
import com.hyd.htalker.factory.model.db.MemberUserModel;

import java.util.List;

/**
 * Created by hydCoder on 2019/12/4.
 * 以梦为马，明日天涯。
 */
public class GroupMemberPresenter extends BaseRecyclerPresenter<MemberUserModel, GroupMemberContract.View>
implements GroupMemberContract.Presenter {

    public GroupMemberPresenter(GroupMemberContract.View view) {
        super(view);
    }

    @Override
    public void refresh() {
        // 显示loading
        start();

        Factory.runOnAsync(loader);
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {

            GroupMemberContract.View view = getView();
            if (view == null) {
                return;
            }
            // -1代表查询所有
            List<MemberUserModel> models = GroupHelper.getMemberUsers(view.getGroupId(), -1);

            refreshData(models);
        }
    };
}
