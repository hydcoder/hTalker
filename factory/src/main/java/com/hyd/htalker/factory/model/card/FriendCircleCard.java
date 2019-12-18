package com.hyd.htalker.factory.model.card;

import java.util.Date;

/**
 * 朋友圈的卡片
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class FriendCircleCard {

    private String id;
    private String title;
    private String content;
    private String head;
    private String imgs;
    private String releaseId;

    private Date createAt;// 发布时间

    private int commentSize;//评论数量

    //是否点赞
    private boolean isFabulous = false;

    //点赞数量
    private int fabulousSize = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public int getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(int commentSize) {
        this.commentSize = commentSize;
    }

    public boolean isFabulous() {
        return isFabulous;
    }

    public void setFabulous(boolean fabulous) {
        isFabulous = fabulous;
    }

    public int getFabulousSize() {
        return fabulousSize;
    }

    public void setFabulousSize(int fabulousSize) {
        this.fabulousSize = fabulousSize;
    }

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
