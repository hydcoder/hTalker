package com.hyd.htalker.factory.model.api.group;

import java.util.HashSet;
import java.util.Set;

/**
 * 创建群的model
 * Created by hydCoder on 2019/11/29.
 * 以梦为马，明日天涯。
 */
public class GroupCreateModel {
    private String name;
    private String desc;
    private String picture;
    private Set<String> users = new HashSet<>();

    public GroupCreateModel() {
    }

    public GroupCreateModel(String name, String desc, String picture, Set<String> users) {
        this.name = name;
        this.desc = desc;
        this.picture = picture;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
