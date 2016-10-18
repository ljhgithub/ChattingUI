package com.saysay.ljh.chattingui;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.saysay.ljh.chattingui.widgets.ChattingFooter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljh on 2016/1/26.
 */
public class ChattingActivity extends AppCompatActivity {
    private ChattingFooter mChattingFooter;
    private View mRoot;
    private RecyclerView rlvMessage;
    private MessageAdapter messageAdapter;
    private List<String> msgs = new ArrayList<>();
    private GestureDetector detector;
    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            if (mChattingFooter.isShowExtra()) {
                mChattingFooter.hideExtra();
            }
            hideSoftKeyboard();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (mChattingFooter.isShowExtra()) {
                mChattingFooter.hideExtra();
            }
            hideSoftKeyboard();
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    @Override
    public void onBackPressed() {
        if (mChattingFooter.isShowExtra()) {
            mChattingFooter.hideExtra();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        mChattingFooter = (ChattingFooter) findViewById(R.id.chatting_footer);
        mRoot = findViewById(R.id.ll_root);
        detector = new GestureDetector(this, simpleOnGestureListener);
        mChattingFooter.setActivity(this, mRoot);
        rlvMessage = (RecyclerView) findViewById(R.id.rlv_message);
        rlvMessage.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rlvMessage.setLayoutManager(llm);
        messageAdapter = new MessageAdapter(this);
        rlvMessage.setAdapter(messageAdapter);
        rlvMessage.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mChattingFooter.setOnChattingFooterListener(new ChattingFooter.OnChattingFooterListener() {
            @Override
            public void OnVoiceRcdInitReuqest() {

            }

            @Override
            public void OnVoiceRcdStartRequest() {

            }

            @Override
            public void OnVoiceRcdCancelRequest() {

            }

            @Override
            public void OnVoiceRcdStopRequest() {

            }

            @Override
            public void OnSendTextMessageRequest(CharSequence text) {
                msgs.add(text.toString());
                messageAdapter.setMsgs(msgs);
                rlvMessage.scrollToPosition(msgs.size() - 1);
            }

            @Override
            public void OnUpdateTextOutBoxRequest(CharSequence text) {

            }

            @Override
            public void OnSendCustomEmojiRequest(int emojiid, String emojiName) {

            }

            @Override
            public void OnEmojiDelRequest() {

            }

            @Override
            public void OnInEditMode() {
                rlvMessage.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlvMessage.scrollToPosition(msgs.size() - 1);
                    }
                }, 200);

            }

            @Override
            public void onPause() {

            }

            @Override
            public void onResume() {

            }

            @Override
            public void release() {

            }
        });

        rlvMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return false;
            }
        });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        boolean b = false;
//        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//            if (mChattingFooter.isShowExtra()) {
//                mChattingFooter.hideExtra();
//                b = true;
//            }
//            b = hideSoftKeyboard() || b;
//            if (b) {
//                return true;
//            } else {
//                onBackPressed();
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

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
