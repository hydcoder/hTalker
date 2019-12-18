package com.hyd.htalker.factory.model.api.friend;

/**
 * 评论的model
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class CommentModel {

    private String friendCircleId;//朋友圈Id

    private String content;//评论内容


    public CommentModel( String friendCircleId, String content) {
        this.friendCircleId = friendCircleId;
        this.content = content;
    }

    public String getFriendCircleId() {
        return friendCircleId;
    }

    public void setFriendCircleId(String friendCircleId) {
        this.friendCircleId = friendCircleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "friendCircleId='" + friendCircleId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
