package com.hyd.common.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hydCoder on 2019/10/16.
 * 以梦为马，明日天涯。
 */
public abstract class BaseFragment extends Fragment {

    private View mRoot;
    private Unbinder mUnbinder;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            // 初始化当前的根布局，但是不在创建时就添加到container中
            mRoot = inflater.inflate(getContentLayoutId(), container, false);
            initWidget();
        } else {
            if (mRoot.getParent() != null) {
                // 把当前mRoot从其父控件中移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    /**
     * 获取根布局文件id
     *
     * @return 根布局文件id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget() {
        mUnbinder = ButterKnife.bind(this, mRoot);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 初始化相关参数
     */
    protected void initArgs(Bundle bundle) {

    }

    /**
     * 返回按键触发时调用
     * @return 返回true代表fragment已经处理了返回逻辑，不需要activity去finish
     * 返回false，代表不拦截，activity走自己的逻辑
     */
    public boolean onBackPressed() {
        return false;
    }
}
