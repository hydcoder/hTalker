package com.hyd.htalker.factory.model.api.message;

import com.hyd.htalker.factory.model.card.MessageCard;
import com.hyd.htalker.factory.model.db.Message;
import com.hyd.htalker.factory.persistence.Account;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hydCoder on 2019/11/21.
 * 以梦为马，明日天涯。
 */
public class MsgCreateModel {
    // id从客户端生成，一个UUID
    private String id;

    private String content;

    private String attach;

    // 消息类型
    private int type = Message.TYPE_STR;

    // 接收者，允许为空
    private String receiverId;

    // 接收者类型，群或人
    private int receiverType = Message.RECEIVER_TYPE_NONE;

    private MsgCreateModel() {
        // 随机生成一个UUID
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAttach() {
        return attach;
    }

    public int getType() {
        return type;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public int getReceiverType() {
        return receiverType;
    }

    // 当需要发送一个文件的时候，content需要刷新

    private MessageCard card;

    // 返回一个MessageCard
    public MessageCard buildCard() {
        if (card == null) {
            card = new MessageCard();
            card.setId(id);
            card.setContent(content);
            card.setAttach(attach);
            card.setType(type);
            card.setSenderId(Account.getUserId());

            // 如果是群
            if (receiverType == Message.RECEIVER_TYPE_GROUP) {
                card.setGroupId(receiverId);
            } else {
                card.setReceiverId(receiverId);
            }
            // 通过当前model建立的card就是一个初步状态的消息
            card.setStatus(Message.STATUS_CREATED);
            card.setCreateAt(new Date());
        }
        return card;
    }

    public static MsgCreateModel buildWithMessage(Message message) {
        MsgCreateModel model = new MsgCreateModel();
        model.id = message.getId();
        model.content = message.getContent();
        model.type = message.getType();
        model.attach = message.getAttach();

        // 如果接收者不为null， 则是给人发消息
        if (message.getReceiver() != null) {
            model.receiverId = message.getReceiver().getId();
            model.receiverType = Message.RECEIVER_TYPE_NONE;
        } else {
            model.receiverId = message.getGroup().getId();
            model.receiverType = Message.RECEIVER_TYPE_GROUP;
        }
        return model;
    }

    public void refreshByCard() {
        if (card == null) {
            return;
        }
        // 刷新内容和附件信息
        this.content = card.getContent();
        this.attach = card.getAttach();
    }

    /**
     * 建造者模式，快速的建立一个发送model
     */
    public static class Builder {
        private MsgCreateModel model;

        public Builder() {
            model = new MsgCreateModel();
        }

        // 设置接收者
        public Builder receiver(String receiverId, int receiverType) {
            model.receiverId = receiverId;
            model.receiverType = receiverType;
            return this;
        }

        // 设置内容
        public Builder content(String content, int type) {
            model.content = content;
            model.type = type;
            return this;
        }

        // 设置附件
        public Builder attach(String attach) {
            model.attach = attach;
            return this;
        }

        public MsgCreateModel build() {
            return model;
        }
    }

}
