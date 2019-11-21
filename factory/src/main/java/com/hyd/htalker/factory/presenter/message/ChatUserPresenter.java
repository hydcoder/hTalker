package com.hyd.htalker.factory.presenter.message;

import com.hyd.htalker.factory.data.helper.UserHelper;
import com.hyd.htalker.factory.data.message.MessageRepository;
import com.hyd.htalker.factory.model.db.Message;
import com.hyd.htalker.factory.model.db.User;

/**
 * Created by hydCoder on 2019/11/20.
 * 以梦为马，明日天涯。
 */
public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView>
implements ChatContract.Presenter {

    public ChatUserPresenter(ChatContract.UserView view, String receiverId) {
        // 数据源，view， 接收者id， 接收者的类型
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);
    }

    @Override
    public void start() {
        super.start();

        // 从本地拿这个人的信息
        User receiver = UserHelper.findFromLocal(mReceiverId);
        getView().onInit(receiver);
    }
}
