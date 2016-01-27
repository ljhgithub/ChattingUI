package com.saysay.ljh.chattingui;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.saysay.ljh.chattingui.widgets.ChattingFooter;

/**
 * Created by ljh on 2016/1/26.
 */
public class ChattingActivity extends AppCompatActivity {
    private ChattingFooter mChattingFooter;
    private View mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        mChattingFooter = (ChattingFooter) findViewById(R.id.chatting_footer);
        mRoot = findViewById(R.id.ll_root);
        mChattingFooter.setActivity(this, mRoot);


    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean b = false;
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (mChattingFooter.isShowExtra()) {
                mChattingFooter.hideExtra();
                b = true;
            }
            b = hideSoftKeyboard() || b;
            if (b) {
                return true;
            } else {
//                backUp();
                onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * hide inputMethod
     */
    public boolean hideSoftKeyboard() {
        boolean b = false;
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                b = inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
        return b;
    }
}
