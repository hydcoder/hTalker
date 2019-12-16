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
import com.hyd.htalker.factory.model.card.UserCard;
import com.hyd.htalker.factory.presenter.contact.FollowContract;
import com.hyd.htalker.factory.presenter.contact.FollowPresenter;
import com.hyd.htalker.factory.presenter.search.SearchContract;
import com.hyd.htalker.factory.presenter.search.SearchUserPresenter;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索人的fragment
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment, SearchContract.UserView {

    @BindView(R.id.user_rv)
    RecyclerView userRv;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<UserCard> mAdapter;

    public SearchUserFragment() {
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        userRv.setLayoutManager(new LinearLayoutManager(getContext()));
        userRv.setAdapter(mAdapter = new RecyclerAdapter<UserCard>(){

            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                // 返回cell的布局Id
                return R.layout.cell_search_user;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
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
        return new SearchUserPresenter(this);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        // 数据成功的情况下返回数据
        mAdapter.replaceData(userCards);
        mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard> implements FollowContract.View {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.im_follow)
        ImageView mFollow;

        private FollowContract.Presenter mPresenter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 当前View和presenter绑定
            new FollowPresenter(this);
        }

        @Override
        protected void onBind(UserCard userCard) {
            mPortraitView.setUp(Glide.with(SearchUserFragment.this), userCard);
            mName.setText(userCard.getName());
            mFollow.setEnabled(!userCard.isFollow());
        }

        @OnClick(R.id.im_portrait)
        void onPortraitClick() {
            // 进入个人信息页面
            PersonalActivity.show(getContext(), mData.getId());
        }

        @OnClick(R.id.im_follow)
        void onFollowClick() {
            // 关注的触发
            mPresenter.follow(mData.getId());
        }

        @Override
        public void onFollowSucceed(UserCard userCard) {
            if (mFollow.getDrawable() instanceof LoadingDrawable) {
                ((LoadingDrawable) mFollow.getDrawable()).stop();
                mFollow.setImageResource(R.drawable.sel_opt_done_add);
            }
            // 发起更新
            updateData(userCard);
        }

        @Override
        public void showError(int str) {
            if (mFollow.getDrawable() instanceof LoadingDrawable) {
                // 失败则停止动画，并且显示一个圆圈
                LoadingDrawable drawable = (LoadingDrawable) mFollow.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(), 22);
            int maxSize = (int) Ui.dipToPx(getResources(), 30);
            // 初始化一个圆形的动画的Drawable
            LoadingDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);
            drawable.setBackgroundColor(0);
            drawable.setForegroundColor(new int[]{UiCompat.getColor(getResources(), R.color.white_alpha_208)});
            mFollow.setImageDrawable(drawable);
            // 启动动画
            drawable.start();
        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            mPresenter = presenter;
        }
    }
}
