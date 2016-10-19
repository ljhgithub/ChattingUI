package com.saysay.ljh.chattingui.message.model;

import android.app.Activity;

/**
 * Created by ljh on 2016/10/19.
 */

public class Container {
    public final Activity activity;
    public final String account;
    public final ModuleProxy proxy;

    public Container(Activity activity, String account, ModuleProxy proxy) {
        this.activity = activity;
        this.account = account;
        this.proxy = proxy;
    }
}
