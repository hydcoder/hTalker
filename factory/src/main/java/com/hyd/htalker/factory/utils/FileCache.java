package com.hyd.htalker.factory.utils;

import com.hyd.common.app.BaseApplication;
import com.hyd.common.utils.HashUtil;
import com.hyd.common.utils.StreamUtil;
import com.hyd.htalker.factory.net.Network;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 简单的一个文件缓存，实现文件的下载操作
 * 下载成功后回调相应方法
 * Created by hydCoder on 2019/12/16.
 * 以梦为马，明日天涯。
 */
public class FileCache<Holder> {

    private File baseDir;
    private String ext;
    private CacheListener<Holder> listener;
    // 最后一次要下载的目标， 使用软引用
    private SoftReference<Holder> holderSoftReference;

    public FileCache(String baseDir, String ext, CacheListener<Holder> listener) {
        this.baseDir = new File(BaseApplication.getCacheDirFile(), baseDir);
        this.ext = ext;
        this.listener = listener;
    }

    // 构建一个缓存文件，使用MD5来保证同一个网络路劲对应同一个本地文件
    private File buildCacheFile(String path) {
        String key = HashUtil.getMD5String(path);
        return new File(baseDir, key + "." + ext);
    }

    public void download(Holder holder, String path) {
        // 如果要下载的路径就是本地缓存路径，那么就无需下载
        if (path.startsWith(BaseApplication.getCacheDirFile().getAbsolutePath())) {
            listener.onDownloadSucceed(holder, new File(path));
            return;
        }

        File cacheFile = buildCacheFile(path);
        if (cacheFile.exists() && cacheFile.length() > 0) {
            // 如果文件已经存在，那么无须重新下载
            listener.onDownloadSucceed(holder, cacheFile);
            return;
        }

        // 把目标进行软引用
        holderSoftReference = new SoftReference<>(holder);

        OkHttpClient client = Network.getClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new NetCallback(holder, cacheFile));
    }

    // 拿最后的目标，只能使用一次
    private Holder getLastHolderAndClear(){
        if (holderSoftReference == null) {
            return null;
        } else {
            // 拿并清理
            Holder holder = holderSoftReference.get();
            holderSoftReference.clear();
            return holder;
        }
    }

    private class NetCallback implements Callback {

        private SoftReference<Holder> holderSoftReference;
        private File file;

        public NetCallback(Holder holder, File file) {
            this.holderSoftReference = new SoftReference<>(holder);
            this.file = file;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Holder holder = holderSoftReference.get();
            if (holder != null && holder == getLastHolderAndClear()) {
                // 只有最后一条被点击的语音是有效的
                FileCache.this.listener.onDownloadFailed(holder);
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Holder holder = holderSoftReference.get();
            if (holder != null && holder == getLastHolderAndClear()) {
                InputStream inputStream = response.body().byteStream();
                if (inputStream != null && StreamUtil.copy(inputStream, file)) {
                    // 只有最后一条被点击的语音是有效的
                    FileCache.this.listener.onDownloadSucceed(holder, file);
                } else {
                    onFailure(call, null);
                }
            }
        }
    }


    public interface CacheListener<Holder> {
        void onDownloadSucceed(Holder holder, File file);

        void onDownloadFailed(Holder holder);
    }
}
