package com.hyd.htalker.factory.data.group;

import com.hyd.htalker.factory.model.card.GroupCard;
import com.hyd.htalker.factory.model.card.GroupMemberCard;

/**
 * 群中心的接口定义
 * Created by hydCoder on 2019/11/7.
 * 以梦为马，明日天涯。
 */
public interface GroupCenter {
    // 群卡片的处理
    void dispatch(GroupCard... cards);

    // 群成员的处理
    void dispatch(GroupMemberCard... cards);
}
