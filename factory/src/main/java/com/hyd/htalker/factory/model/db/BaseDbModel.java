package com.hyd.htalker.factory.model.db;

import com.hyd.htalker.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 我们APP中的基础的一个BaseDbModel，
 * 继承了数据库框架DbFlow中的基础类
 * 同时定义了我们需要的方法
 * Created by hydCoder on 2019/11/7.
 * 以梦为马，明日天涯。
 */
public abstract class BaseDbModel<Model> extends BaseModel
        implements DiffUiDataCallback.UiDataDiff<Model> {
}
