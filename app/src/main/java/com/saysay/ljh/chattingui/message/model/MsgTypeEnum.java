package com.saysay.ljh.chattingui.message.model;

/**
 * Created by ljh on 2016/10/19.
 */

public enum MsgTypeEnum {
    UNDEF(-1, "Unknown"),
    TEXT(0, ""),
    IMAGE(1, "图片"),
    AUDIO(2, "语音"),
    VIDEO(3, "视频"),
    LOCATION(4, "位置"),
    FILE(6, "文件"),
    AVCHAT(7, "音视频通话"),
    NOTIFICATION(5, "通知消息"),
    TIP(10, "提醒消息"),
    CUSTOM(100, "自定义消息");

    private final int value;
    final String sendMessageTip;

    private MsgTypeEnum(int var3, String var4) {
        this.value = var3;
        this.sendMessageTip = var4;
    }

    public final int getValue() {
        return this.value;
    }

    public final String getSendMessageTip() {
        return this.sendMessageTip;
    }

    public static MsgTypeEnum typeOfValue(int var0) {
        MsgTypeEnum[] var1;
        int var2 = (var1 = values()).length;

        for (int var3 = 0; var3 < var2; ++var3) {
            MsgTypeEnum var4;
            if ((var4 = var1[var3]).getValue() == var0) {
                return var4;
            }
        }

        return UNDEF;
    }
}
