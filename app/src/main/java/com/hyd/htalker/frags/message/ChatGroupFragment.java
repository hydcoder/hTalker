package com.hyd.htalker.frags.message;


import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.hyd.common.widget.PortraitView;
import com.hyd.htalker.R;
import com.hyd.htalker.activities.PersonalActivity;
import com.hyd.htalker.factory.model.db.Group;
import com.hyd.htalker.factory.model.db.MemberUserModel;
import com.hyd.htalker.factory.presenter.message.ChatContract;
import com.hyd.htalker.factory.presenter.message.ChatGroupPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * 群聊的界面
 *
 * Created by hydCoder on 2019/11/18.
 * 以梦为马，明日天涯。
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatContract.GroupView {

    @BindView(R.id.im_header)
    ImageView mHeader;

    @BindView(R.id.lay_members)
    LinearLayout mLayMembers;

    @BindView(R.id.txt_member_more)
    TextView mTxtMemberMore;

    public ChatGroupFragment() {
    }

    @Override
    protected int getHeaderLayoutId() {
        return R.layout.lay_chat_header_group;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        Glide.with(this).load(R.drawable.default_banner_group)
                .centerCrop()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<
                                                ? super Drawable> transition) {
                        mCollapsingToolbarLayout.setContentScrim(resource.getCurrent());
                    }
                });
    }

    // 进行高度的综合运算，透明头像和menu icon
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        View view = mLayMembers;
        if (view == null) {
            return;
        }

        if (verticalOffset == 0) {
            // 完全展开状态
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);

        } else {
            // abs运算取绝对值
            verticalOffset = Math.abs(verticalOffset);
            // appbar最大的可滚动高度
            int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScrollRange) {
                // 关闭状态
                view.setVisibility(View.GONE);
                view.setScaleX(0);
                view.setScaleY(0);
                view.setAlpha(0);
            } else {
                // 中间状态
                float progress = 1 - verticalOffset / (float) totalScrollRange;
                view.setVisibility(View.VISIBLE);
                view.setScaleX(progress);
                view.setScaleY(progress);
                view.setAlpha(progress);
            }
        }
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return new ChatGroupPresenter(this, mReceiverId);
    }

    @Override
    public void onInit(Group group) {
        // 群聊时初始化群信息
        mCollapsingToolbarLayout.setTitle(group.getName());
        Glide.with(this).load(group.getPicture())
                .centerCrop()
                .placeholder(R.drawable.default_banner_group)
                .into(mHeader);
    }

    @Override
    public void onInitGroupMember(List<MemberUserModel> members, long moreCount) {
        if (members == null || members.isEmpty()) {
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (MemberUserModel member : members) {
            PortraitView portrait = (PortraitView) inflater.inflate(R.layout.lay_chat_group_portrait, mLayMembers, false);
            mLayMembers.addView(portrait, 0);
            portrait.setUp(Glide.with(this), member.portrait);

            portrait.setOnClickListener(v -> PersonalActivity.show(getContext(), member.userId));
        }

        if (moreCount > 0) {
            //noinspection MalformedFormatString
            mTxtMemberMore.setText(String.format("+%s", moreCount));
            mTxtMemberMore.setOnClickListener(v -> {
                // TODO
            });
        } else {
            mTxtMemberMore.setVisibility(View.GONE);
        }
    }

    @Override
    public void showAdminOption(boolean isAdmin) {
        if (isAdmin) {
            mToolbar.inflateMenu(R.menu.chat_group);
            mToolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_add) {
                    // TODO 进行群成员添加操作
                    return true;
                }
                return false;
            });
        }
    }
}
