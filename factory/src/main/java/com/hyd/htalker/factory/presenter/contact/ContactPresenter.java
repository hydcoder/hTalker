package com.hyd.htalker.factory.presenter.contact;

import androidx.annotation.NonNull;

import com.hyd.common.factory.presenter.BasePresenter;
import com.hyd.htalker.factory.model.db.User;
import com.hyd.htalker.factory.model.db.User_Table;
import com.hyd.htalker.factory.persistence.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.List;

/**
 * 联系人的presenter实现
 * Created by hydCoder on 2019/11/5.
 * 以梦为马，明日天涯。
 */
public class ContactPresenter extends BasePresenter<ContactContract.View> implements ContactContract.Presenter {

    public ContactPresenter(ContactContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();

        // 加载数据
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
                        getView().getRecyclerAdapter().replaceData(tResult);
                        getView().onAdapterDataChanged();
                    }
                })
                .execute();
    }
}
