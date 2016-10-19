package com.saysay.ljh.chattingui.message.holder;

import android.view.View;
import android.widget.TextView;

import com.saysay.ljh.chattingui.R;
import com.saysay.ljh.chattingui.message.model.IMessage;
import com.saysay.ljh.chattingui.message.model.MessageTextBody;

/**
 * Created by ljh on 2016/10/19.
 */

public class MessageTextHolder extends BaseMessageHolder {
    private TextView msgTextContent;

    public MessageTextHolder(View itemView) {
        super(itemView);
        View view = View.inflate(itemView.getContext(), R.layout.im_msg_item_text, msgContent);
        msgTextContent = (TextView) view.findViewById(R.id.msg_text_content);
    }

    @Override
    public void bindMessage(IMessage iMessage) {
        super.bindMessage(iMessage);
        MessageTextBody messageText = (MessageTextBody) iMessage.getMessageBody();
        msgTextContent.setText(messageText.getContent());
    }
}
