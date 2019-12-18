package com.hyd.htalker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyd.common.app.BaseApplication;
import com.hyd.common.app.PresenterToolbarActivity;
import com.hyd.common.utils.CollectionUtil;
import com.hyd.common.utils.DateTimeUtil;
import com.hyd.common.widget.EmptyView;
import com.hyd.common.widget.PortraitView;
import com.hyd.common.widget.adapter.TextWatcherAdapter;
import com.hyd.common.widget.recycler.RecyclerAdapter;
import com.hyd.htalker.R;
import com.hyd.htalker.factory.model.card.FriendCircleCard;
import com.hyd.htalker.factory.persistence.Account;
import com.hyd.htalker.factory.presenter.friend.CommentContract;
import com.hyd.htalker.factory.presenter.friend.CommentPresenter;
import com.hyd.htalker.factory.presenter.friend.FabulousContract;
import com.hyd.htalker.factory.presenter.friend.FabulousPresenter;
import com.hyd.htalker.factory.presenter.friend.FriendCircleContract;
import com.hyd.htalker.factory.presenter.friend.FriendCirclePresenter;
import com.hyd.htalker.factory.utils.KeyBoardUtil;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.widget.EditText;
import net.qiujuer.widget.airpanel.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 朋友圈界面
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class FriendCircleActivity extends PresenterToolbarActivity<FriendCircleContract.Presenter> implements FriendCircleContract.View {

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<FriendCircleCard> mAdapter;

    public static void show(Context context) {
        context.startActivity(new Intent(context, FriendCircleActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_friend_circle;
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<FriendCircleCard>() {
            @Override
            protected ViewHolder<FriendCircleCard> onCreateViewHolder(View root, int viewType) {
                return new FriendCircleActivity.ViewHolder(root);
            }

            @Override
            protected int getItemViewType(int position, FriendCircleCard userCard) {
                //其实是返回cell的布局id
                return R.layout.cell_friend_circle;
            }
        });
        addHeaderView();
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    private void addHeaderView() {
        View view = LayoutInflater.from(this).inflate(R.layout.lay_friend_circle_header, null);
        TextView mName = view.findViewById(R.id.txt_name);
        ImageView mHeader = view.findViewById(R.id.im_bg);
        PortraitView portraitView = view.findViewById(R.id.im_portrait);
        mName.setText(Account.getUser().getName());
        portraitView.setUp(Glide.with(this), Account.getUser().getPortrait());
        Glide.with(this).asBitmap().load(R.drawable.default_portrait).placeholder(R.drawable.default_portrait).into(mHeader);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.friendCircle();
    }

    @Override
    public void onFriendCircleDone(List<FriendCircleCard> friendCircles) {
        //数据加载成功的时候返回数据
        mAdapter.replaceData(friendCircles);
        //如果有布局就显示布局，没有布局就显示空布局
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected FriendCircleContract.Presenter initPresenter() {
        return new FriendCirclePresenter(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.friend_circle, menu);

        // 根据状态设置颜色
        Drawable drawable = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_shot));
        DrawableCompat.setTint(drawable, Resource.Color.WHITE);
        menu.findItem(R.id.action_release).setIcon(drawable);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_release) {
            // 进行发布处理
            ReleaseFriendCircleActivity.show(FriendCircleActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //每一个Cell布局的操作
    class ViewHolder extends RecyclerAdapter.ViewHolder<FriendCircleCard> implements CommentContract.CommentView {

        @BindView(R.id.im_portrait)
        PortraitView mPortrait;

        @BindView(R.id.txt_title)
        TextView mTitle;

        @BindView(R.id.txt_content)
        TextView mContent;

        @BindView(R.id.recycler)
        RecyclerView mRecyclerView;

        @BindView(R.id.txt_date)
        TextView mDate;

        @BindView(R.id.txt_fabulous)
        TextView mFabulous;//赞的数量

        @BindView(R.id.im_fabulous)
        ImageView mImageFabulous;//赞的图片

        @BindView(R.id.txt_comment)
        TextView mComment;//评论的数量

        CommentContract.Presenter mPresenter;
        FabulousContract.Presenter mFabulousPresenter;
        RecyclerAdapter.AdapterListener<String> listener;

        public ViewHolder(View itemView) {
            super(itemView);
            new CommentPresenter(this);
            mFabulousPresenter = new FabulousPresenter(new FabulousContract.FabulousView() {
                @Override
                public void onFabulousDone() {
                    boolean isFabulous = !mData.isFabulous();
                    mData.setFabulous(isFabulous);
                    mData.setFabulousSize(isFabulous ? mData.getFabulousSize() + 1 :
                            mData.getFabulousSize() - 1);
                    updateData(mData);
                }

                @Override
                public void showError(@StringRes int str) {
                    BaseApplication.showToast(str);
                }

                @Override
                public void showLoading() {

                }

                @Override
                public void setPresenter(FabulousContract.Presenter presenter) {
                    ViewHolder.this.mFabulousPresenter = presenter;
                }
            });
        }

        @Override
        protected void onBind(FriendCircleCard friendCircleCard) {
            mPortrait.setUp(Glide.with(FriendCircleActivity.this), friendCircleCard.getHead());
            mTitle.setText(friendCircleCard.getTitle());
            mContent.setText(friendCircleCard.getContent());
            mDate.setText(DateTimeUtil.getSimpleDate(friendCircleCard.getCreateAt()));
            mFabulous.setText(String.valueOf(friendCircleCard.getFabulousSize()));
            mComment.setText(String.valueOf(friendCircleCard.getCommentSize()));
            Drawable drawable = getResources().getDrawable(friendCircleCard.isFabulous() ?
                    R.drawable.ic_select_fabulou : R.drawable.ic_fabulous);
            mImageFabulous.setImageDrawable(drawable);
            String[] imgs = friendCircleCard.getImgs().split(",");
            if (imgs.length == 2 || imgs.length == 4) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(FriendCircleActivity.this, 2));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(FriendCircleActivity.this, 3));
            }
            listener = new RecyclerAdapter.AdapterListenerImpl<String>() {
                @Override
                public void onItemClick(RecyclerAdapter.ViewHolder holder, String s) {
                    BigPictureActivity.show(FriendCircleActivity.this, ((ImageHolder)holder).mContent, s);
                }
            };
            mRecyclerView.setAdapter(new RecyclerAdapter<String>(CollectionUtil.toArrayList(imgs)
                    , listener) {
                @Override
                protected ViewHolder<String> onCreateViewHolder(View root, int viewType) {
                    return new ImageHolder(root);
                }

                @Override
                protected int getItemViewType(int position, String s) {
                    return R.layout.cell_image;
                }
            });
        }

        @OnClick({R.id.txt_fabulous, R.id.im_fabulous})
        void onFabulousClick() {
            boolean isFabulous = !mData.isFabulous();
            Drawable drawable = getResources().getDrawable(isFabulous ?
                    R.drawable.ic_select_fabulou : R.drawable.ic_fabulous);
            mImageFabulous.setImageDrawable(drawable);
            //点赞
            mFabulousPresenter.fabulous(mData.getId());
            //设置本地数据点赞图片更改与点赞数量更改，更新数据
        }

        @OnClick({R.id.im_portrait})
        void onPortraitClick() {
            PersonalActivity.show(FriendCircleActivity.this, mData.getReleaseId());
        }

        //评论
        @OnClick({R.id.txt_comment, R.id.im_comment})
        void onCommentClick() {

            final PopupWindow popupWindow = new PopupWindow();
            View view =
                    LayoutInflater.from(FriendCircleActivity.this).inflate(R.layout.friend_circle_popup, null);
            final EditText mContent = view.findViewById(R.id.edit_content);
            final TextView mRelease = view.findViewById(R.id.txt_release);
            popupWindow.setContentView(view);
            mContent.addTextChangedListener(new TextWatcherAdapter() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s)) {
                        mRelease.setEnabled(false);
                        mRelease.setBackgroundDrawable(getDrawable(R.drawable.btn_grey));
                        mRelease.setTextColor(Color.parseColor("#bdbdbd"));
                    } else {
                        mRelease.setEnabled(true);
                        mRelease.setBackgroundDrawable(getDrawable(R.drawable.btn_green));
                        mRelease.setTextColor(Color.parseColor("#ffffff"));
                    }
                }
            });
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            //设置SelectPicPopupWindow弹出窗体的高
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            //设置SelectPicPopupWindow弹出窗体可点击
            popupWindow.setFocusable(true);
            //设置SelectPicPopupWindow弹出窗体动画效果
            popupWindow.setAnimationStyle(R.style.PopupWindowToTopAnim);
            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            //设置SelectPicPopupWindow弹出窗体的背景
            popupWindow.setBackgroundDrawable(dw);
            popupWindow.showAtLocation(getWindow().getDecorView(),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            //开启软件盘
            KeyBoardUtil.showKeyBoard(mContent);
            mRelease.setOnClickListener(v -> {
                //关闭软件盘
                Util.hideKeyboard(mContent);
                popupWindow.dismiss();
                mPresenter.comment(mData.getId(), mContent.getText().toString());
            });

        }

        @Override
        public void showError(@StringRes int str) {
            BaseApplication.showToast(str);
        }

        @Override
        public void showLoading() {

        }

        @Override
        public void setPresenter(CommentContract.Presenter presenter) {
            this.mPresenter = presenter;
        }

        @Override
        public void onCommentDone() {
            mData.setCommentSize(mData.getCommentSize() + 1);
            updateData(mData);
            BaseApplication.showToast(R.string.label_comment_success);
        }

    }

    class ImageHolder extends RecyclerAdapter.ViewHolder<String> {

        @BindView(R.id.im_content)
        ImageView mContent;

        public ImageHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(String s) {
            Glide.with(FriendCircleActivity.this).asBitmap().load(s).placeholder(R.drawable.default_banner_group).into(mContent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mPresenter.friendCircle();
    }
}
