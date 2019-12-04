package com.hyd.htalker.factory.data.helper;

import com.hyd.common.factory.data.DataSource;
import com.hyd.htalker.factory.Factory;
import com.hyd.htalker.factory.R;
import com.hyd.htalker.factory.model.api.RspModel;
import com.hyd.htalker.factory.model.api.group.GroupCreateModel;
import com.hyd.htalker.factory.model.api.group.GroupMemberAddModel;
import com.hyd.htalker.factory.model.card.GroupCard;
import com.hyd.htalker.factory.model.card.GroupMemberCard;
import com.hyd.htalker.factory.model.db.Group;
import com.hyd.htalker.factory.model.db.GroupMember;
import com.hyd.htalker.factory.model.db.GroupMember_Table;
import com.hyd.htalker.factory.model.db.Group_Table;
import com.hyd.htalker.factory.model.db.MemberUserModel;
import com.hyd.htalker.factory.model.db.User;
import com.hyd.htalker.factory.model.db.User_Table;
import com.hyd.htalker.factory.net.Network;
import com.hyd.htalker.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 对群的一个简单的辅助工具类
 * Created by hydCoder on 2019/11/7.
 * 以梦为马，明日天涯。
 */
public class GroupHelper {

    // 查询群的信息，先本地，后网络
    public static Group find(String groupId) {
        Group group = findFromLocal(groupId);
        if (group == null) {
            group = findFromNet(groupId);
        }
        return group;
    }

    // 本地找群信息
    public static Group findFromLocal(String groupId) {
        return SQLite.select().from(Group.class).where(Group_Table.id.eq(groupId)).querySingle();
    }

    // 从网络查找群信息
    public static Group findFromNet(String groupId) {
        RemoteService remoteService = Network.remote();
        try {
            Response<RspModel<GroupCard>> response = remoteService.findGroup(groupId).execute();
            GroupCard card = response.body().getResult();
            if (card != null) {
                // 数据库的存储并通知
                Factory.getGroupCenter().dispatch(card);

                User user = UserHelper.search(card.getOwnerId());
                if (user != null) {
                    return card.build(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void create(GroupCreateModel model,
                              final DataSource.Callback<GroupCard> callback) {
        RemoteService service = Network.remote();
        service.createGroup(model).enqueue(new Callback<RspModel<GroupCard>>() {
            @Override
            public void onResponse(Call<RspModel<GroupCard>> call,
                                   Response<RspModel<GroupCard>> response) {
                RspModel<GroupCard> rspModel = response.body();
                if (rspModel != null && rspModel.success()) {
                    GroupCard groupCard = rspModel.getResult();
                    // 保存到本地数据库
                    // 唤起进行保存的操作
                    Factory.getGroupCenter().dispatch(groupCard);
                    // 返回数据
                    callback.onDataLoaded(groupCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<GroupCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    // 搜索群
    public static Call searchGroup(String name,
                                   final DataSource.Callback<List<GroupCard>> callback) {
        RemoteService service = Network.remote();
        // 得到一个Call
        Call<RspModel<List<GroupCard>>> call = service.searchGroup(name);
        // 网络请求
        call.enqueue(new Callback<RspModel<List<GroupCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupCard>>> call,
                                   Response<RspModel<List<GroupCard>>> response) {
                RspModel<List<GroupCard>> rspModel = response.body();
                if (rspModel != null && rspModel.success()) {
                    List<GroupCard> groupCards = rspModel.getResult();
                    callback.onDataLoaded(groupCards);
                } else {
                    // 错误情况下进行错误分配
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        return call;
    }

    // 刷新我的群组列表
    public static void refreshGroups() {
        RemoteService service = Network.remote();
        service.groupList("").enqueue(new Callback<RspModel<List<GroupCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupCard>>> call,
                                   Response<RspModel<List<GroupCard>>> response) {
                RspModel<List<GroupCard>> rspModel = response.body();
                if (rspModel != null && rspModel.success()) {
                    List<GroupCard> groupCards = rspModel.getResult();
                    if (groupCards != null && !groupCards.isEmpty())
                        Factory.getGroupCenter().dispatch(groupCards.toArray(new GroupCard[0]));
                } else {
                    // 错误情况下进行错误分配
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupCard>>> call, Throwable t) {
            }
        });
    }

    // 获取一个群的成员数量
    public static long getMemberCount(String groupId) {
        return SQLite.selectCountOf().from(GroupMember.class).where(GroupMember_Table.group_id.eq(groupId)).count();
    }

    // 从网络去刷新一个群的成员信息
    public static void refreshGroupMember(Group group) {
        RemoteService service = Network.remote();
        service.groupMembers(group.getId()).enqueue(new Callback<RspModel<List<GroupMemberCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupMemberCard>>> call,
                                   Response<RspModel<List<GroupMemberCard>>> response) {
                RspModel<List<GroupMemberCard>> rspModel = response.body();
                if (rspModel != null && rspModel.success()) {
                    List<GroupMemberCard> memberCards = rspModel.getResult();
                    if (memberCards != null && !memberCards.isEmpty())
                        Factory.getGroupCenter().dispatch(memberCards.toArray(new GroupMemberCard[0]));
                } else {
                    // 错误情况下进行错误分配
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupMemberCard>>> call, Throwable t) {
            }
        });
    }

    // 关联查询一个用户和群成员的表，返回一个MemberUserModel的集合
    public static List<MemberUserModel> getMemberUsers(String groupId, int size) {
        return SQLite.select(GroupMember_Table.alias.withTable().as("alias"),
                User_Table.id.withTable().as("userId"), User_Table.name.withTable().as("name"),
                User_Table.portrait.withTable().as("portrait")).from(GroupMember.class).join(User.class, Join.JoinType.INNER).on(GroupMember_Table.user_id.withTable().eq(User_Table.id.withTable())).where(GroupMember_Table.group_id.withTable().eq(groupId)).orderBy(GroupMember_Table.user_id, true).limit(size).queryCustomList(MemberUserModel.class);
    }

    // 网络请求进行成员添加
    public static void addMembers(String groupId, GroupMemberAddModel model,
                                  final DataSource.Callback<List<GroupMemberCard>> callback) {
        RemoteService service = Network.remote();
        service.groupMemberAdd(groupId, model).enqueue(new Callback<RspModel<List<GroupMemberCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupMemberCard>>> call,
                                   Response<RspModel<List<GroupMemberCard>>> response) {
                RspModel<List<GroupMemberCard>> rspModel = response.body();
                if (rspModel != null && rspModel.success()) {
                    List<GroupMemberCard> memberCards = rspModel.getResult();
                    if (memberCards != null && memberCards.size() > 0) {
                        // 进行调度显示
                        Factory.getGroupCenter().dispatch(memberCards.toArray(new GroupMemberCard[0]));
                        callback.onDataLoaded(memberCards);
                    }
                } else {
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupMemberCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
}
