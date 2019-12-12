package com.hyd.htalker.frags.panel;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.hyd.common.common.widget.recycler.RecyclerAdapter;
import com.hyd.common.face.Face;
import com.hyd.htalker.R;

import butterknife.BindView;

/**
 * Created by hydCoder on 2019/12/11.
 * 以梦为马，明日天涯。
 */
public class FaceHolder extends RecyclerAdapter.ViewHolder<Face.Bean> {

    @BindView(R.id.im_face)
    ImageView mFace;

    public FaceHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(Face.Bean bean) {
        if (bean != null &&
                // drawable 资源id
                ((bean.preview instanceof Integer)
                        // face zip 包资源路径
                        || bean.preview instanceof String)) {
            Glide.with(mFace.getContext())
                    .asBitmap()
                    .load(bean.preview)
                    .format(DecodeFormat.PREFER_ARGB_8888)  //设置解码的格式8888，保证清晰度
                    .into(mFace);
        }
    }
}
