package com.hyd.htalker.factory.data.helper;

import com.hyd.common.factory.data.DataSource;
import com.hyd.htalker.factory.Factory;
import com.hyd.htalker.factory.R;
import com.hyd.htalker.factory.model.api.RspModel;
import com.hyd.htalker.factory.model.api.group.GroupCreateModel;
import com.hyd.htalker.factory.model.card.GroupCard;
import com.hyd.htalker.factory.model.db.Group;
import com.hyd.htalker.factory.model.db.Group_Table;
import com.hyd.htalker.factory.model.db.User;
import com.hyd.htalker.factory.net.Network;
import com.hyd.htalker.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;

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
        return SQLite.select()
                .from(Group.class)
                .where(Group_Table.id.eq(groupId))
                .querySingle();
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

    public static void create(GroupCreateModel model, final DataSource.Callback<GroupCard> callback) {
        RemoteService service = Network.remote();
        service.createGroup(model).enqueue(new Callback<RspModel<GroupCard>>() {
            @Override
            public void onResponse(Call<RspModel<GroupCard>> call, Response<RspModel<GroupCard>> response) {
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
}
