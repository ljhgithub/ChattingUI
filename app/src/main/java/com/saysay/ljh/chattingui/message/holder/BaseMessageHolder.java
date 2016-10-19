package com.saysay.ljh.chattingui.message.holder;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saysay.ljh.chattingui.R;
import com.saysay.ljh.chattingui.message.model.IMessage;
import com.saysay.ljh.chattingui.message.model.MsgDirectionEnum;
import com.saysay.ljh.chattingui.message.model.MsgStatusEnum;
import com.saysay.ljh.chattingui.utils.TimeUtil;

/**
 * Created by ljh on 2016/10/19.
 */

public class BaseMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected ImageView avatarLeft;
    protected ImageView avatarRight;
    protected TextView msgTime;
    protected ImageView msgAlter;
    protected ProgressBar msgProgress;
    private FrameLayout msgBodyLayout;
    protected LinearLayout msgBody;
    protected FrameLayout msgContent;

    protected IMessage mMessage;

    private SendMessageFailListener sendMessageFailListener;

    public void setSendMessageFailListener(SendMessageFailListener listener) {
        this.sendMessageFailListener = listener;
    }

    public SendMessageFailListener getSendMessageFailListener() {
        return sendMessageFailListener;
    }

    public BaseMessageHolder(View itemView) {
        super(itemView);
        msgTime = (TextView) itemView.findViewById(R.id.message_item_time);
        avatarLeft = (ImageView) itemView.findViewById(R.id.message_item_avatar_left);
        avatarRight = (ImageView) itemView.findViewById(R.id.message_item_avatar_right);
        msgAlter = (ImageView) itemView.findViewById(R.id.message_item_alert);
        msgProgress = (ProgressBar) (itemView.findViewById(R.id.message_item_progress));
        msgBodyLayout = (FrameLayout) itemView.findViewById(R.id.msg_body_layout);
        msgBody = (LinearLayout) itemView.findViewById(R.id.message_item_body);
        msgContent = (FrameLayout) itemView.findViewById(R.id.message_item_content);
        msgAlter.setOnClickListener(this);
    }

    public void bindMessage(IMessage iMessage) {
        mMessage = iMessage;
        setContent();
        setStatus();
        showTime(false);
    }

    protected final void setContent() {
        int index = (isReceiveMessage() ? 0 : 2);
        if (msgBody.getChildAt(index) != msgContent) {
            msgBody.removeView(msgContent);
            msgBody.addView(msgContent, index);
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) msgBodyLayout.getLayoutParams();
        int anchor = (index == 2 ? avatarRight.getId() : avatarLeft.getId());
        lp.addRule(RelativeLayout.ALIGN_TOP, anchor);
        if (isReceiveMessage()) {
            msgBody.setGravity(Gravity.LEFT);
            msgContent.setBackgroundResource(leftBackground());
            avatarLeft.setVisibility(View.VISIBLE);
            avatarRight.setVisibility(View.GONE);
        } else {
            msgBody.setGravity(Gravity.RIGHT);
            msgContent.setBackgroundResource(rightBackground());
            avatarLeft.setVisibility(View.GONE);
            avatarRight.setVisibility(View.VISIBLE);
        }
    }

    protected int leftBackground() {
        return R.drawable.im_msg_text_receive_bg_sel;
    }

    protected int rightBackground() {
        return R.drawable.im_msg_text_send_bg_sel;
    }

    protected final boolean isReceiveMessage() {
        return MsgDirectionEnum.RECEIVE == mMessage.getDirection();
    }


    /**
     * 设置消息发送状态
     */
    private void setStatus() {
        MsgStatusEnum status = mMessage.getStatus();
        switch (status) {
            case FAIL:
                msgProgress.setVisibility(View.GONE);
                msgAlter.setVisibility(View.VISIBLE);
                break;
            case SENDING:
                msgProgress.setVisibility(View.VISIBLE);
                msgAlter.setVisibility(View.GONE);
                break;
            default:
                msgProgress.setVisibility(View.GONE);
                msgAlter.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 设置时间显示
     */
    public void showTime(boolean show) {
        if (show) {
            msgTime.setVisibility(View.VISIBLE);
        } else {
            msgTime.setVisibility(View.GONE);
            return;
        }
        String text = TimeUtil.getTimeShowString(mMessage.getTime(), false);
        msgTime.setText(text);
    }

    @Override
    public void onClick(View v) {
        if (null != sendMessageFailListener) {
            sendMessageFailListener.sendFail(mMessage);
        }
    }

    public interface SendMessageFailListener {
        public void sendFail(IMessage iMessage);
    }
}
