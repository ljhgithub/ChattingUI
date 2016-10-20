package com.saysay.ljh.chattingui.message.holder;

import android.view.View;
import android.widget.ImageView;

import com.saysay.ljh.chattingui.R;
import com.saysay.ljh.chattingui.message.model.IMessage;
import com.saysay.ljh.chattingui.message.model.MessagePictureBody;
import com.saysay.ljh.chattingui.utils.BitmapDecoder;
import com.saysay.ljh.chattingui.utils.ScreenUtil;


/**
 * Created by ljh on 2016/10/19.
 */

public class MessagePictureHolder extends BaseMessageHolder {
    private ImageView msgThumbnail;

    public MessagePictureHolder(View itemView) {
        super(itemView);
        View view = View.inflate(itemView.getContext(), R.layout.im_msg_item_picture, msgContent);
        msgThumbnail = (ImageView) view.findViewById(R.id.msg_thumbnail);
    }

    @Override
    public void bindMessage(IMessage iMessage) {
        super.bindMessage(iMessage);
        MessagePictureBody msgPictureBody = (MessagePictureBody) iMessage.getMessageBody();
        msgThumbnail.setImageBitmap(BitmapDecoder.decodeSampled(msgPictureBody.getPath(), getImageMaxEdge(), getImageMaxEdge()));
    }

    public static int getImageMaxEdge() {
        return (int) (165.0 / 320.0 * ScreenUtil.screenWidth);
    }

    public static int getImageMinEdge() {
        return (int) (76.0 / 320.0 * ScreenUtil.screenWidth);
    }
}
