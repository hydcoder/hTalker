package com.hyd.htalker.frags.message;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.hyd.common.app.BaseFragment;
import com.hyd.common.widget.adapter.TextWatcherAdapter;
import com.hyd.htalker.R;
import com.hyd.htalker.activities.MessageActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hydCoder on 2019/11/18.
 * 以梦为马，明日天涯。
 */
public abstract class ChatFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener {

    protected String mReceiverId;

    @BindView(R.id.im_header)
    ImageView mHeader;
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        initToolBar();
        initAppBar();
        initEditContent();
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
                } else {
                    onMoreClick();
                }
                break;
        }
    }

    private void onMoreClick() {
        // TODO 
    }
}
