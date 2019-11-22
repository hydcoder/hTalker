package com.hyd.htalker.factory.presenter.message;

import androidx.recyclerview.widget.DiffUtil;

import com.hyd.htalker.factory.data.message.SessionDataSource;
import com.hyd.htalker.factory.data.message.SessionRepository;
import com.hyd.htalker.factory.model.db.Session;
import com.hyd.htalker.factory.presenter.BaseSourcePresenter;
import com.hyd.htalker.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 最近聊天列表的presenter
 * <p>
 * Created by hydCoder on 2019/11/22.
 * 以梦为马，明日天涯。
 */
public class SessionPresenter extends BaseSourcePresenter<Session, Session, SessionDataSource,
        SessionContract.View> implements SessionContract.Presenter {

    public SessionPresenter(SessionContract.View view) {
        super(new SessionRepository(), view);
    }

    @Override
    public void onDataLoaded(List<Session> sessions) {
        SessionContract.View view = getView();
        if (view == null) {
            return;
        }

        // 对比差异
        List<Session> oldList = view.getRecyclerAdapter().getDataList();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(oldList,
                sessions);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);

        // 刷新到界面
        refreshData(diffResult, sessions);
    }
}
