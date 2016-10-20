package com.saysay.ljh.chattingui;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.saysay.ljh.chattingui.message.adapter.MessageAdapter;
import com.saysay.ljh.chattingui.message.model.Container;
import com.saysay.ljh.chattingui.message.model.IMessage;
import com.saysay.ljh.chattingui.message.model.MessageTextBody;
import com.saysay.ljh.chattingui.message.model.ModuleProxy;
import com.saysay.ljh.chattingui.message.model.MsgDirectionEnum;
import com.saysay.ljh.chattingui.message.model.MsgStatusEnum;
import com.saysay.ljh.chattingui.message.model.MsgTypeEnum;
import com.saysay.ljh.chattingui.message.model.SimpleMessage;
import com.saysay.ljh.chattingui.widgets.ChattingFooter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ljh on 2016/10/18.
 */

public class ChattingFragment extends Fragment implements ChattingFooter.OnChattingFooterListener, ModuleProxy {

    private ChattingFooter mChattingFooter;
    private View mRoot;
    private RecyclerView rlvMessage;
    private MessageAdapter messageAdapter;
    private List<IMessage> msgs = new ArrayList<>();
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

    public static ChattingFragment newFragment() {
        return new ChattingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chatting, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mChattingFooter = (ChattingFooter) rootView.findViewById(R.id.chatting_footer);
        mRoot = rootView.findViewById(R.id.ll_root);
        detector = new GestureDetector(getContext(), simpleOnGestureListener);
        mChattingFooter.setActivity(getActivity(), mRoot);
        rlvMessage = (RecyclerView) rootView.findViewById(R.id.rlv_message);
        rlvMessage.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rlvMessage.setLayoutManager(llm);
        messageAdapter = new MessageAdapter(getContext());
        rlvMessage.setAdapter(messageAdapter);
        rlvMessage.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mChattingFooter.setOnChattingFooterListener(this);
        rlvMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return false;
            }
        });

        Container container = new Container(getActivity(), "ssss", this);
        mChattingFooter.setContainer(container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

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

    int position = 0;
//    static List<String> urls = new ArrayList<>();
//
//    static {
//        urls.add("/storage/emulated/0/1yibao/a.jpg");
//        urls.add("/storage/emulated/0/1yibao/b.jpg");
//        urls.add("/storage/emulated/0/1yibao/c.jpg");
//        urls.add("/storage/emulated/0/1yibao/d.jpg");
//    }

    @Override
    public void OnSendTextMessageRequest(CharSequence text) {
        SimpleMessage simpleMessage = new SimpleMessage();

        simpleMessage.setType(MsgTypeEnum.TEXT);

        if (position % 2 == 0) {
            simpleMessage.setDirection(MsgDirectionEnum.SEND);
        } else {
            simpleMessage.setDirection(MsgDirectionEnum.RECEIVE);
        }
        if (position % 3 == 0) {
            simpleMessage.setStatus(MsgStatusEnum.SUCCESS);
        } else {
            simpleMessage.setStatus(MsgStatusEnum.FAIL);
        }
        simpleMessage.setTime(Calendar.getInstance().getTimeInMillis());
        MessageTextBody messageText = new MessageTextBody();
        messageText.setContent(text.toString());
        simpleMessage.setMessageBody(messageText);

//        MessagePictureBody messagePictureBody = new MessagePictureBody();
//        messagePictureBody.setPath(urls.get(position));
//        simpleMessage.setMessageBody(messagePictureBody);
        msgs.add(simpleMessage);
        messageAdapter.setMsgs(msgs);
        rlvMessage.postDelayed(new Runnable() {
            @Override
            public void run() {
                rlvMessage.scrollToPosition(msgs.size() - 1);
            }
        }, 100);
        position++;
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
    public void release() {

    }


    /**
     * hide inputMethod
     */
    public boolean hideSoftKeyboard() {
        boolean b = false;
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = getActivity().getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                b = inputMethodManager.hideSoftInputFromWindow(windowToken, 0);

            }
        }
        return b;
    }

    @Override
    public void sendMessage(IMessage message) {
        Log.d("tag", "sendMessage");
    }
}
