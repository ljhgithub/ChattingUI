package com.saysay.ljh.chattingui.message.model;

/**
 * Created by ljh on 2016/10/19.
 */

public class SimpleMessage implements IMessage {
    private Long time;
    private MsgDirectionEnum direction;
    private MsgStatusEnum status;
    private MsgTypeEnum type;
    private MessageBody messageBody;

    @Override
    public void setMessageBody(MessageBody body) {
        this.messageBody = body;
    }

    @Override
    public MessageBody getMessageBody() {
        return messageBody;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public MsgDirectionEnum getDirection() {
        return direction;
    }

    @Override
    public void setDirection(MsgDirectionEnum direction) {
        this.direction = direction;
    }

    @Override
    public MsgStatusEnum getStatus() {
        return status;
    }

    @Override
    public void setStatus(MsgStatusEnum status) {
        this.status = status;
    }

    @Override
    public MsgTypeEnum getType() {
        return type;
    }

    @Override
    public void setType(MsgTypeEnum type) {
        this.type = type;
    }
}
