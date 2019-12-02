package com.hyd.htalker.frags.search;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyd.common.app.PresenterFragment;
import com.hyd.common.widget.EmptyView;
import com.hyd.common.widget.PortraitView;
import com.hyd.common.widget.recycler.RecyclerAdapter;
import com.hyd.htalker.R;
import com.hyd.htalker.activities.PersonalActivity;
import com.hyd.htalker.activities.SearchActivity;
import com.hyd.htalker.factory.model.card.GroupCard;
import com.hyd.htalker.factory.presenter.search.SearchContract;
import com.hyd.htalker.factory.presenter.search.SearchGroupPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索群的fragment
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.Presenter> implements SearchActivity.SearchFragment, SearchContract.GroupView {

    @BindView(R.id.user_rv)
    RecyclerView userRv;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<GroupCard> mAdapter;

    public SearchGroupFragment() {
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        userRv.setLayoutManager(new LinearLayoutManager(getContext()));
        userRv.setAdapter(mAdapter = new RecyclerAdapter<GroupCard>() {

            @Override
            protected int getItemViewType(int position, GroupCard groupCard) {
                // 返回cell的布局Id
                return R.layout.cell_search_group;
            }

            @Override
            protected ViewHolder<GroupCard> onCreateViewHolder(View root, int viewType) {
                return new SearchGroupFragment.ViewHolder(root);
            }
        });
        // 初始化占位布局
        mEmptyView.bind(userRv);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        // 发起首次搜索
        search("");
    }

    @Override
    public void search(String content) {
        // ACT -> FRAGMENT -> Presenter -> Net
        mPresenter.search(content);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        // 初始化presenter
        return new SearchGroupPresenter(this);
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {
        // 数据成功的情况下返回数据
        mAdapter.replaceData(groupCards);
        mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCard> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_desc)
        TextView mDesc;

        @BindView(R.id.im_join)
        ImageView mFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GroupCard groupCard) {
            mPortraitView.setUp(Glide.with(SearchGroupFragment.this), groupCard.getPicture());
            mName.setText(groupCard.getName());
            mDesc.setText(groupCard.getDesc());
            // 通过加入时间判断是否可以加入群
            mFollow.setEnabled(groupCard.getJoinAt() == null);
        }

        @OnClick(R.id.im_join)
        void onJoinClick() {
            // 进入创建者的个人界面
            PersonalActivity.show(getContext(), mData.getOwnerId());
        }
    }
}
