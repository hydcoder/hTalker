package com.hyd.htalker.factory.model.card;

import java.util.Date;

/**
 * 评论内容的卡片
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class CommentCard {

    private String id;

    private String friendCircleId;

    private Date createAt;

    private String content;//评论的内容

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriendCircleId() {
        return friendCircleId;
    }

    public void setFriendCircleId(String friendCircleId) {
        this.friendCircleId = friendCircleId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
