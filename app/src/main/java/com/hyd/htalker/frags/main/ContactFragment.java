package com.hyd.htalker.frags.main;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyd.common.common.app.PresenterFragment;
import com.hyd.common.common.widget.EmptyView;
import com.hyd.common.common.widget.PortraitView;
import com.hyd.common.common.widget.recycler.RecyclerAdapter;
import com.hyd.htalker.R;
import com.hyd.htalker.activities.MessageActivity;
import com.hyd.htalker.activities.PersonalActivity;
import com.hyd.htalker.factory.model.db.User;
import com.hyd.htalker.factory.presenter.contact.ContactContract;
import com.hyd.htalker.factory.presenter.contact.ContactPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hydCoder on 2019/10/29.
 * 以梦为马，明日天涯。
 */
public class ContactFragment extends PresenterFragment<ContactContract.Presenter> implements ContactContract.View {

    @BindView(R.id.contact_rv)
    RecyclerView contactRv;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    // 适配器，User，可以直接从数据库查询数据
    private RecyclerAdapter<User> mAdapter;

    public ContactFragment() {
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        contactRv.setLayoutManager(new LinearLayoutManager(getContext()));
        contactRv.setAdapter(mAdapter = new RecyclerAdapter<User>(){

            @Override
            protected int getItemViewType(int position, User user) {
                // 返回cell的布局Id
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return new ContactFragment.ViewHolder(root);
            }
        });

        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<User>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, User user) {
                // 跳转到聊天界面
                MessageActivity.show(getContext(), user);
            }
        });

        // 初始化占位布局
        mEmptyView.bind(contactRv);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void OnFirstInitData() {
        super.OnFirstInitData();
        // 进行一次数据加载
        mPresenter.start();
    }

    @Override
    protected ContactContract.Presenter initPresenter() {
        return new ContactPresenter(this);
    }

    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<User> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_desc)
        TextView mDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 当前View和presenter绑定
        }

        @Override
        protected void onBind(User user) {
            mPortraitView.setUp(Glide.with(ContactFragment.this), user);
            mName.setText(user.getName());
            mDesc.setText(user.getDesc());
        }

        @OnClick(R.id.im_portrait)
        void onPortraitClick() {
            // 进入个人信息页面
            PersonalActivity.show(getContext(), mData.getId());
        }
    }
}
