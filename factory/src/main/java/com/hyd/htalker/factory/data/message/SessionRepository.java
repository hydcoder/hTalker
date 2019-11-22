package com.hyd.htalker.factory.data.message;

import androidx.annotation.NonNull;

import com.hyd.htalker.factory.data.BaseDbRepository;
import com.hyd.htalker.factory.model.db.Session;
import com.hyd.htalker.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 * 最近聊天列表仓库，是对SessionDataSource的实现
 *
 * Created by hydCoder on 2019/11/22.
 * 以梦为马，明日天涯。
 */
public class SessionRepository extends BaseDbRepository<Session> implements SessionDataSource {

    @Override
    public void load(SucceedCallback<List<Session>> callback) {
        super.load(callback);

        SQLite.select().from(Session.class)
                .orderBy(Session_Table.modifyAt, false)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
        // eg：时间倒序  10,9,8
        // 由于是一条条插入的，复写了insert后会导致变成 8,9,10
        // 所有需要复写数据库查询回调的方法，进行一次反转
    }

    @Override
    protected boolean isRequired(Session session) {
        // 所有的会话都需要展示，不需要过滤
        return true;
    }

    @Override
    protected void insert(Session session) {
        // 复写方法，让新的数据加到头部
        dataList.addFirst(session);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {
        // 复写数据库查询回调的方法，进行一次反转
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
