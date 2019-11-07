package com.hyd.htalker.factory.data.user;

import com.hyd.common.factory.data.DataSource;
import com.hyd.htalker.factory.data.BaseDbRepository;
import com.hyd.htalker.factory.model.db.User;
import com.hyd.htalker.factory.model.db.User_Table;
import com.hyd.htalker.factory.persistence.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * 联系人仓库
 * Created by hydCoder on 2019/11/7.
 * 以梦为马，明日天涯。
 */
public class ContactRepository extends BaseDbRepository<User> implements ContactDataSource {
    @Override
    public void load(DataSource.SucceedCallback<List<User>> callback) {
        super.load(callback);

        // 加载本地数据库数据
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equals(Account.getUserId());
    }
}
