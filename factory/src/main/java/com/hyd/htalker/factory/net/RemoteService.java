package com.hyd.htalker.factory.net;

import com.hyd.htalker.factory.model.api.RspModel;
import com.hyd.htalker.factory.model.api.account.AccountRspModel;
import com.hyd.htalker.factory.model.api.account.LoginModel;
import com.hyd.htalker.factory.model.api.account.RegisterModel;
import com.hyd.htalker.factory.model.api.friend.CommentModel;
import com.hyd.htalker.factory.model.api.friend.ReleaseFriendCircleModel;
import com.hyd.htalker.factory.model.api.group.GroupCreateModel;
import com.hyd.htalker.factory.model.api.group.GroupMemberAddModel;
import com.hyd.htalker.factory.model.api.message.MsgCreateModel;
import com.hyd.htalker.factory.model.api.user.UserUpdateModel;
import com.hyd.htalker.factory.model.card.FriendCircleCard;
import com.hyd.htalker.factory.model.card.GroupCard;
import com.hyd.htalker.factory.model.card.GroupMemberCard;
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

    // 拉取群信息
    @GET("group/{groupId}")
    Call<RspModel<GroupCard>> findGroup(@Path("groupId") String groupId);

    // 搜索群
    @GET("group/search/{name}")
    Call<RspModel<List<GroupCard>>> searchGroup(@Path(value = "name", encoded = true) String name);

    // 我的群列表
    @GET("group/list/{date}")
    Call<RspModel<List<GroupCard>>> groupList(@Path(value = "date", encoded = true) String date);

    // 我的群成员列表
    @GET("group/{groupId}/members")
    Call<RspModel<List<GroupMemberCard>>> groupMembers(@Path("groupId") String groupId);

    // 群添加成员
    @POST("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMemberAdd(@Path("groupId") String groupId, @Body GroupMemberAddModel model);

    //获取朋友圈的列表信息
    @GET("friend/list")
    Call<RspModel<List<FriendCircleCard>>> friendCircle();

    //发布朋友圈
    @POST("friend")
    Call<RspModel<FriendCircleCard>> release(@Body ReleaseFriendCircleModel model);

    //点赞
    @POST("friend/fabulous/{friendCircleId}")
    Call<RspModel> fabulous(@Path(value = "friendCircleId") String friendCircleId);

    //评论
    @POST("friend/comment")
    Call<RspModel> comment(@Body CommentModel model);

}

