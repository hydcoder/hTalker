package com.hyd.htalker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.hyd.common.app.BaseActivity;
import com.hyd.common.app.BaseFragment;
import com.hyd.common.factory.model.Author;
import com.hyd.htalker.R;
import com.hyd.htalker.factory.model.db.Group;
import com.hyd.htalker.frags.message.ChatGroupFragment;
import com.hyd.htalker.frags.message.ChatUserFragment;

public class MessageActivity extends BaseActivity {
    // 接收者id，可以是userId，也可以是groupId
    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";
    // 标识是否是群
    public static final String KEY_RECEIVER_GROUP = "KEY_RECEIVER_GROUP";

    private String mReceiverId;
    private boolean mIsGroup;

    /**
     * 显示人的聊天界面
     *
     * @param context 上下文
     * @param author  人的信息
     */
    public static void show(Context context, Author author) {
        if (author == null || context == null || TextUtils.isEmpty(author.getId())) {
            return;
        }

        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, author.getId());
        intent.putExtra(KEY_RECEIVER_GROUP, false);
        context.startActivity(intent);
    }

    /**
     * 发起群聊天
     *
     * @param context 上下文
     * @param group   群的model
     */
    public static void show(Context context, Group group) {
        if (group == null || context == null || TextUtils.isEmpty(group.getId())) {
            return;
        }

        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, group.getId());
        intent.putExtra(KEY_RECEIVER_GROUP, true);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
        mIsGroup = bundle.getBoolean(KEY_RECEIVER_GROUP);
        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        BaseFragment fragment;
        if (mIsGroup) {
            fragment = new ChatGroupFragment();
        } else {
            fragment = new ChatUserFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID, mReceiverId);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.lay_container, fragment).commit();
    }
}
