package com.hyd.htalker.factory.data.helper;

import android.os.SystemClock;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.hyd.common.common.Common;
import com.hyd.common.common.app.BaseApplication;
import com.hyd.common.utils.BitmapUtil;
import com.hyd.common.utils.PicturesCompressor;
import com.hyd.common.utils.StreamUtil;
import com.hyd.htalker.factory.Factory;
import com.hyd.htalker.factory.model.api.RspModel;
import com.hyd.htalker.factory.model.api.message.MsgCreateModel;
import com.hyd.htalker.factory.model.card.MessageCard;
import com.hyd.htalker.factory.model.db.Message;
import com.hyd.htalker.factory.model.db.Message_Table;
import com.hyd.htalker.factory.net.Network;
import com.hyd.htalker.factory.net.RemoteService;
import com.hyd.htalker.factory.net.UploadHelper;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 消息工具类
 * Created by hydCoder on 2019/11/7.
 * 以梦为马，明日天涯。
 */
public class MessageHelper {
    // 从本地找消息
    public static Message findFromLocal(String id) {
        return SQLite.select().from(Message.class).where(Message_Table.id.eq(id)).querySingle();
    }

    public static void push(final MsgCreateModel model) {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                // 成功状态：如果是一个已经发送过的消息，则不能重新发送
                // 正在发送状态：如果是一个正在发送的消息，则不能重新发送
                Message message = findFromLocal(model.getId());
                if (message != null && message.getStatus() != Message.STATUS_FAILED) {
                    return;
                }

                // 在发送的时候需要通知界面更新状态，card
                final MessageCard messageCard = model.buildCard();
                Factory.getMessageCenter().dispatch(messageCard);

                // 发送文件消息分两步：上传文件到云服务器，消息push到服务器
                // 如果是文件类型的(语音、图片、文件)需要先上传后再发送
                if (messageCard.getType() != Message.TYPE_STR) {
                    // 非文字类型
                    if (!messageCard.getContent().startsWith(UploadHelper.ENDPOINT)) {
                        // 没有上传到云服务器的文件
                        String content;

                        switch (messageCard.getType()) {
                            case Message.TYPE_PIC:
                                content = uploadPic(messageCard.getContent());
                                break;
                            case Message.TYPE_AUDIO:
                                content = uploadAudio(messageCard.getContent());
                                break;
                            default:
                                content = "";
                                break;
                        }

                        if (TextUtils.isEmpty(content)) {
                            // 失败
                            messageCard.setStatus(Message.STATUS_FAILED);
                            Factory.getMessageCenter().dispatch(messageCard);
                        } else {
                            // 成功则把本地路径替换成网络路径
                            messageCard.setContent(content);
                            Factory.getMessageCenter().dispatch(messageCard);
                            // 因为卡片的内容改变了，而发送到服务器是使用的model，使用要同步修改
                            model.refreshByCard();
                        }
                    }
                }

                // 直接发送，进行网络调度
                RemoteService service = Network.remote();
                service.msgPush(model).enqueue(new Callback<RspModel<MessageCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call,
                                           Response<RspModel<MessageCard>> response) {
                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && rspModel.success()) {
                            MessageCard rspCard = rspModel.getResult();
                            if (rspCard != null) {
                                // 成功的调度
                                Factory.getMessageCenter().dispatch(rspCard);
                            }
                        } else {
                            // 解析是否是账户异常
                            Factory.decodeRspCode(rspModel, null);
                            // 走失败流程
                            onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {
                        messageCard.setStatus(Message.STATUS_FAILED);
                        Factory.getMessageCenter().dispatch(messageCard);
                    }
                });
            }
        });
    }

    // 上传图片
    private static String uploadPic(String path) {
        File file = null;
        try {
            // 利用Glide的缓存区间去加载一个外部文件，可以避免图片外部权限问题
            file = Glide.with(Factory.app())
                    .load(path)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (file != null) {
            // 压缩图片
            String cacheDir = BaseApplication.getCacheDirFile().getAbsolutePath();
            String temFile = String.format("%s/image/Cache_%s.%s", cacheDir, SystemClock.uptimeMillis(), BitmapUtil.getExtension(file.getAbsolutePath()));
            if (PicturesCompressor.compressImage(file.getAbsolutePath(), temFile, Common.Constance.MAX_UPLOAD_IMAGE_LENGTH)) {
                // 上传
                String ossPath = UploadHelper.uploadImage(temFile);
                // 清理缓存
                StreamUtil.delete(temFile);
                return ossPath;
            }
        }
        return null;
    }

    // 上传语音
    private static String uploadAudio(String path) {
        // TODO
        return null;
    }

    /**
     * 查询一个消息，这个消息是一个群中聊天的最后一条消息
     *
     * @param groupId 群id
     * @return 群中聊天的最后一条消息
     */
    public static Message findLastWithGroup(String groupId) {
        return SQLite.select().from(Message.class).where(Message_Table.group_id.eq(groupId)).orderBy(Message_Table.createAt, false) // 倒序
                .querySingle();
    }

    /**
     * 查询一个消息，这个消息是一个单聊的最后一条消息
     *
     * @param userId 用户id
     * @return 单聊的最后一条消息
     */
    public static Message findLastWithUser(String userId) {
        return SQLite.select().from(Message.class).where(OperatorGroup.clause().and(Message_Table.sender_id.eq(userId)).and(Message_Table.group_id.isNull())).or(Message_Table.receiver_id.eq(userId)).orderBy(Message_Table.createAt, false).querySingle();
    }
}
