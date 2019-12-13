package com.hyd.htalker.frags.panel;


import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hyd.common.common.app.BaseFragment;
import com.hyd.common.common.tools.UiTool;
import com.hyd.common.common.widget.GalleryView;
import com.hyd.common.common.widget.recycler.RecyclerAdapter;
import com.hyd.common.face.Face;
import com.hyd.htalker.R;

import net.qiujuer.genius.ui.Ui;

import java.io.File;
import java.util.List;

/**
 * 聊天界面的底部面板
 */
public class PanelFragment extends BaseFragment {

    private PanelCallback mCallback;
    private View mFacePanel, mRecordPanel, mGalleryPanel;

    public PanelFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_panel;
    }

    public void setUp(PanelCallback callback) {
        mCallback = callback;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        initFace(root);
        initRecord(root);
        initMore(root);
    }

    // 初始化表情
    @SuppressWarnings("ConstantConditions")
    private void initFace(View root) {
        mFacePanel = root.findViewById(R.id.lay_panel_face);

        View backSpace = mFacePanel.findViewById(R.id.im_backspace);
        backSpace.setOnClickListener(v -> {
            // 删除表情逻辑
            if (mCallback == null) {
                return;
            }

            // 模拟一个键盘删除点击
            KeyEvent event = new KeyEvent(0,0,0,KeyEvent.KEYCODE_DEL, 0,0,0,0,KeyEvent.KEYCODE_ENDCALL);
            mCallback.getInputEditText().dispatchKeyEvent(event);
        });

        TabLayout tabLayout = mFacePanel.findViewById(R.id.tab);
        ViewPager viewPager = mFacePanel.findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);

        // 每一个表情显示大小为48dp
        final int minFaceSize = (int) Ui.dipToPx(getResources(), 48);
        final int TotalScreen = UiTool.getScreenWidth(getActivity());
        final int spanCount = TotalScreen / minFaceSize;

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Face.all(getContext()).size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                // 添加
                LayoutInflater inflater = LayoutInflater.from(getContext());
                RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.lay_face_content, container, false);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));

                // 设置adapter
                List<Face.Bean> faces = Face.all(getContext()).get(position).faces;
                FaceAdapter adapter = new FaceAdapter(faces, new RecyclerAdapter.AdapterListenerImpl<Face.Bean>() {
                    @Override
                    public void onItemClick(RecyclerAdapter.ViewHolder holder, Face.Bean bean) {
                        if (mCallback != null) {
                            // 添加表情到输入框
                            EditText editText = mCallback.getInputEditText();
                            Face.inputFace(getContext(), editText.getText(), bean,
                                    (int) (editText.getTextSize() + Ui.dipToPx(getResources(), 2)));
                        }
                    }
                });

                recyclerView.setAdapter(adapter);
                container.addView(recyclerView);
                return recyclerView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position,
                                    @NonNull Object object) {
                // 移除
                container.removeView((View) object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return Face.all(getContext()).get(position).name;
            }
        });
    }

    // 初始化语音
    private void initRecord(View root) {

    }

    // 初始化图片
    private void initMore(View root) {
        mGalleryPanel = root.findViewById(R.id.lay_gallery_panel);
        GalleryView galleryView = mGalleryPanel.findViewById(R.id.view_gallery);
        TextView selectedSize = mGalleryPanel.findViewById(R.id.txt_gallery_select_count);

        galleryView.setup(LoaderManager.getInstance(this), count -> {
            String resStr = getText(R.string.label_gallery_selected_size).toString();
            selectedSize.setText(String.format(resStr, count));
        });

        mGalleryPanel.findViewById(R.id.btn_send).setOnClickListener(v ->
                onGallerySendClick(galleryView, galleryView.getSelectedPath()));
    }

    // 点击发送按钮是触发，传回一个GalleryView和选中的路径
    private void onGallerySendClick(GalleryView galleryView, String[] paths) {
        // 通知给聊天界面发送图片
        if (mCallback == null) {
            return;
        }
        mCallback.onSendGallery(paths);
        // 清理状态
        galleryView.clear();
    }

    public void showFace() {
        mFacePanel.setVisibility(View.VISIBLE);
//        mRecordPanel.setVisibility(View.GONE);
        mGalleryPanel.setVisibility(View.GONE);
    }

    public void showRecord() {
        mFacePanel.setVisibility(View.GONE);
//        mRecordPanel.setVisibility(View.VISIBLE);
        mGalleryPanel.setVisibility(View.GONE);
    }

    public void showMore() {
        mFacePanel.setVisibility(View.GONE);
//        mRecordPanel.setVisibility(View.GONE);
        mGalleryPanel.setVisibility(View.VISIBLE);
    }

    // 回调聊天界面的callback
    public interface PanelCallback {
        EditText getInputEditText();

        // 返回需要发送的图片
        void onSendGallery(String[] paths);

        // 返回录音文件和时长
        void onRecordDone(File file, long time);
    }
}
