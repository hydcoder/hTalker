package com.hyd.htalker.frags.search;


import com.hyd.common.app.BaseFragment;
import com.hyd.htalker.R;
import com.hyd.htalker.activities.SearchActivity;

/**
 * 搜索群的fragment
 */
public class SearchGroupFragment extends BaseFragment implements SearchActivity.SearchFragment {


    public SearchGroupFragment() {
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {

    }
}
