package com.hyd.common.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.hyd.common.factory.presenter.BaseContract;
import com.hyd.htalker.common.R;

/**
 * Created by hydCoder on 2019/11/1.
 * 以梦为马，明日天涯。
 */
public abstract class PresenterToolbarActivity<Presenter extends BaseContract.Presenter> extends ToolbarActivity implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;
    protected ProgressDialog mLoadingDialog;

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
        hideLoadingDialog();

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
        } else {
            if (mLoadingDialog == null) {
                mLoadingDialog = new ProgressDialog(this, R.style.AppTheme_Dialog_Alert_Light);
                // 不可触摸取消
                mLoadingDialog.setCanceledOnTouchOutside(false);
                // 如果强制取消就关闭当前界面
                mLoadingDialog.setCancelable(true);
                mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        finish();
                    }
                });
            }
            mLoadingDialog.setMessage(getText(R.string.prompt_loading));
            mLoadingDialog.show();
        }
    }

    protected void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public void hideLoading() {
        hideLoadingDialog();

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
