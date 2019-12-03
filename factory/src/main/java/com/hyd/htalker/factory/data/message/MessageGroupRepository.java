package com.hyd.htalker.factory.data.message;

import androidx.annotation.NonNull;

import com.hyd.htalker.factory.data.BaseDbRepository;
import com.hyd.htalker.factory.model.db.Message;
import com.hyd.htalker.factory.model.db.Message_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;


/**
 * 跟群聊天时候的聊天内容列表
 * 关注的内容一定是一定是我发送群的，或者是别人发送给群的
 * <p>
 * Created by hydCoder on 2019/11/20.
 * 以梦为马，明日天涯。
 */
public class MessageGroupRepository extends BaseDbRepository<Message> implements MessageDataSource {

    // 聊天的群id
    private String receiverId;

    public MessageGroupRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SucceedCallback<List<Message>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)  // 倒序
                .limit(50)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Message message) {
        return false;
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        // 最近的一条消息要显示在最下面，所以结果要先倒序
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
