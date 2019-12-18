package com.hyd.htalker.frags.main;

import com.hyd.common.app.BaseFragment;
import com.hyd.htalker.R;
import com.hyd.htalker.activities.FriendCircleActivity;

import butterknife.OnClick;

/**
 * 动态的fragment
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class DynamicFragment extends BaseFragment {

    public DynamicFragment() {
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @OnClick(R.id.layout_dynamic)
    void onDynamicClick(){
        FriendCircleActivity.show(getContext());
    }
}
