package com.hyd.htalker.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;

import com.hyd.common.common.app.BaseFragment;
import com.hyd.common.common.app.ToolbarActivity;
import com.hyd.htalker.R;
import com.hyd.htalker.frags.search.SearchGroupFragment;
import com.hyd.htalker.frags.search.SearchUserFragment;

/**
 * Created by hydCoder on 2019/11/1.
 * 以梦为马，明日天涯。
 */
public class SearchActivity extends ToolbarActivity {

    private static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final int TYPE_USER = 1;   // 搜索用户
    public static final int TYPE_GROUP = 2;   // 搜索群

    // 具体需要显示的类型
    private int type;

    private SearchFragment mSearchFragment;

    /**
     * 显示搜索界面
     * @param context 上下文
     * @param type 搜索的类型，用户还是群
     */
    public static void show(Context context, int type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        type = bundle.getInt(EXTRA_TYPE);
        return type == TYPE_USER || type == TYPE_GROUP;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // 显示对应的fragment
        BaseFragment fragment;
        if (type == TYPE_USER) {
            SearchUserFragment searchUserFragment = new SearchUserFragment();
            fragment = searchUserFragment;
            mSearchFragment = searchUserFragment;
        } else {
            SearchGroupFragment searchGroupFragment = new SearchGroupFragment();
            fragment = searchGroupFragment;
            mSearchFragment = searchGroupFragment;
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 初始化菜单
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);

        // 找到搜索菜单
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        if (searchView != null) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (TextUtils.isEmpty(newText)) {
                        search("");
                        return true;
                    }
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 搜索的发起点
     * @param query 搜索的内容
     */
    private void search(String query) {
        if (mSearchFragment != null) {
            mSearchFragment.search(query);
        }
    }

    /**
     * 搜索的fragment必须实现的接口
     */
    public interface SearchFragment {
        void search(String content);
    }
}
