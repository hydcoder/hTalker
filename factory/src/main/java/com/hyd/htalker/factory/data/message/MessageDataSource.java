package com.hyd.htalker.factory.data.message;

import com.hyd.common.factory.data.DbDataSource;
import com.hyd.htalker.factory.model.db.Message;


/**
 * 消息的数据源定义，他的实现是MessageRepository
 * 关注的对象是message表
 *
 * Created by hydCoder on 2019/11/20.
 * 以梦为马，明日天涯。
 */
public interface MessageDataSource extends DbDataSource<Message> {

}
