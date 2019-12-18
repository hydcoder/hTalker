package com.hyd.common.widget.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyd.htalker.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hydCoder on 2019/10/17.
 * 以梦为马，明日天涯。
 */
public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>> implements View.OnClickListener, View.OnLongClickListener, AdapterCallBack<Data> {

    private final List<Data> mDataList;
    private AdapterListener<Data> mListener;

    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
        mDataList = dataList;
        mListener = listener;
    }

    /**
     * 复写默认的布局类型返回
     *
     * @param position 角标
     * @return 类型，其实复写后返回的都是XML布局文件的id
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 得到的布局类型
     *
     * @param position 角标
     * @param data     当前item的数据
     * @return XML布局文件的id，用于创建ViewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    /**
     * 创建一个ViewHolder
     *
     * @param parent   RecyclerView
     * @param viewType 界面的类型,约定为XML布局的Id
     * @return
     */
    @NonNull
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 通过LayoutInflater把XML布局的Id为viewType的布局文件初始化为root
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(viewType, parent, false);
        // 通过子类必须实现的方法，得到一个ViewHolder
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);

        // 设置事件点击
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        // 设置view的tag为viewHolder，从而实现双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);

        // 进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);
        // 绑定callback
        holder.mCallBack = this;

        return holder;
    }

    /**
     * 得到一个新的ViewHolder
     *
     * @param root     根布局
     * @param viewType 界面的类型,其实 为XML布局的Id
     * @return
     */
    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    /**
     * 绑定数据到holder上
     *
     * @param holder   要绑定的holder
     * @param position 角标
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> holder, int position) {
        // 得到需要绑定的数据
        Data data = mDataList.get(position);
        // 触发holder的绑定数据的方法
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 获取数据集
     *
     * @return List<Data>
     */
    public List<Data> getDataList() {
        return mDataList;
    }

    /**
     * 插入一条数据并通知插入更新
     *
     * @param data 插入的数据
     */
    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 移除一条数据
     * @param index 角标
     */
    public void remove(int index) {
        mDataList.remove(index);
        notifyItemRemoved(index);
    }

    /**
     * 插入一堆数据并通知插入更新
     *
     * @param dataList 插入的数据集
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 删除操作
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为一个新的集合，其中包括了清空
     *
     * @param dataList 一个新的集合
     */
    public void replaceData(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0) return;
        mDataList.addAll(dataList);
        notifyDataSetChanged();

    }

    /**
     * 插入一堆数据并通知插入更新
     *
     * @param dataList 插入的数据集合
     */
    public void add(List<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    @Override
    public void update(Data data, ViewHolder<Data> holder) {
        // 得到当前ViewHolder的坐标
        int pos = holder.getAdapterPosition();
        if (pos >= 0) {
            // 进行数据的移除与更新
            mDataList.remove(pos);
            mDataList.add(pos, data);
            // 通知这个坐标下的数据有更新
            notifyItemChanged(pos);
        }
    }

    @Override
    public void onClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (mListener != null) {
            int pos = holder.getAdapterPosition();
            mListener.onItemClick(holder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (mListener != null) {
            int pos = holder.getAdapterPosition();
            mListener.onItemClick(holder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 设置监听器
     *
     * @param listener 实现了的监听器
     */
    public void setListener(AdapterListener<Data> listener) {
        this.mListener = listener;
    }

    /**
     * 当前Adapter的监听器
     *
     * @param <Data> 泛型
     */
    public interface AdapterListener<Data> {
        // 当点击的时候回调
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);

        // 当长按的时候回调
        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
    }

    /**
     * 自定义的ViewHolder
     *
     * @param <Data> 数据泛型类型
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {

        private Unbinder unbinder;
        protected Data mData;
        private AdapterCallBack<Data> mCallBack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 绑定的数据
         */
        void bind(Data data) {
            mData = data;
            onBind(data);
        }

        /**
         * 当触发绑定数据时候的回调，子类必须复写，实现具体绑定逻辑
         *
         * @param data 绑定的数据
         */
        protected abstract void onBind(Data data);

        /**
         * holder对自己对应的数据进行更新操作
         *
         * @param data 要更新的数据
         */
        public void updateData(Data data) {
            if (mCallBack != null) {
                mCallBack.update(data, this);
            }
        }
    }

    /**
     * 对回调接口做一次实现AdapterListener
     *
     * @param <Data>
     */
    public static abstract class AdapterListenerImpl<Data> implements AdapterListener<Data> {

        @Override
        public void onItemClick(ViewHolder holder, Data data) {

        }

        @Override
        public void onItemLongClick(ViewHolder holder, Data data) {

        }
    }
}
