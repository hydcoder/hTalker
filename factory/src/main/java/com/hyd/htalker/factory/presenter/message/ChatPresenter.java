package com.hyd.htalker.factory.presenter.message;

import androidx.recyclerview.widget.DiffUtil;

import com.hyd.htalker.factory.data.helper.MessageHelper;
import com.hyd.htalker.factory.data.message.MessageDataSource;
import com.hyd.htalker.factory.model.api.message.MsgCreateModel;
import com.hyd.htalker.factory.model.db.Message;
import com.hyd.htalker.factory.persistence.Account;
import com.hyd.htalker.factory.presenter.BaseSourcePresenter;
import com.hyd.htalker.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 聊天presenter的基础类
 * <p>
 * Created by hydCoder on 2019/11/20.
 * 以梦为马，明日天涯。
 */
public class ChatPresenter<View extends ChatContract.View> extends BaseSourcePresenter<Message,
        Message, MessageDataSource, View> implements ChatContract.Presenter {

    // 接收者id，可能是人，也可能是群
    protected String mReceiverId;
    // 区分是人还是群id
    protected int mReceiverType;

    public ChatPresenter(MessageDataSource source, View view, String receiverId, int receiverType) {
        super(source, view);
        this.mReceiverId = receiverId;
        this.mReceiverType = receiverType;
    }

    @Override
    public void pushText(String content) {
        MsgCreateModel model = new MsgCreateModel.Builder()
                .receiver(mReceiverId, mReceiverType)
                .content(content, Message.TYPE_STR)
                .build();

        // 进行网络发送
        MessageHelper.push(model);
    }

    @Override
    public void pushAudio(String path) {
        // TODO 发送语音
    }

    @Override
    public void pushImages(String[] paths) {
        if (paths == null || paths.length == 0) {
            return;
        }

        // 此时的路径是本地的手机上的路径
        for (String path : paths) {
            // 构建一个新的消息
            MsgCreateModel model = new MsgCreateModel.Builder()
                    .receiver(mReceiverId, mReceiverType)
                    .content(path, Message.TYPE_PIC)
                    .build();

            // 进行网络发送
            MessageHelper.push(model);
        }
    }

    @Override
    public boolean rePush(Message message) {
        // 确认消息是可重新发送的
        if (Account.getUserId().equalsIgnoreCase(message.getSender().getId())
                && message.getStatus() == Message.STATUS_FAILED) {
            // 更改状态
            message.setStatus(Message.STATUS_CREATED);

            // 构建发送model
            MsgCreateModel model = MsgCreateModel.buildWithMessage(message);
            MessageHelper.push(model);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDataLoaded(List<Message> messages) {
        ChatContract.View view = getView();
        if (view == null) {
            return;
        }

        // 拿到老数据
        List oldData = view.getRecyclerAdapter().getDataList();

        // 新老数据进行差异对比
        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(oldData, messages);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);

        // 将差异的数据刷新到界面上
        refreshData(diffResult, messages);
    }
}
