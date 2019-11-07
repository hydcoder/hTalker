package com.hyd.htalker.factory.data.helper;

import com.hyd.htalker.factory.model.db.Session;
import com.hyd.htalker.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * 会话辅助工具类
 * Created by hydCoder on 2019/11/7.
 * 以梦为马，明日天涯。
 */
public class SessionHelper {
    // 从本地查询Session
    public static Session findFromLocal(String id) {
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
