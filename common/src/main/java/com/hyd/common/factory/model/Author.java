package com.hyd.common.factory.model;

/**
 * 用户基础信息接口
 * Created by hydCoder on 2019/11/4.
 * 以梦为马，明日天涯。
 */
public interface Author {

    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    String getPortrait();

    void setPortrait(String portrait);
}
