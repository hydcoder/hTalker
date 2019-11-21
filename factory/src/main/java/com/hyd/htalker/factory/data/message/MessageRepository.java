package com.hyd.htalker.factory.data.message;

import androidx.annotation.NonNull;

import com.hyd.htalker.factory.data.BaseDbRepository;
import com.hyd.htalker.factory.model.db.Message;
import com.hyd.htalker.factory.model.db.Message_Table;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;


/**
 * 跟某人聊天时候的聊天列表
 * 关注的内容一定是一定是我发送给他的，或者是他发送给我的
 * <p>
 * Created by hydCoder on 2019/11/20.
 * 以梦为马，明日天涯。
 */
public class MessageRepository extends BaseDbRepository<Message> implements MessageDataSource {

    // 聊天的对象id
    private String receiverId;

    public MessageRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SucceedCallback<List<Message>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause().and(Message_Table.sender_id.eq(receiverId))
                .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)  // 倒序
                .limit(50)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Message message) {
        // receiverId如果是发送者，那么不是群聊的情况下一定是发送给我的消息
        // 如果消息的接收者不为空，那么一定是发送给某个人的，这个人只能是我或者是某个人
        // 如果这个“某个人”就是receiverId，那么就是我需要关注的信息
        return (receiverId.equalsIgnoreCase(message.getSender().getId()) && message.getGroup() == null)
                || (message.getReceiver() != null && receiverId.equalsIgnoreCase(message.getReceiver().getId()));
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        // 最近的一条消息要显示在最下面，所以结果要先倒序
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
