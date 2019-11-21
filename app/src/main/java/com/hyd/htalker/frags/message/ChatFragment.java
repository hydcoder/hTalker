package com.hyd.htalker.frags.message;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.hyd.common.app.PresenterFragment;
import com.hyd.common.widget.PortraitView;
import com.hyd.common.widget.adapter.TextWatcherAdapter;
import com.hyd.common.widget.recycler.RecyclerAdapter;
import com.hyd.htalker.R;
import com.hyd.htalker.activities.MessageActivity;
import com.hyd.htalker.factory.model.db.Message;
import com.hyd.htalker.factory.model.db.User;
import com.hyd.htalker.factory.persistence.Account;
import com.hyd.htalker.factory.presenter.message.ChatContract;

import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hydCoder on 2019/11/18.
 * 以梦为马，明日天涯。
 */
public abstract class ChatFragment<InitModel> extends PresenterFragment<ChatContract.Presenter>
        implements AppBarLayout.OnOffsetChangedListener, ChatContract.View<InitModel> {

    protected String mReceiverId;
    protected Adapter mAdapter;

    @BindView(R.id.im_header)
    ImageView mHeader;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_content)
    EditText mContent;
    @BindView(R.id.btn_submit)
    ImageView mSubmit;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        initToolBar();
        initAppBar();
        initEditContent();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        // 进行初始化操作
        mPresenter.start();
    }

    protected void initToolBar() {
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(view -> getActivity().finish());
    }

    private void initAppBar() {
        // 给界面的appbar添加一个监听，得到打开与关闭的时候的高度，从而计算出打开与关闭的进度
        mAppbarLayout.addOnOffsetChangedListener(this);
    }

    private void initEditContent() {
        mContent.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
                String content = editable.toString().trim();
                boolean isNeedSendMsg = !TextUtils.isEmpty(content);
                // 设置状态，改变发送按钮的状态
                mSubmit.setActivated(isNeedSendMsg);
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    @OnClick({R.id.btn_face, R.id.btn_record, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_face:
                break;
            case R.id.btn_record:
                break;
            case R.id.btn_submit:
                if (mSubmit.isActivated()) {
                    // 发送消息
                    String content = mContent.getText().toString();
                    mContent.setText("");
                    mPresenter.pushText(content);
                } else {
                    onMoreClick();
                }
                break;
        }
    }

    private void onMoreClick() {
        // TODO
    }

    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        // 界面没有占位布局，RecyclerView是一直显示的，所以不用做任何操作
    }

    class Adapter extends RecyclerAdapter<Message> {

        @Override
        protected int getItemViewType(int position, Message message) {
            // 我发送的在右边，收到的在左边
            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUserId());
            int type = message.getType();
            switch (type) {
                // 语音消息
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;

                // 图片消息
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;

                // 文字消息
                // 其他消息
                case Message.TYPE_STR:
                default:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
            }
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType) {
                // 左右布局共用一个holder
                case R.layout.cell_chat_audio_right:
                case R.layout.cell_chat_audio_left:
                    return new AudioHolder(root);

                case R.layout.cell_chat_pic_right:
                case R.layout.cell_chat_pic_left:
                    return new PicHolder(root);

                case R.layout.cell_chat_text_right:
                case R.layout.cell_chat_text_left:
                default:   // 默认情况下，也是返回Text类型的holder进行处理
                    return new TextHolder(root);
            }
        }
    }

    // 基类holder
    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {

        @BindView(R.id.im_portrait)
        PortraitView mPortrait;

        // 允许为空，左边没有，右边有
        @Nullable
        @BindView(R.id.loading)
        Loading mLoading;

        public BaseHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            // 进行数据加载sender
            sender.load();

            mPortrait.setUp(Glide.with(ChatFragment.this), sender);

            if (mLoading != null) {
                // 当前布局是属于右边的
                int status = message.getStatus();
                if (status == Message.STATUS_DONE) {
                    // 正常状态，隐藏loading
                    mLoading.stop();
                    mLoading.setVisibility(View.GONE);
                } else if (status == Message.STATUS_CREATED) {
                    // 正在发送中的状态
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(0);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(),
                            R.color.colorAccent));
                    mLoading.start();
                } else if (status == Message.STATUS_FAILED) {
                    // 发送失败状态，允许点击头像重新发送
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(1);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(),
                            R.color.alertImportant));
                    mLoading.stop();
                }
                // 当状态是发送错误的状态才允许点击
                mPortrait.setEnabled(status == Message.STATUS_FAILED);
            }
        }

        @OnClick(R.id.im_portrait)
        void onRePushClick() {
            if (mLoading != null && mPresenter.rePush(mData)) {
                // 必须是右边的才有可能重新发送
                // 发送成功需要刷新消息的状态
                updateData(mData);
            }
        }
    }

    // 文本消息的holder
    class TextHolder extends BaseHolder {

        @BindView(R.id.txt_content)
        TextView mContent;

        public TextHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);

            mContent.setText(message.getContent());
        }
    }

    // 语音消息的holder
    class AudioHolder extends BaseHolder {

        public AudioHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    // 图片消息的holder
    class PicHolder extends BaseHolder {

        public PicHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
