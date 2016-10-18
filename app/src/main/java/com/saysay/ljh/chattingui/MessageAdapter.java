package com.saysay.ljh.chattingui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljh on 2016/10/17.
 */

public class MessageAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> msgs;

    public MessageAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        msgs = new ArrayList<>();
    }

    public void setMsgs(List<String> msgs) {
        this.msgs = msgs;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SendTextHolder sendTextHolder = new SendTextHolder(mInflater.inflate(R.layout.im_msg_item, parent, false));
        return sendTextHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SendTextHolder h = (SendTextHolder) holder;
        mInflater.inflate(R.layout.im_msg_item_text, h.msgContent);
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    protected class SendTextHolder extends RecyclerView.ViewHolder {
        public FrameLayout msgContent;
        public LinearLayout msgBody;
        public ImageView avatarLeft;

        public SendTextHolder(View itemView) {
            super(itemView);
            avatarLeft = (ImageView) itemView.findViewById(R.id.message_item_avatar_left);
            msgBody = (LinearLayout) itemView.findViewById(R.id.message_item_body);
            msgContent = (FrameLayout) itemView.findViewById(R.id.message_item_content);
            FrameLayout frameLayout = (FrameLayout) itemView.findViewById(R.id.msg_body_layout);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) frameLayout.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_TOP, avatarLeft.getId());
        }
    }
}
