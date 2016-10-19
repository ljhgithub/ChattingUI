package com.saysay.ljh.chattingui.message.model;

/**
 * Created by ljh on 2016/10/19.
 */

public interface IMessage {
    public void setMessageBody(MessageBody body);
    public MessageBody getMessageBody();
    public long getTime();

    public void setTime(Long time);

    public MsgDirectionEnum getDirection();

    public void setDirection(MsgDirectionEnum direction);

    public MsgStatusEnum getStatus();

    public void setStatus(MsgStatusEnum status);

    public MsgTypeEnum getType();

    public void setType(MsgTypeEnum type);
}
