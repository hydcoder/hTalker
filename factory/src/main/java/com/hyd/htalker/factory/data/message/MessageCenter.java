package com.hyd.htalker.factory.data.message;

import com.hyd.htalker.factory.model.card.MessageCard;

/**
 * 消息中心，进行消息卡片的消费
 * Created by hydCoder on 2019/11/7.
 * 以梦为马，明日天涯。
 */
public interface MessageCenter {
    void dispatch(MessageCard... cards);
}
