package com.hyd.htalker.factory.presenter.message;

import com.hyd.htalker.factory.data.helper.GroupHelper;
import com.hyd.htalker.factory.data.message.MessageGroupRepository;
import com.hyd.htalker.factory.model.db.Group;
import com.hyd.htalker.factory.model.db.MemberUserModel;
import com.hyd.htalker.factory.model.db.Message;
import com.hyd.htalker.factory.persistence.Account;

import java.util.List;

/**
 * 群聊天的逻辑
 * Created by hydCoder on 2019/11/20.
 * 以梦为马，明日天涯。
 */
public class ChatGroupPresenter extends ChatPresenter<ChatContract.GroupView>
        implements ChatContract.Presenter {

    public ChatGroupPresenter(ChatContract.GroupView view, String receiverId) {
        // 数据源，view， 接收者id， 接收者的类型
        super(new MessageGroupRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_GROUP);
    }

    @Override
    public void start() {
        super.start();

        // 拿群的信息
        Group group = GroupHelper.findFromLocal(mReceiverId);
        if (group != null) {
            // 初始化操作
            ChatContract.GroupView view = getView();
            boolean isAdmin = Account.getUserId().equalsIgnoreCase(group.getOwner().getId());
            view.showAdminOption(isAdmin);

            // 基础信息初始化
            view.onInit(group);

            // 成员初始化
            List<MemberUserModel> models = group.getLatelyGroupMembers();
            long memberCount = group.getGroupMemberCount();
            // 没有显示的成员的数量
            long moreCount = memberCount - models.size();
            view.onInitGroupMember(models, moreCount);
        }
    }
}
