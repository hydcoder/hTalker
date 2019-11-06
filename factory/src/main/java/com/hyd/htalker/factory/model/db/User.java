package com.hyd.htalker.factory.model.db;

import com.hyd.common.factory.model.Author;
import com.hyd.htalker.factory.model.card.UserCard;
import com.hyd.htalker.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;
import java.util.Objects;

/**
 * Created by hydCoder on 2019/10/29.
 * 以梦为马，明日天涯。
 */
@Table(database = AppDatabase.class)
public class User extends BaseModel implements Author, DiffUiDataCallback.UiDataDiff<User> {
    public static final int SEX_MAN = 1;
    public static final int SEX_WOMAN = 2;

    // 主键
    @PrimaryKey
    private String id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String portrait;
    @Column
    private String desc;
    @Column
    private int sex = 0;

    // 我对某人的备注信息，也应该写入到数据库中
    @Column
    private String alias;

    // 用户关注人的数量
    @Column
    private int follows;

    // 用户粉丝的数量
    @Column
    private int following;

    // 我与当前User的关系状态，是否已经关注了这个人
    @Column
    private boolean isFollow;

    // 时间字段
    @Column
    private Date modifyAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", portrait='" + portrait + '\'' +
                ", desc='" + desc + '\'' +
                ", sex=" + sex +
                ", alias='" + alias + '\'' +
                ", follows=" + follows +
                ", following=" + following +
                ", isFollow=" + isFollow +
                ", modifyAt=" + modifyAt +
                '}';
    }

    // 缓存一个对应的UserCard, 不能被GSON框架解析使用
    private transient UserCard userCard;

    public UserCard build() {
        if (userCard == null) {
            UserCard userCard = new UserCard();
            userCard.setId(id);
            userCard.setName(name);
            userCard.setPortrait(portrait);
            userCard.setPhone(phone);
            userCard.setDesc(desc);
            userCard.setSex(sex);
            userCard.setFollow(isFollow);
            userCard.setFollows(follows);
            userCard.setFollowing(following);
            userCard.setModifyAt(modifyAt);
            this.userCard = userCard;
        }
        return userCard;
    }

    @Override
    public boolean isSame(User old) {
        return old == this || Objects.equals(id, old.id);
    }

    @Override
    public boolean isUiContentSame(User old) {
        return old == this || (Objects.equals(name, old.name)
                && Objects.equals(portrait, old.portrait)
                && Objects.equals(sex, old.sex)
                && Objects.equals(isFollow, old.isFollow));
    }
}
