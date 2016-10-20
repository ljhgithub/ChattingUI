package com.saysay.ljh.chattingui.message.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.saysay.ljh.chattingui.R;
import com.saysay.ljh.chattingui.message.holder.MessagePictureHolder;
import com.saysay.ljh.chattingui.message.holder.MessageTextHolder;
import com.saysay.ljh.chattingui.message.holder.UnknownHolder;
import com.saysay.ljh.chattingui.message.model.IMessage;
import com.saysay.ljh.chattingui.message.model.MsgTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljh on 2016/10/17.
 */

public class MessageAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<IMessage> msgs;

    public MessageAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        msgs = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return msgs.get(position).getType().getValue();
    }

    public void setMsgs(List<IMessage> msgs) {
        this.msgs = msgs;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        switch (MsgTypeEnum.typeOfValue(viewType)) {
            case TEXT:
                holder = new MessageTextHolder(mInflater.inflate(R.layout.im_msg_item, parent, false));
                break;
            case IMAGE:
                holder = new MessagePictureHolder(mInflater.inflate(R.layout.im_msg_item, parent, false));
                break;
            case UNDEF:
                holder = new UnknownHolder(mInflater.inflate(R.layout.item_empty, parent, false));
                break;
            default:
                holder = new UnknownHolder(mInflater.inflate(R.layout.item_empty, parent, false));
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (MsgTypeEnum.typeOfValue(getItemViewType(position))) {
            case TEXT: {
                MessageTextHolder h = (MessageTextHolder) holder;
                h.bindMessage(msgs.get(position));
                h.showTime(position / 2 == 0);
                break;
            }
            case IMAGE: {
                MessagePictureHolder h = (MessagePictureHolder) holder;
                h.bindMessage(msgs.get(position));
                h.showTime(position / 2 == 0);
                break;
            }
            case UNDEF:
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

}
