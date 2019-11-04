package com.hyd.htalker.factory.data.helper;

import com.hyd.common.factory.data.DataSource;
import com.hyd.htalker.factory.Factory;
import com.hyd.htalker.factory.R;
import com.hyd.htalker.factory.model.api.RspModel;
import com.hyd.htalker.factory.model.api.user.UserUpdateModel;
import com.hyd.htalker.factory.model.card.UserCard;
import com.hyd.htalker.factory.model.db.User;
import com.hyd.htalker.factory.net.Network;
import com.hyd.htalker.factory.net.RemoteService;
import com.hyd.htalker.factory.presenter.contact.FollowPresenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hydCoder on 2019/10/29.
 * 以梦为马，明日天涯。
 */
public class UserHelper {

    // 更新用户信息的操作，异步的
    public static void update(UserUpdateModel model, final DataSource.Callback<UserCard> callback) {
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.remote();
        // 得到一个Call
        Call<RspModel<UserCard>> call = service.userUpdate(model);
        // 网络请求
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel != null && rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    // 数据库的存储操作，需要把UserCard转换为User
                    // 保存用户信息
                    User user = userCard.build();
                    user.save();
                    // 返回成功
                    callback.onDataLoaded(userCard);
                } else {
                    // 错误情况下进行错误分配
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    // 搜索用户
    public static Call searchUser(String name, final DataSource.Callback<List<UserCard>> callback) {
        RemoteService service = Network.remote();
        // 得到一个Call
        Call<RspModel<List<UserCard>>> call = service.searchUser(name);
        // 网络请求
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel != null && rspModel.success()) {
                    List<UserCard> userCards = rspModel.getResult();
                    callback.onDataLoaded(userCards);
                } else {
                    // 错误情况下进行错误分配
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        return call;
    }

    /**
     * 关注的网络请求
     * @param id 用户id
     * @param callback 回调
     */
    public static void follow(String id, final DataSource.Callback<UserCard> callback) {
        RemoteService service = Network.remote();
        // 得到一个Call
        Call<RspModel<UserCard>> call = service.followUser(id);

        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call,
                                   Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel != null && rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    // 保存到本地数据库
                    User user = userCard.build();
                    user.save();
                    // TODO 通知联系人列表刷新

                    // 返回数据
                    callback.onDataLoaded(userCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
}
