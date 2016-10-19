package com.saysay.ljh.chattingui.message.model;

/**
 * Created by ljh on 2016/10/19.
 */

public enum MsgDirectionEnum {
    SEND(0),
    RECEIVE(1);

    private int value;

    private MsgDirectionEnum(int var3) {
        this.value = var3;
    }

    public final int getValue() {
        return this.value;
    }

    public static MsgDirectionEnum directionOfValue(int var0) {
        MsgDirectionEnum[] var1;
        int var2 = (var1 = values()).length;

        for(int var3 = 0; var3 < var2; ++var3) {
            MsgDirectionEnum var4;
            if((var4 = var1[var3]).getValue() == var0) {
                return var4;
            }
        }

        return RECEIVE;
    }
}
