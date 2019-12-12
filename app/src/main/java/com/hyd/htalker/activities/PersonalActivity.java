package com.hyd.htalker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.hyd.common.common.app.PresenterToolbarActivity;
import com.hyd.common.common.widget.PortraitView;
import com.hyd.htalker.R;
import com.hyd.htalker.factory.model.db.User;
import com.hyd.htalker.factory.presenter.contact.PersonalContract;
import com.hyd.htalker.factory.presenter.contact.PersonalPresenter;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hydCoder on 2019/11/6.
 * 以梦为马，明日天涯。
 */
public class PersonalActivity extends PresenterToolbarActivity<PersonalContract.Presenter> implements PersonalContract.View {

    private static final String BOUND_KEY_ID = "bound_key_id";

    @BindView(R.id.im_header)
    ImageView mHeader;
    @BindView(R.id.txt_name)
    TextView mName;
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    @BindView(R.id.txt_follows)
    TextView mFollows;
    @BindView(R.id.txt_following)
    TextView mFollowing;
    @BindView(R.id.txt_desc)
    TextView mDesc;
    @BindView(R.id.btn_say_hello)
    Button mSayHello;

    private MenuItem mFollowItem;
    private String userId;
    private boolean mIsFollowUser;

    public static void show(Context context, String userId) {
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtra(BOUND_KEY_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        userId = bundle.getString(BOUND_KEY_ID);
        return !TextUtils.isEmpty(userId);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.personal, menu);
        mFollowItem = menu.findItem(R.id.action_follow);
        changeFollowItemStatus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_follow) {
            // 进行关注操作
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_say_hello)
    public void onClick() {
        // 发起聊天
        User user = mPresenter.getUserPersonal();
        if (user != null) {
            MessageActivity.show(this, user);
        }
    }

    /**
     * 更改关注菜单状态
     */
    private void changeFollowItemStatus() {
        if (mFollowItem == null) {
            return;
        }
        Drawable drawable = mIsFollowUser ? getResources().getDrawable(R.drawable.ic_favorite) : getResources().getDrawable(R.drawable.ic_favorite_border);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, Resource.Color.WHITE);
        mFollowItem.setIcon(drawable);
    }

    @Override
    protected PersonalContract.Presenter initPresenter() {
        return new PersonalPresenter(this);
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void onLoadDone(User user) {
        if (user != null) {
            mPortrait.setUp(Glide.with(this), user);
            mName.setText(user.getName());
            mDesc.setText(user.getDesc());
            mFollows.setText(String.format(getString(R.string.label_follows), String.valueOf(user.getFollows())));
            mFollowing.setText(String.format(getString(R.string.label_following), String.valueOf(user.getFollowing())));
            hideLoading();
        }
    }



    @Override
    public void allowSayHello(boolean isAllow) {
        mSayHello.setVisibility(isAllow ? View.VISIBLE :View.GONE);
    }

    @Override
    public void setFollowStatus(boolean isFollow) {
        mIsFollowUser = isFollow;
        changeFollowItemStatus();
    }
}
