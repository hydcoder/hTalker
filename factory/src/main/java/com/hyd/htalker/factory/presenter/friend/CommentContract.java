package com.hyd.htalker.factory.presenter.friend;

import com.hyd.common.factory.presenter.BaseContract;

/**
 * 点赞评论契约
 * Created by hydCoder on 2019/12/18.
 * 以梦为马，明日天涯。
 */
public class CommentContract {

    public interface Presenter extends BaseContract.Presenter{
        //评论请求
        void comment(String friendCircleId,String content);
    }

    //用户界面
    public interface CommentView extends BaseContract.View<Presenter>{
        //评论成功
        void onCommentDone();
    }
}
