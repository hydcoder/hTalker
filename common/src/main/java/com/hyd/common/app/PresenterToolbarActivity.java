package com.hyd.common.app;

import com.hyd.common.factory.presenter.BaseContract;

/**
 * Created by hydCoder on 2019/11/1.
 * 以梦为马，明日天涯。
 */
public abstract class PresenterToolbarActivity<Presenter extends BaseContract.Presenter> extends ToolbarActivity implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    @Override
    protected void initBefore() {
        super.initBefore();
        // 初始化presenter
        initPresenter();
    }

    /**
     * 初始化Presenter
     *
     * @return Presenter
     */
    protected abstract Presenter initPresenter();

    @Override
    public void showError(int str) {
        // 显示错误
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(str);
        } else {
            BaseApplication.showToast(str);
        }
    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        }
    }

    public void hideLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerOk();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        // View中赋值Presenter
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }
}
