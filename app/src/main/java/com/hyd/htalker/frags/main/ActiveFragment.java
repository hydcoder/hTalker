package com.hyd.htalker.frags.main;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyd.common.app.PresenterFragment;
import com.hyd.common.utils.DateTimeUtil;
import com.hyd.common.widget.EmptyView;
import com.hyd.common.widget.PortraitView;
import com.hyd.common.widget.recycler.RecyclerAdapter;
import com.hyd.htalker.R;
import com.hyd.htalker.activities.MessageActivity;
import com.hyd.htalker.activities.PersonalActivity;
import com.hyd.htalker.factory.model.db.Session;
import com.hyd.htalker.factory.presenter.message.SessionContract;
import com.hyd.htalker.factory.presenter.message.SessionPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 会话列表的fragment
 *
 * Created by hydCoder on 2019/10/29.
 * 以梦为马，明日天涯。
 */
public class ActiveFragment extends PresenterFragment<SessionContract.Presenter> implements SessionContract.View {

    @BindView(R.id.session_rv)
    RecyclerView sessionRv;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<Session> mAdapter;

    public ActiveFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void OnFirstInitData() {
        super.OnFirstInitData();
        // 进行一次数据加载
        mPresenter.start();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        sessionRv.setLayoutManager(new LinearLayoutManager(getContext()));
        sessionRv.setAdapter(mAdapter = new RecyclerAdapter<Session>(){

            @Override
            protected int getItemViewType(int position, Session session) {
                // 返回cell的布局Id
                return R.layout.cell_session_list;
            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {
                return new ActiveFragment.ViewHolder(root);
            }
        });

        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Session>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Session session) {
                // 跳转到聊天界面
                MessageActivity.show(getContext(), session);
            }
        });

        // 初始化占位布局
        mEmptyView.bind(sessionRv);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected SessionContract.Presenter initPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Session> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_content)
        TextView mContent;

        @BindView(R.id.txt_time)
        TextView mTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 当前View和presenter绑定
        }

        @Override
        protected void onBind(Session session) {
            mPortraitView.setUp(Glide.with(ActiveFragment.this), session.getPicture());
            mName.setText(session.getTitle());
            mContent.setText(TextUtils.isEmpty(session.getContent()) ? "" : session.getContent());
            mTime.setText(DateTimeUtil.getSimpleDate(session.getModifyAt()));
        }

        @OnClick(R.id.im_portrait)
        void onPortraitClick() {
            // 进入个人信息页面
            PersonalActivity.show(getContext(), mData.getId());
        }
    }
}
