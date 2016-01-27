package com.saysay.ljh.chattingui.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.saysay.ljh.chattingui.R;
import com.saysay.ljh.chattingui.utils.PrefUtils;

/**
 * Created by ljh on 2016/1/26.
 */
public class ChattingFooter extends LinearLayout implements View.OnClickListener, TextWatcher, ViewTreeObserver.OnGlobalLayoutListener {
    private Context mContext;
    private View parentRoot, view, vLine;
    private EditText etInput;
    private ImageView ivVoice, ivExtra, ivEmoji;
    private ViewPager vpExtra;
    private Button btnSend, btnVoice;
    private AppCompatActivity mActivity;
    private int keyboardHeight;
    private LinearLayout.LayoutParams vpLp;


    public ChattingFooter(Context context) {
        super(context);
        initView(context);
    }

    public ChattingFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setActivity(AppCompatActivity activity, View root) {
        mActivity = activity;
        parentRoot = root;
        parentRoot.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private void initView(Context context) {
        setFocusable(true);
        keyboardHeight = PrefUtils.getKeyboardHeight(context);
        mContext = context;
        setOrientation(VERTICAL);
        view = LayoutInflater.from(mContext).inflate(R.layout.chatting_footer, this, true);
        etInput = (EditText) view.findViewById(R.id.et_input);
        ivVoice = (ImageView) view.findViewById(R.id.iv_voice);
        ivEmoji = (ImageView) view.findViewById(R.id.iv_emoji);
        ivExtra = (ImageView) view.findViewById(R.id.iv_extra);
        vpExtra = (ViewPager) view.findViewById(R.id.vp_extra);
        btnSend = (Button) view.findViewById(R.id.btn_send);
        btnVoice = (Button) view.findViewById(R.id.btn_voice);
        vLine = view.findViewById(R.id.v_line);
        etInput.setOnClickListener(this);
        ivVoice.setOnClickListener(this);
        ivEmoji.setOnClickListener(this);
        ivExtra.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnVoice.setOnClickListener(this);
        etInput.addTextChangedListener(this);
        ivVoice.setSelected(false);
        ivEmoji.setSelected(false);
        vpLp = (LayoutParams) vpExtra.getLayoutParams();
        vpLp.height = keyboardHeight < 200 ? WindowManager.LayoutParams.WRAP_CONTENT : keyboardHeight;
        vpLp.weight = ViewGroup.LayoutParams.MATCH_PARENT;
        vpExtra.setLayoutParams(vpLp);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_input:
                showSoftInputFromWindow(etInput);
                ivEmoji.setSelected(false);
                break;
            case R.id.iv_voice:
                hideSoftInputFromWindow(ivVoice);
                vpExtra.setVisibility(GONE);
                ivVoice.setSelected(!ivVoice.isSelected());
                if (ivVoice.isSelected()) {
                    showVoice();
                } else {
                    showInput();
                    showSoftInputFromWindow(etInput);
                }
                break;
            case R.id.iv_emoji:
                ivEmoji.setSelected(!ivEmoji.isSelected());
                if (ivEmoji.isSelected()) {
                    vpExtra.setVisibility(VISIBLE);
                    setInputMode();
                    hideSoftInputFromWindow(etInput);
                } else {
                    setInputMode();
                    showSoftInputFromWindow(etInput);
                }

                break;
            case R.id.iv_extra:
                vpExtra.setVisibility(VISIBLE);
                setInputMode();
                hideSoftInputFromWindow(ivExtra);
                break;
            case R.id.btn_send:
                break;
            default:
                break;
        }
    }

    private void setInputMode() {
        if (null != mActivity) {
            if (vpExtra.getVisibility() == VISIBLE) {
                mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

            } else {
                mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        }
    }

    private void showInput() {
        ivVoice.setImageResource(R.mipmap.chatting_voice_n);
        btnVoice.setVisibility(GONE);
        etInput.setVisibility(VISIBLE);
        vLine.setVisibility(VISIBLE);
        ivEmoji.setVisibility(VISIBLE);
        ivEmoji.setSelected(false);
        showSendOrExtra();
    }

    private void showSendOrExtra() {
        if (TextUtils.isEmpty(etInput.getText())) {
            ivExtra.setVisibility(VISIBLE);
            btnSend.setVisibility(GONE);
        } else {
            ivExtra.setVisibility(GONE);
            btnSend.setVisibility(VISIBLE);
        }
    }

    private void showVoice() {
        ivVoice.setImageResource(R.mipmap.chatting_keyboard_n);
        btnVoice.setVisibility(VISIBLE);
        etInput.setVisibility(GONE);
        vLine.setVisibility(GONE);
        ivEmoji.setVisibility(GONE);
        ivExtra.setVisibility(VISIBLE);
        btnSend.setVisibility(GONE);
    }


    public void hideSoftInputFromWindow(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public void showSoftInputFromWindow(View view) {
        if (vpExtra.getVisibility() == VISIBLE && keyboardHeight > 200) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        } else {
            vpExtra.setVisibility(GONE);
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        InputMethodManager inputMethodManager = (InputMethodManager) view
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        showSendOrExtra();
    }

    @Override
    public void onGlobalLayout() {
        Log.d("emoji", "onGlobalLayout");
        Rect rootRect = new Rect();
        int paddingTop;
        parentRoot.getWindowVisibleDisplayFrame(rootRect);
        int screenHeight = parentRoot.getRootView()
                .getHeight();
        int heightDifference = screenHeight
                - (rootRect.bottom - rootRect.top);
        int resourceId = this.getResources()
                .getIdentifier("status_bar_height",
                        "dimen", "android");

        if (resourceId > 0) {
            paddingTop = parentRoot.getResources()
                    .getDimensionPixelSize(resourceId);
            heightDifference -= paddingTop;
        }
        if (heightDifference > 200) {
            PrefUtils.setKeyboardHeight(mContext, heightDifference);
            vpLp.height = heightDifference;
            keyboardHeight = heightDifference;
            Log.d("emoji", rootRect.bottom + "top" + rootRect.top + "  " + heightDifference);
        }

    }

    public boolean isShowExtra() {
        return vpExtra.getVisibility() == VISIBLE;
    }

    public void hideExtra() {
        vpExtra.setVisibility(GONE);
    }
}
