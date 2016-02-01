package com.saysay.ljh.chattingui.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.PopupMenuCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.saysay.ljh.chattingui.R;
import com.saysay.ljh.chattingui.utils.PrefUtils;

import java.io.File;

/**
 * Created by ljh on 2016/1/26.
 */
public class ChattingFooter extends LinearLayout implements View.OnClickListener, TextWatcher, ViewTreeObserver.OnGlobalLayoutListener, View.OnTouchListener {
    // cancel recording sliding distance field.
    private static final int CANCEL_DISTANCE = 60;
    private Context mContext;
    private View parentRoot, view, vLine;
    private EditText etInput;
    private ImageView ivVoice, ivExtra, ivEmoji;
    private ViewPager vpExtra;
    private Button btnSend, btnVoice;
    private AppCompatActivity mActivity;
    private int keyboardHeight;
    private LinearLayout.LayoutParams vpLp;
    private LayoutInflater mInflater;
    private OnChattingFooterListener mChattingFooterListener;

    public ChattingFooter(Context context) {
        super(context);
        initView(context);
    }

    public void setOnChattingFooterListener(OnChattingFooterListener onChattingFooterListener) {
        this.mChattingFooterListener = onChattingFooterListener;
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

    PopupWindow popupWindow;

    private void initView(Context context) {
        setRecordPopWindow(context);
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
        btnVoice.setOnTouchListener(this);

    }

    private ImageView mVoiceHintAnim;
    private View mVoiceHintAnimArea, mVoiceRcdHitCancelView, mVoiceHintTooshort;

    private void setRecordPopWindow(Context context) {
        mInflater = LayoutInflater.from(context);
        View recordView = mInflater.inflate(R.layout.pop_chatting_voice, null);
        popupWindow = new PopupWindow(recordView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        mVoiceHintAnim = (ImageView) recordView.findViewById(R.id.voice_rcd_hint_anim);
        mVoiceHintAnimArea = recordView.findViewById(R.id.voice_rcd_hint_anim_area);
        mVoiceRcdHitCancelView = recordView.findViewById(R.id.voice_rcd_hint_cancel_area);
//        mVoiceHintCancelText = (TextView) recordView.findViewById(R.id.voice_rcd_hint_cancel_text);
//        mVoiceHintCancelIcon = (ImageView) popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_cancel_icon);
//        mVoiceHintRcding = recordView.findViewById(R.id.voice_rcd_hint_rcding);
        mVoiceHintTooshort = recordView.findViewById(R.id.voice_rcd_hint_tooshort);
//        mVoiceNormalWording = (TextView) recordView.findViewById(R.id.voice_rcd_normal_wording);

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
            case R.id.btn_voice:
                Log.d("tag", "btn_voice");
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
        }

    }

    public boolean isShowExtra() {
        return vpExtra.getVisibility() == VISIBLE;
    }

    public void hideExtra() {
        vpExtra.setVisibility(GONE);
    }

    public long getAvailableSize() {

        File path = Environment.getExternalStorageDirectory(); //取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize) / 1024 / 1024;//  MIB单位
    }

    long currentTimeMillis = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getAvailableSize() < 10) {
            Log.d("tag", "sdcard no memory ");
            Toast.makeText(mContext, "储存卡存储空间不足", Toast.LENGTH_SHORT).show();
            return false;
        }

        long time = System.currentTimeMillis() - currentTimeMillis;
        if (time <= 300) {
            Log.d("tag", "nvalid click");
            currentTimeMillis = System.currentTimeMillis();
            return false;
        }

        if (!isExistExternalStore()) {
            Toast.makeText(mContext, "储存卡已拔出，语音功能将暂时不可用", Toast.LENGTH_SHORT).show();
            return false;

        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("tag", "ChatFooter voice recording action down");
                if (mChattingFooterListener != null) {
                    mChattingFooterListener.OnVoiceRcdInitReuqest();
                }
                btnVoice.setText("松开 结束");
                break;

            case MotionEvent.ACTION_MOVE:
                if (popupWindow == null) {
                    return false;
                }
                if (event.getX() <= 0.0F || event.getY() <= -CANCEL_DISTANCE || event.getX() >= btnVoice.getWidth()) {
                    Log.d("tag", "show cancel Tips");
                    mVoiceRcdHitCancelView.setVisibility(View.VISIBLE);
                    mVoiceHintAnimArea.setVisibility(View.GONE);
                    btnVoice.setText(R.string.chatfooter_cancel_rcd_release);

                } else {
                    btnVoice.setText("松开 结束");
                    popupWindow.showAtLocation(parentRoot, Gravity.CENTER, 0, 0);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.update();
                    Log.d("tag", "show rcd animation Tips");
                    mVoiceRcdHitCancelView.setVisibility(View.GONE);
                    mVoiceHintAnimArea.setVisibility(View.VISIBLE);
                }

                break;
            case MotionEvent.ACTION_UP:
                popupWindow.dismiss();
                Log.d("tag", "ChatFooter voice recording action up ");
                btnVoice.setText("按住 请说话");
                break;
        }

        return false;
    }

    /**
     * 是否有外存卡
     *
     * @return
     */
    public static boolean isExistExternalStore() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public interface OnChattingFooterListener {

        void OnVoiceRcdInitReuqest();

        void OnVoiceRcdStartRequest();

        /**
         * Called when the voce record button nomal and cancel send voice.
         */
        void OnVoiceRcdCancelRequest();

        /**
         * Called when the voce record button nomal and send voice.
         */
        void OnVoiceRcdStopRequest();

        void OnSendTextMessageRequest(CharSequence text);

        void OnUpdateTextOutBoxRequest(CharSequence text);

        void OnSendCustomEmojiRequest(int emojiid, String emojiName);

        void OnEmojiDelRequest();

        void OnInEditMode();

        void onPause();

        void onResume();

        void release();
    }


}
