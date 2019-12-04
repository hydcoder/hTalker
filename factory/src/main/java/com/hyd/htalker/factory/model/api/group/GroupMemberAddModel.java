package com.hyd.htalker.factory.model.api.group;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hydCoder on 2019/12/2.
 * 以梦为马，明日天涯。
 */
public class GroupMemberAddModel {

    private Set<String> users = new HashSet<>();

    public GroupMemberAddModel() {
    }

    public GroupMemberAddModel(Set<String> users) {
        this.users = users;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
