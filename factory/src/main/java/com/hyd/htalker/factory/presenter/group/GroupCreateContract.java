package com.hyd.htalker.factory.presenter.group;

import com.hyd.common.factory.model.Author;
import com.hyd.common.factory.presenter.BaseContract;

/**
 * 群创建的契约
 * Created by hydCoder on 2019/11/28.
 * 以梦为马，明日天涯。
 */
public interface GroupCreateContract {

    interface Presenter extends BaseContract.Presenter {

        void create(String name, String desc, String portrait);

        // 更改一个model选中的状态
        void changeSelect(ViewModel viewModel, boolean isSelected);
    }

    interface View extends BaseContract.RecyclerView<Presenter, ViewModel> {
        void onCreateSucceed();
    }

    class ViewModel {
        // 用户信息
        public Author author;
        // 是否选中
        public boolean isSelected;
    }
}
