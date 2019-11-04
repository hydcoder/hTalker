package com.hyd.htalker.factory.presenter.search;

import com.hyd.common.factory.presenter.BasePresenter;

/**
 * 搜索群的逻辑实现
 * Created by hydCoder on 2019/11/4.
 * 以梦为马，明日天涯。
 */
public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView> implements SearchContract.Presenter {

    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
