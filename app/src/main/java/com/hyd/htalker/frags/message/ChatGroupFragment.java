package com.hyd.htalker.frags.message;


import com.hyd.htalker.R;
import com.hyd.htalker.factory.model.db.Group;
import com.hyd.htalker.factory.presenter.message.ChatContract;

/**
 * 群聊的界面
 *
 * Created by hydCoder on 2019/11/18.
 * 以梦为马，明日天涯。
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatContract.GroupView {


    public ChatGroupFragment() {
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public void onInit(Group group) {
        // 群聊时初始化群信息
    }
}
