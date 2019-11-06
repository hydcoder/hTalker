package com.hyd.htalker.factory.utils;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

/**
 * Created by hydCoder on 2019/11/6.
 * 以梦为马，明日天涯。
 */
public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiff> extends DiffUtil.Callback {

    private List<T> mOldList, mNewList;

    public DiffUiDataCallback(List<T> mOldList, List<T> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        // 旧的数据大小
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        // 新的数据大小
        return mNewList.size();
    }

    // 两个类是否就是同一个东西，比如id相等的user
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);
        return beanNew.isSame(beanOld);
    }

    // 在经过相等判断后，进一步判断是否有数据更改
    // 比如，同一个用户的两个不同实例，其中的name字段不同
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);
        return beanNew.isUiContentSame(beanOld);
    }

    /**
     * 进行数据比较的真正实现由T继承该接口去实现
     * @param <T> 进行比较的相同的数据类型，适用于所有数据类型
     */
    public interface UiDataDiff<T> {
        // 传递一个旧的数据给你，问你是否和你标识的是同一个数据
        boolean isSame(T old);

        // 和传递进来的旧数据对比，内容是否相同
        boolean isUiContentSame(T old);
    }
}
