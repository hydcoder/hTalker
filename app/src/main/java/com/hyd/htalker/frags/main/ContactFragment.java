package com.hyd.htalker.frags.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyd.common.app.BaseFragment;
import com.hyd.common.widget.EmptyView;
import com.hyd.common.widget.PortraitView;
import com.hyd.common.widget.recycler.RecyclerAdapter;
import com.hyd.htalker.R;
import com.hyd.htalker.factory.model.card.UserCard;
import com.hyd.htalker.factory.model.db.User;
import com.hyd.htalker.factory.presenter.contact.FollowContract;
import com.hyd.htalker.factory.presenter.contact.FollowPresenter;
import com.hyd.htalker.frags.search.SearchUserFragment;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hydCoder on 2019/10/29.
 * 以梦为马，明日天涯。
 */
public class ContactFragment extends BaseFragment {

    @BindView(R.id.user_rv)
    RecyclerView userRv;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    // 适配器，User，可以直接从数据库查询数据
    private RecyclerAdapter<User> mAdapter;

    public ContactFragment() {
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        userRv.setLayoutManager(new LinearLayoutManager(getContext()));
        userRv.setAdapter(mAdapter = new RecyclerAdapter<User>(){

            @Override
            protected int getItemViewType(int position, User user) {
                // 返回cell的布局Id
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return null;
            }
        });
        // 初始化占位布局
        mEmptyView.bind(userRv);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<User> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_desc)
        TextView mDesc;

        private FollowContract.Presenter mPresenter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 当前View和presenter绑定
        }

        @Override
        protected void onBind(User user) {
            mPortraitView.setUp(Glide.with(ContactFragment.this), user);
            mName.setText(user.getName());
        }

        @OnClick(R.id.im_follow)
        void onFollowClick() {
            // 关注的触发
            mPresenter.follow(mData.getId());
        }
    }
}
