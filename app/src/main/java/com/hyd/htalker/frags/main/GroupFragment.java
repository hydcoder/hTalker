package com.hyd.htalker.frags.main;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyd.common.app.PresenterFragment;
import com.hyd.common.widget.EmptyView;
import com.hyd.common.widget.PortraitView;
import com.hyd.common.widget.recycler.RecyclerAdapter;
import com.hyd.htalker.R;
import com.hyd.htalker.activities.MessageActivity;
import com.hyd.htalker.factory.model.db.Group;
import com.hyd.htalker.factory.presenter.contact.GroupsContract;
import com.hyd.htalker.factory.presenter.group.GroupsPresenter;

import butterknife.BindView;

/**
 * Created by hydCoder on 2019/10/29.
 * 以梦为马，明日天涯。
 */
public class GroupFragment extends PresenterFragment<GroupsContract.Presenter>
implements GroupsContract.View {

    @BindView(R.id.contact_rv)
    RecyclerView contactRv;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    // 适配器，User，可以直接从数据库查询数据
    private RecyclerAdapter<Group> mAdapter;

    public GroupFragment() {
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        contactRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        contactRv.setAdapter(mAdapter = new RecyclerAdapter<Group>(){

            @Override
            protected int getItemViewType(int position, Group group) {
                // 返回cell的布局Id
                return R.layout.cell_group_list;
            }

            @Override
            protected ViewHolder<Group> onCreateViewHolder(View root, int viewType) {
                return new GroupFragment.ViewHolder(root);
            }
        });

        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Group>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Group group) {
                // 跳转到聊天界面
                MessageActivity.show(getContext(), group);
            }
        });

        // 初始化占位布局
        mEmptyView.bind(contactRv);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void OnFirstInitData() {
        super.OnFirstInitData();
        // 进行一次数据加载
        mPresenter.start();
    }

    @Override
    protected GroupsContract.Presenter initPresenter() {
        return new GroupsPresenter(this);
    }

    @Override
    public RecyclerAdapter<Group> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Group> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_desc)
        TextView mDesc;

        @BindView(R.id.txt_member)
        TextView mMember;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Group group) {
            mPortraitView.setUp(Glide.with(GroupFragment.this), group.getPicture());
            mName.setText(group.getName());
            mDesc.setText(group.getDesc());
            if (group.holder instanceof String) {
                mMember.setText((String) group.holder);
            } else {
                mMember.setText("");
            }
        }
    }
}
