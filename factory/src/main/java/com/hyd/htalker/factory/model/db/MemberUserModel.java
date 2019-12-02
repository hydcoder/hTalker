package com.hyd.htalker.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

/**
 * 群成员对应用户的简单信息临时表
 * Created by hydCoder on 2019/12/2.
 * 以梦为马，明日天涯。
 */
@QueryModel(database = AppDatabase.class)
public class MemberUserModel {
    @Column
    public String userId;   // 来源  User-id/Member-UserId
    @Column
    public String name;   // User-name
    @Column
    public String portrait; // User-portrait
    @Column
    public String alias;  // Member-alias
}
