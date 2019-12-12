package com.hyd.common.common.widget.recycler;

/**
 * Created by hydCoder on 2019/10/17.
 * 以梦为马，明日天涯。
 */
public interface AdapterCallBack<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
