package com.hyd.htalker.factory.presenter;

import com.hyd.common.factory.data.DataSource;
import com.hyd.common.factory.data.DbDataSource;
import com.hyd.common.factory.presenter.BaseContract;
import com.hyd.common.factory.presenter.BaseRecyclerPresenter;

import java.util.List;

/**
 * 基础的仓库源的Presenter定义
 * Created by hydCoder on 2019/11/7.
 * 以梦为马，明日天涯。
 */
public abstract class BaseSourcePresenter<Data, ViewModel, Source extends DbDataSource<Data>,
        View extends BaseContract.RecyclerView>
        extends BaseRecyclerPresenter<ViewModel, View>
        implements DataSource.SucceedCallback<List<Data>> {

    protected Source mSource;

    public BaseSourcePresenter(Source source, View view) {
        super(view);
        this.mSource = source;
    }

    @Override
    public void start() {
        super.start();
        if (mSource != null)
            mSource.load(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        mSource.dispose();
        mSource = null;
    }
}
