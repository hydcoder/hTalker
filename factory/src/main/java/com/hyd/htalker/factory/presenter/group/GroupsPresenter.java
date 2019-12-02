package com.hyd.htalker.factory.presenter.group;

import androidx.recyclerview.widget.DiffUtil;

import com.hyd.htalker.factory.data.group.GroupsDataSource;
import com.hyd.htalker.factory.data.group.GroupsRepository;
import com.hyd.htalker.factory.data.helper.GroupHelper;
import com.hyd.htalker.factory.model.db.Group;
import com.hyd.htalker.factory.presenter.BaseSourcePresenter;
import com.hyd.htalker.factory.presenter.contact.GroupsContract;
import com.hyd.htalker.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * Created by hydCoder on 2019/12/2.
 * 以梦为马，明日天涯。
 */
public class GroupsPresenter extends BaseSourcePresenter<Group, Group,
        GroupsDataSource, GroupsContract.View> implements GroupsContract.Presenter {

    public GroupsPresenter(GroupsContract.View view) {
        super(new GroupsRepository(), view);
    }

    @Override
    public void start() {
        super.start();

        // 加载网络数据，以后可以优化到下拉刷新中
        // 只有用户在下拉刷新时才进行网络请求刷新数据
        GroupHelper.refreshGroups();
    }

    @Override
    public void onDataLoaded(List<Group> groups) {
        GroupsContract.View view = getView();
        if (view == null) {
            return;
        }

        List<Group> oldData = view.getRecyclerAdapter().getDataList();
        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(oldData, groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        refreshData(result, groups);
    }
}
