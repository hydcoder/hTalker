package com.hyd.htalker.factory.model.db;

import com.hyd.common.factory.model.Author;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

/**
 * 用户基础信息的model，可以和数据库进行查询
 * Created by hydCoder on 2019/11/29.
 * 以梦为马，明日天涯。
 */
@QueryModel(database = AppDatabase.class)
public class UserSimpleModel implements Author {

    @Column
    public String id;
    @Column
    public String name;
    @Column
    public String portrait;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPortrait() {
        return portrait;
    }

    @Override
    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
