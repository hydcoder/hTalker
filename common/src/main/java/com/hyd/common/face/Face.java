package com.hyd.common.face;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.ArrayMap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.hyd.common.utils.StreamUtil;
import com.hyd.htalker.common.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 表情工具类
 * Created by hydCoder on 2019/12/11.
 * 以梦为马，明日天涯。
 */
public class Face {

    // 全局的表情的映射ArrayMap，更加轻量级
    private static final ArrayMap<String, Bean> FACE_MAP = new ArrayMap<>();
    private static List<FaceTab> FACE_TABS = null;

    private static void init(Context context) {
        if (FACE_TABS == null) {
            synchronized (Face.class) {
                if (FACE_TABS == null) {
                    ArrayList<FaceTab> faceTabs = new ArrayList<>();
                    FaceTab tab = initAssetsFace(context);
                    if (tab != null) {
                        faceTabs.add(tab);
                    } else {
                        tab = initResourceFace(context);
                        if (tab != null) {
                            faceTabs.add(tab);
                        }
                    }

                    // 初始化map
                    for (FaceTab faceTab : faceTabs) {
                        faceTab.copyToMap(FACE_MAP);
                    }

                    // 初始化list为不可变的集合
                    FACE_TABS = Collections.unmodifiableList(faceTabs);
                }
            }
        }
    }

    // 从Assets目录下的face-t.zip包中解析表情
    private static FaceTab initAssetsFace(Context context) {
        String faceAsset = "face-t.zip";
        // data/data/包名/files/face/ft/*
        String faceCacheDir = String.format("%s/face/ft", context.getFilesDir());
        File faceFolder = new File(faceCacheDir);
        if (!faceFolder.exists()) {
            // 不存在进行初始化
            if (faceFolder.mkdirs()) {
                try {
                    InputStream inputStream = context.getAssets().open(faceAsset);
                    // 存储文件
                    File faceSource = new File(faceFolder, "source.zip");

                    // copy
                    StreamUtil.copy(inputStream, faceSource);

                    // 解压
                    unZipFile(faceSource, faceFolder);

                    // 清理文件
                    StreamUtil.delete(faceSource.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 找到info.json
        File infoFile = new File(faceCacheDir, "info.json");

        Gson gson = new Gson();
        JsonReader reader;
        try {
            reader = gson.newJsonReader(new FileReader(infoFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        // 解析
        FaceTab tab = gson.fromJson(reader, FaceTab.class);

        // 相对路径转换到绝对路径
        for (Bean face : tab.faces) {
            face.preview = String.format("%s/%s", faceCacheDir, face.preview);
            face.source = String.format("%s/%s", faceCacheDir, face.source);
        }

        return tab;
    }

    // 解压文件到指定目录
    private static void unZipFile(File zipFile, File desDir) throws IOException {
        String folderPath = desDir.getAbsolutePath();

        ZipFile zf = new ZipFile(zipFile);
        // 判断节点进行循环
        for (Enumeration<? extends ZipEntry> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = entries.nextElement();
            // 过滤缓存的文件
            String name = entry.getName();
            if (name.startsWith(".")) {
                continue;
            }

            // 输入流
            InputStream inputStream = zf.getInputStream(entry);
            String str = folderPath + File.separator + name;

            // 防止名字错乱
            str = new String(str.getBytes("8859_1"), "GB2312");

            File desFile = new File(str);
            // 输出文件
            StreamUtil.copy(inputStream, desFile);
        }
    }

    // 从drawable资源中加载数据并映射到对应key
    private static FaceTab initResourceFace(Context context) {
        ArrayList<Bean> faces = new ArrayList<>();
        Resources resources = context.getResources();
        String packageName = context.getApplicationInfo().packageName;
        for (int i = 1; i <= 142; i++) {
            // %03d  i=1 ==> 001
            String key = String.format(Locale.ENGLISH, "fb_%03d", i);
            String resStr = String.format(Locale.ENGLISH, "face_base_%03d", i);

            // 根据资源名称去拿资源对应的id
            int resId = resources.getIdentifier(resStr, "drawable", packageName);
            if (resId == 0) {
                continue;
            }
            faces.add(new Bean(key, resId));
        }

        if (faces.size() == 0) {
            return null;
        }
        return new FaceTab("NAME", faces.get(0).preview, faces);
    }

    /**
     * 索取所有的表情
     *
     * @param context 上下文，不允许为空
     * @return 所有的表情
     */
    public static List<FaceTab> all(@NonNull Context context) {
        init(context);
        return FACE_TABS;
    }

    /**
     * 输入表情到editable
     *
     * @param context  上下文，不允许为空
     * @param editable 输入框
     * @param bean     要输入的表情的bean
     * @param size     表情的显示大小
     */
    public static void inputFace(@NonNull final Context context, final Editable editable,
                                 final Face.Bean bean, int size) {
        Glide.with(context).asBitmap().load(bean.preview).into(new SimpleTarget<Bitmap>(size,
                size) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<?
                    super Bitmap> transition) {
                Spannable spannable = new SpannableString(String.format("[%s]", bean.key));
                ImageSpan imageSpan = new ImageSpan(context, resource, ImageSpan.ALIGN_BASELINE);
                // 前后不关联
                spannable.setSpan(imageSpan, 0, spannable.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editable.append(spannable);
            }
        });
    }

    /**
     * 从Spannable中解析表情并替换现实
     *
     * @param view      要显示的view
     * @param spannable spannable
     * @param size      表情的显示大小
     */
    public static void decode(@NonNull View view, final Spannable spannable, int size) {
        if (spannable == null) {
            return;
        }

        String str = spannable.toString();
        if (TextUtils.isEmpty(str)) {
            return;
        }

        final Context context = view.getContext();

        // 进行正则匹配 [][][]
        Pattern pattern = Pattern.compile("(\\[[^\\[\\]:\\s\\n]+\\])");
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            // [ft112]
            String key = matcher.group();
            if (TextUtils.isEmpty(key)) {
                continue;
            }

            final Bean bean = get(context, key.replace("[", "").replace("]", ""));
            if (bean == null) {
                continue;
            }

            int start = matcher.start();
            int end = matcher.end();

            // 得到一个复写后的imageSpan
            FaceSpan span = new FaceSpan(context, view, bean.preview, size);

            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    // 拿一个bean
    public static Bean get(Context context, String key) {
        init(context);
        if (FACE_MAP.containsKey(key)) {
            return FACE_MAP.get(key);
        }
        return null;
    }

    public static class FaceSpan extends ImageSpan {

        // 自己真实绘制的drawable
        private Drawable mDrawable;
        private View mView;
        private int mSize;

        /**
         * 构造
         *
         * @param context 上下文
         * @param view    目标view，用于加载完成时刷新实用
         * @param source  加载的目标表情
         * @param size    图片的显示大小
         */
        public FaceSpan(@NonNull Context context, View view, Object source, final int size) {
            super(context, R.drawable.default_face, ALIGN_BOTTOM);
            mView = view;
            mSize = size;

            Glide.with(context).load(source).fitCenter().into(new SimpleTarget<Drawable>(size,
                    size) {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<?
                        super Drawable> transition) {
                    mDrawable = resource.getCurrent();
                    // 获取自测量宽高
                    int width = mDrawable.getIntrinsicWidth();
                    int height = mDrawable.getIntrinsicHeight();

                    // 重新设置
                    mDrawable.setBounds(0, 0, width > 0 ? width : size, height > 0 ? height : size);

                    // 刷新
                    mView.invalidate();
                }
            });
        }

        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end,
                           @Nullable Paint.FontMetricsInt fm) {
            // 重写测量方法, 主要是为了防止mDrawable等于null时报错
            Rect rect = mDrawable != null ? mDrawable.getBounds() : new Rect(0, 0, mSize, mSize);

            if (fm != null) {
                fm.ascent = -rect.bottom;
                fm.descent = 0;

                fm.top = fm.ascent;
                fm.bottom = 0;
            }

            return rect.right;
        }

        @Override
        public Drawable getDrawable() {
            // 复写拿drawable的方法，这里有可能mDrawable还是null
            return mDrawable;
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x,
                         int top, int y, int bottom, @NonNull Paint paint) {
            // 复写后增加判空
            if (mDrawable != null) {
                super.draw(canvas, text, start, end, x, top, y, bottom, paint);
            }
        }
    }

    /**
     * 每一个表情盘，拥有很多表情
     */
    public static class FaceTab {
        public List<Bean> faces;
        public String name;
        // 预览图,包括了drawable下面的int资源类型表情
        Object preview;

        FaceTab(String name, Object preview, List<Bean> faces) {
            this.faces = faces;
            this.name = name;
            this.preview = preview;
        }

        void copyToMap(ArrayMap<String, Bean> faceMap) {
            for (Bean face : faces) {
                faceMap.put(face.key, face);
            }
        }
    }

    /**
     * 每一个表情
     */
    public static class Bean {

        Bean(String key, int preview) {
            this.key = key;
            this.preview = preview;
            this.source = preview;
        }

        String key;
        Object source;
        public Object preview;
        public String desc;
    }
}
