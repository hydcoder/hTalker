package com.hyd.htalker.factory.presenter.contact;

import androidx.recyclerview.widget.DiffUtil;

import com.hyd.common.factory.data.DataSource;
import com.hyd.common.common.widget.recycler.RecyclerAdapter;
import com.hyd.htalker.factory.data.helper.UserHelper;
import com.hyd.htalker.factory.data.user.ContactDataSource;
import com.hyd.htalker.factory.data.user.ContactRepository;
import com.hyd.htalker.factory.model.db.User;
import com.hyd.htalker.factory.presenter.BaseSourcePresenter;
import com.hyd.htalker.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 联系人的presenter实现
 * Created by hydCoder on 2019/11/5.
 * 以梦为马，明日天涯。
 */
public class ContactPresenter extends BaseSourcePresenter<User, User, ContactDataSource, ContactContract.View>
        implements ContactContract.Presenter, DataSource.SucceedCallback<List<User>> {

    public ContactPresenter(ContactContract.View view) {
        // 初始化数据仓库
        super(new ContactRepository(), view);
    }

    @Override
    public void start() {
        super.start();

        /*// 加载本地数据库数据
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

        // 加载网络数据
        UserHelper.refreshContacts(new DataSource.Callback<List<UserCard>>() {
            @Override
            public void onDataNotAvailable(int strRes) {
                // 网络失败，因为本地有数据，不管错误
            }

            @Override
            public void onDataLoaded(List<UserCard> userCards) {
                // 转换为User
                final List<User> users = new ArrayList<>();
                for (UserCard userCard : userCards) {
                    users.add(userCard.build());
                }

                // 将网络数据保存到本地数据库
                DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
                definition.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        FlowManager.getModelAdapter(User.class)
                                .saveAll(users);
                    }
                }).build().execute();

                diff(getView().getRecyclerAdapter().getDataList(), users);
            }
        });*/

        // TODO 问题
        // 1、关注后虽然存储了数据库，但是没有刷新联系人；
        // 2、如果从数据库刷新数据，或者从网络刷新，最终刷新的时候还是全局刷新； 通过数据对比解决---DiffUtil
        // 3、本地数据库刷新和网络刷新，在显示添加到界面的时候会有可能冲突；导致数据显示异常
        // 4、如何识别在数据库中已经有这样的数据

        // 直接从网络获取，然后存储到数据库，
        // 并通过数据库观察者进行通知界面更新，
        //  界面更新的时候进行对比，然后差异更新
        // 便解决了上述的问题
        UserHelper.refreshContacts();
    }

    private void diff(List<User> oldList, List<User> newList) {
        // 进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<>(oldList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);

        // 在对比完成后进行数据的赋值
        getView().getRecyclerAdapter().replaceData(newList);

        // 尝试刷新界面
        diffResult.dispatchUpdatesTo(getView().getRecyclerAdapter());

        getView().onAdapterDataChanged();
    }

    // 运行到这里的时候是子线程
    @Override
    public void onDataLoaded(List<User> users) {
        // 无论怎么操作，数据变更，最终都会通知到这里来
        final ContactContract.View view = getView();
        if (view == null)
            return;

        RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> old = adapter.getDataList();

        // 进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<>(old, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 调用基类方法进行界面刷新
        refreshData(result, users);
    }
}
