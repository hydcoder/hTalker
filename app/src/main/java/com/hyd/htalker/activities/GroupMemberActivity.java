package com.hyd.htalker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyd.common.common.app.PresenterToolbarActivity;
import com.hyd.common.common.widget.PortraitView;
import com.hyd.common.common.widget.recycler.RecyclerAdapter;
import com.hyd.htalker.R;
import com.hyd.htalker.factory.model.db.MemberUserModel;
import com.hyd.htalker.factory.presenter.group.GroupMemberContract;
import com.hyd.htalker.factory.presenter.group.GroupMemberPresenter;
import com.hyd.htalker.frags.group.GroupMemberAddFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupMemberActivity extends PresenterToolbarActivity<GroupMemberContract.Presenter>
        implements GroupMemberContract.View, GroupMemberAddFragment.Callback {
    public static final String KEY_GROUP_ID = "KEY_GROUP_ID";
    public static final String KEY_GROUP_ADMIN = "KEY_GROUP_ADMIN";

    private String mGroupId;
    private boolean mIsAdmin;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<MemberUserModel> mAdapter;

    public static void show(Context context, String groupId, boolean isAdmin) {
        if (TextUtils.isEmpty(groupId)) {
            return;
        }
        Intent intent = new Intent(context, GroupMemberActivity.class);
        intent.putExtra(KEY_GROUP_ID, groupId);
        intent.putExtra(KEY_GROUP_ADMIN, isAdmin);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_member;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle(R.string.title_member_list);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<MemberUserModel>() {
            @Override
            protected int getItemViewType(int position, MemberUserModel memberUserModel) {
                return R.layout.cell_group_create_contact;
            }

            @Override
            protected ViewHolder<MemberUserModel> onCreateViewHolder(View root, int viewType) {
                return new GroupMemberActivity.ViewHolder(root);
            }
        });
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mGroupId = bundle.getString(KEY_GROUP_ID);
        mIsAdmin = bundle.getBoolean(KEY_GROUP_ADMIN, false);
        return !TextUtils.isEmpty(mGroupId);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.refresh();

        // 显示管理员界面，添加成员
        if (mIsAdmin) {
            new GroupMemberAddFragment()
                    .show(getSupportFragmentManager(), GroupMemberAddFragment.class.getName());
        }
    }

    @Override
    protected GroupMemberContract.Presenter initPresenter() {
        return new GroupMemberPresenter(this);
    }

    @Override
    public String getGroupId() {
        return mGroupId;
    }

    @Override
    public RecyclerAdapter<MemberUserModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        hideLoading();
    }

    @Override
    public void refreshMembers() {
        // 重新加载成员信息
        if (mPresenter != null)
            mPresenter.refresh();
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<MemberUserModel> {

        @BindView(R.id.im_portrait)
        PortraitView mPortrait;

        @BindView(R.id.txt_name)
        TextView mName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.findViewById(R.id.cb_select).setVisibility(View.GONE);
        }

        @Override
        protected void onBind(MemberUserModel viewModel) {
            mPortrait.setUp(Glide.with(GroupMemberActivity.this), viewModel.portrait);
            mName.setText(viewModel.name);
        }

        @OnClick(R.id.im_portrait)
        void onPortraitClick() {
            PersonalActivity.show(GroupMemberActivity.this, mData.userId);
        }
    }
}
