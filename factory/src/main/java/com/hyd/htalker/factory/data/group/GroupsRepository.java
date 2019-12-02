package com.hyd.htalker.factory.data.group;

import android.text.TextUtils;

import com.hyd.htalker.factory.data.BaseDbRepository;
import com.hyd.htalker.factory.data.helper.GroupHelper;
import com.hyd.htalker.factory.model.db.Group;
import com.hyd.htalker.factory.model.db.Group_Table;
import com.hyd.htalker.factory.model.db.MemberUserModel;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * 我的群组的数据仓库，是对GroupsDataSource的实现
 * Created by hydCoder on 2019/12/2.
 * 以梦为马，明日天涯。
 */
public class GroupsRepository extends BaseDbRepository<Group> implements GroupsDataSource {

    @Override
    public void load(SucceedCallback<List<Group>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Group.class)
                .orderBy(Group_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Group group) {
        // 一个群的信息，只有两种可能出现在本地数据库
        // 一个是被别人加入群，第二是直接自己建立一个群
        // 但是，无论什么情况，那到的都只是群的信息，没有群成员的信息
        // 所以需要进行成员信息初始化操作
        if (group.getGroupMemberCount() > 0) {
            // 已经初始化了群成员的信息
            group.holder = buildGroupHolder(group);
        } else {
            // 待初始化群成员信息
            group.holder = null;
            GroupHelper.refreshGroupMember(group);
        }
        // 所有的群我都需要关注显示
        return true;
    }

    // 初始化界面显示的成员信息
    private String buildGroupHolder(Group group) {
        List<MemberUserModel> userModels = group.getLatelyGroupMembers();
        if (userModels == null || userModels.isEmpty()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (MemberUserModel userModel : userModels) {
            builder.append(TextUtils.isEmpty(userModel.alias) ? userModel.name : userModel.alias);
            builder.append(", ");
        }

        builder.delete(builder.lastIndexOf(", "), builder.length());
        return builder.toString();
    }
}
