package com.hyd.htalker.factory.presenter.group;

import android.text.TextUtils;

import com.hyd.common.factory.data.DataSource;
import com.hyd.common.factory.presenter.BaseRecyclerPresenter;
import com.hyd.htalker.factory.Factory;
import com.hyd.htalker.factory.R;
import com.hyd.htalker.factory.data.helper.GroupHelper;
import com.hyd.htalker.factory.data.helper.UserHelper;
import com.hyd.htalker.factory.model.api.group.GroupCreateModel;
import com.hyd.htalker.factory.model.card.GroupCard;
import com.hyd.htalker.factory.model.db.UserSimpleModel;
import com.hyd.htalker.factory.net.UploadHelper;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 群创建的presenter
 * Created by hydCoder on 2019/11/28.
 * 以梦为马，明日天涯。
 */
public class GroupCreatePresenter extends BaseRecyclerPresenter<GroupCreateContract.ViewModel, GroupCreateContract.View> implements GroupCreateContract.Presenter, DataSource.Callback<GroupCard> {

    private Set<String> userIds = new HashSet<>();

    public GroupCreatePresenter(GroupCreateContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();

        // 加载联系人数据
        Factory.runOnAsync(loader);
    }

    @Override
    public void create(final String name, final String desc, final String portrait) {
        GroupCreateContract.View view = getView();
        view.showLoading();

        // 判断参数
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc)
            || TextUtils.isEmpty(portrait) || userIds.size() == 0) {
            view.showError(R.string.label_group_create_invalid);
            return;
        }

        // 上传图片
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = uploadPicture(portrait);
                if (TextUtils.isEmpty(url)) {
                    return;
                }
                GroupCreateModel model = new GroupCreateModel(name, desc, portrait, userIds);

                GroupHelper.create(model, GroupCreatePresenter.this);
            }
        });
    }

    @Override
    public void changeSelect(GroupCreateContract.ViewModel viewModel, boolean isSelected) {
        if (isSelected) {
            userIds.add(viewModel.author.getId());
        } else {
            userIds.remove(viewModel.author.getId());
        }
    }

    // 同步上传图片
    private String uploadPicture(String path) {
        String url = UploadHelper.uploadImage(path);
        if (TextUtils.isEmpty(url)) {
            // 切换到UI线程，提示信息
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    GroupCreateContract.View view = getView();
                    if (view != null) {
                        view.showError(R.string.data_upload_error);
                    }
                }
            });
        }
        return url;
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            List<UserSimpleModel> simpleContacts = UserHelper.getSimpleContact();
            List<GroupCreateContract.ViewModel> models = new ArrayList<>();
            for (UserSimpleModel simpleContact : simpleContacts) {
                GroupCreateContract.ViewModel viewModel = new GroupCreateContract.ViewModel();
                viewModel.author = simpleContact;
                models.add(viewModel);
            }

            refreshData(models);
        }
    };

    @Override
    public void onDataLoaded(GroupCard groupCard) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                GroupCreateContract.View view = getView();
                if (view != null) {
                    view.onCreateSucceed();
                }
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                GroupCreateContract.View view = getView();
                if (view != null) {
                    view.showError(strRes);
                }
            }
        });
    }
}
