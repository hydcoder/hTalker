package com.hyd.htalker.factory.presenter.message;

import com.hyd.common.factory.presenter.BaseContract;
import com.hyd.htalker.factory.model.db.Group;
import com.hyd.htalker.factory.model.db.MemberUserModel;
import com.hyd.htalker.factory.model.db.Message;
import com.hyd.htalker.factory.model.db.User;

import java.util.List;

/**
 * 聊天的契约
 *
 * Created by hydCoder on 2019/11/20.
 * 以梦为马，明日天涯。
 */
public interface ChatContract {

    interface Presenter extends BaseContract.Presenter {
        // 发送文字
        void pushText(String content);
        // 发送语音
        void pushAudio(String path, long time);
        // 发送图片
        void pushImages(String[] paths);

        // 重新发送一条消息，返回是否发送成功
        boolean rePush(Message message);
    }

    // 聊天界面的基类
    interface View<InitModel> extends BaseContract.RecyclerView<Presenter, Message> {
        // 初始化的model
        void onInit(InitModel model);
    }

    // 单聊的界面
    interface UserView extends View<User> {

    }

    // 群聊的界面
    interface GroupView extends View<Group> {
        // 是否显示管理员菜单
        void showAdminOption(boolean isAdmin);

        void onInitGroupMember(List<MemberUserModel> members, long moreCount);
    }
}
