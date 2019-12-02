package com.hyd.htalker.factory.presenter.search;

import com.hyd.common.factory.data.DataSource;
import com.hyd.common.factory.presenter.BasePresenter;
import com.hyd.htalker.factory.data.helper.GroupHelper;
import com.hyd.htalker.factory.data.helper.UserHelper;
import com.hyd.htalker.factory.model.card.GroupCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/**
 * 搜索群的逻辑实现
 * Created by hydCoder on 2019/11/4.
 * 以梦为马，明日天涯。
 */
public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView>
        implements SearchContract.Presenter, DataSource.Callback<List<GroupCard>> {

    private Call searchCall;

    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();

        Call call = searchCall;
        if (call != null && !call.isCanceled()) {
            // 如果有上一次的请求，并且没有取消
            // 则调用取消请求操作
            call.cancel();
        }
        searchCall = GroupHelper.searchGroup(content, this);
    }

    @Override
    public void onDataLoaded(final List<GroupCard> groupCards) {
        final SearchContract.GroupView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onSearchDone(groupCards);
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final SearchContract.GroupView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
