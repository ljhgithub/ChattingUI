package com.saysay.ljh.chattingui.message.actions;

import android.util.Log;

import com.saysay.ljh.chattingui.message.model.SimpleMessage;

/**
 * Created by ljh on 2016/10/19.
 */

public class PickImageAction extends BaseAction {
    public PickImageAction(int iconResId, int titleId) {
        super(iconResId, titleId);
    }

    @Override
    public void onClick() {
        Log.d("tag","dddddddddddddddd");
        sendMessage(new SimpleMessage());
    }

}
