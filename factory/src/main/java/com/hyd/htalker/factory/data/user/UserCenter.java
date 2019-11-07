package com.hyd.htalker.factory.data.user;

import com.hyd.htalker.factory.model.card.UserCard;

/**
 * 用户中心的基本定义
 * Created by hydCoder on 2019/11/7.
 * 以梦为马，明日天涯。
 */
public interface UserCenter {
    // 分发处理一堆用户卡片的信息，并更新到数据库
    void dispatch(UserCard... cards);
}

