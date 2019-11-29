package com.hyd.htalker.factory.net;

import com.hyd.htalker.factory.model.api.RspModel;
import com.hyd.htalker.factory.model.api.account.AccountRspModel;
import com.hyd.htalker.factory.model.api.account.LoginModel;
import com.hyd.htalker.factory.model.api.account.RegisterModel;
import com.hyd.htalker.factory.model.api.group.GroupCreateModel;
import com.hyd.htalker.factory.model.api.message.MsgCreateModel;
import com.hyd.htalker.factory.model.api.user.UserUpdateModel;
import com.hyd.htalker.factory.model.card.GroupCard;
import com.hyd.htalker.factory.model.card.MessageCard;
import com.hyd.htalker.factory.model.card.UserCard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * 网络请求的所有的接口
 * Created by hydCoder on 2019/10/29.
 * 以梦为马，明日天涯。
 */
public interface RemoteService {
    /**
     * 注册接口
     *
     * @param model 传入的是RegisterModel
     * @return 返回的是RspModel<AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 登录接口
     *
     * @param model LoginModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 绑定设备Id
     *
     * @param pushId 设备Id
     * @return RspModel<AccountRspModel>
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    // 用户更新的接口
    @PUT("user")
    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);

    // 搜索用户的接口
    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> searchUser(@Path("name") String name);

    // 用户关注接口
    @PUT("user/follow/{userId}")
    Call<RspModel<UserCard>> followUser(@Path("userId") String userId);

    // 获取联系人列表
    @GET("user/contact")
    Call<RspModel<List<UserCard>>> userContacts();

    // 获取某个用户的信息
    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);

    // 发送消息
    @POST("msg")
    Call<RspModel<MessageCard>> msgPush(@Body MsgCreateModel model);

    // 创建群
    @POST("group")
    Call<RspModel<GroupCard>> createGroup(@Body GroupCreateModel model);

    // 查找群
    @GET("group/{groupId}")
    Call<RspModel<GroupCard>> findGroup(@Path("groupId") String groupId);
}
