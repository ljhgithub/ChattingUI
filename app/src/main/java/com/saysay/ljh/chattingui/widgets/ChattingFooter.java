package com.saysay.ljh.chattingui.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.view.ViewPager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.saysay.ljh.chattingui.R;
import com.saysay.ljh.chattingui.message.actions.BaseAction;
import com.saysay.ljh.chattingui.message.actions.PickImageAction;
import com.saysay.ljh.chattingui.message.adapter.ExtraActionsPagerAdapter;
import com.saysay.ljh.chattingui.message.model.Container;
import com.saysay.ljh.chattingui.utils.PrefUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljh on 2016/1/26.
 */
public class ChattingFooter extends LinearLayout implements View.OnClickListener, TextWatcher, ViewTreeObserver.OnGlobalLayoutListener, View.OnTouchListener {
    // cancel recording sliding distance field.
    private static final int CANCEL_DISTANCE = 60;
    private Context mContext;
    private View parentRoot, view, vLine;
    private EditTextPreIme etInput;
    private ImageView ivVoice, ivExtra, ivEmoji;
    private ViewPager vpExtra;
    private Button btnSend, btnVoice;
    private Activity mActivity;
    private int keyboardHeight;
    private int extraBarHeight;
    private LinearLayout.LayoutParams extraLp;
    private LayoutInflater mInflater;
    private OnChattingFooterListener mChattingFooterListener;
    private View extraView;
    private ExtraActionsPagerAdapter pagerAdapter;
   private boolean  deviceHasNavigationBar;
    private PointIndicator pointIndicator;

    private Container mContainer;

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

    public void setActivity(Activity activity, View root) {
        mActivity = activity;
        parentRoot = activity.getWindow().getDecorView();
        parentRoot.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    PopupWindow popupWindow;

    public void setContainer(Container mContainer) {
        this.mContainer = mContainer;
        List<BaseAction> baseActions = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            PickImageAction pickImageAction = new PickImageAction(R.mipmap.ic_launcher, R.string.app_name);
            pickImageAction.setContainer(mContainer);
            baseActions.add(pickImageAction);
        }
        pagerAdapter = new ExtraActionsPagerAdapter(mContext, vpExtra, baseActions);
    }

    private void initView(Context context) {
        mContext = context;
        deviceHasNavigationBar=checkDeviceHasNavigationBar(context);
        setRecordPopWindow(context);
        setFocusable(true);
        keyboardHeight = PrefUtils.getKeyboardHeight(context);
        setOrientation(VERTICAL);
        view = LayoutInflater.from(mContext).inflate(R.layout.chatting_footer, this, true);
        extraView = view.findViewById(R.id.extra_view);
        pointIndicator = (PointIndicator) view.findViewById(R.id.pointIndicator);
        pointIndicator.setIndicator(10, 30, Color.RED, Color.BLACK);

        etInput = (EditTextPreIme) view.findViewById(R.id.et_input);
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
        extraLp = (LayoutParams) extraView.getLayoutParams();
        extraLp.height = keyboardHeight < 200 ? 831 : keyboardHeight;
        extraLp.weight = ViewGroup.LayoutParams.MATCH_PARENT;
        extraView.setLayoutParams(extraLp);
        btnVoice.setOnTouchListener(this);
        etInput.setOnKeyBackListener(new EditTextPreIme.OnKeyBackListener() {
            @Override
            public boolean onKeyBack() {
                if (isShowExtra()) {
                    hideExtra();
                    hideSoftInputFromWindow(etInput);
                    ivEmoji.setSelected(false);
                    return true;
                }
                return false;
            }
        });
        etInput.requestFocus();
        etInput.setCursorVisible(false);

        vpExtra.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                pointIndicator.translationPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                etInput.setCursorVisible(true);
                showSoftInputFromWindow(etInput);
                ivEmoji.setSelected(false);
                if (null != mChattingFooterListener) {
                    mChattingFooterListener.OnInEditMode();
                }
                break;
            case R.id.iv_voice:
                hideSoftInputFromWindow(ivVoice);
                extraView.setVisibility(GONE);
//                vpExtra.setVisibility(GONE);
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
                    extraView.setVisibility(VISIBLE);
//                    vpExtra.setVisibility(VISIBLE);
                    vpExtra.setAdapter(pagerAdapter);
                    pointIndicator.setIndicatorCount(vpExtra.getAdapter().getCount());
                    //TODO vpExtra 绑定表情PagerAdapter 这里统一绑定了ActionsPagerAdapter
                    setInputMode();
                    hideSoftInputFromWindow(etInput);
                } else {
                    setInputMode();
                    showSoftInputFromWindow(etInput);
                }
                if (null != mChattingFooterListener) {
                    mChattingFooterListener.OnInEditMode();
                }
                break;
            case R.id.iv_extra:
                extraView.setVisibility(VISIBLE);
//                vpExtra.setVisibility(VISIBLE);
                vpExtra.setAdapter(pagerAdapter);
                pointIndicator.setIndicatorCount(vpExtra.getAdapter().getCount());
                setInputMode();
                hideSoftInputFromWindow(ivExtra);
                btnVoice.setVisibility(GONE);
                etInput.setVisibility(VISIBLE);
                if (null != mChattingFooterListener) {
                    mChattingFooterListener.OnInEditMode();
                }
                break;
            case R.id.btn_send:
                if (null != mChattingFooterListener) {
                    mChattingFooterListener.OnSendTextMessageRequest(etInput.getText());
                    etInput.setText("");
                }
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
            if (extraView.getVisibility() == VISIBLE) {
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
        Log.d("tag", "keyboardHeight" + keyboardHeight);
        if (extraView.getVisibility() == VISIBLE && keyboardHeight > 200) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        } else {
            extraView.setVisibility(GONE);
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

    public static boolean checkDeviceHasNavigationBar(Context activity) {
        int id = activity.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        return (id > 0 && activity.getResources().getBoolean(id));
    }

    @Override
    public void onGlobalLayout() {
        Rect rootRect = new Rect();
        parentRoot.getWindowVisibleDisplayFrame(rootRect);
        int screenHeight = parentRoot.getRootView()
                .getHeight();
        int heightDifference = screenHeight
                - (rootRect.bottom - rootRect.top);

        if (heightDifference < 350 && heightDifference > 20) {
            extraBarHeight = heightDifference;
        }
        if (heightDifference > 350 && extraBarHeight > 20) {
            keyboardHeight = heightDifference - extraBarHeight;
            extraLp.height = keyboardHeight;
            PrefUtils.setKeyboardHeight(mContext, keyboardHeight);

        }

//        int statusResourceId = this.getResources()
//                .getIdentifier("status_bar_height",
//                        "dimen", "android");
//        int navigationResourceId = this.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
//        Log.d("tag", "onGlobalLayout " + "statusResourceId " + statusResourceId + "  " + heightDifference + " " + rootRect.height() + "  " + screenHeight);
//        Log.d("tag", "onGlobalLayout " + "navigationResourceId " + navigationResourceId);
//        int paddingTop;
//        if (statusResourceId > 0) {
//            paddingTop = parentRoot.getResources()
//                    .getDimensionPixelSize(statusResourceId);
//            heightDifference -= paddingTop;
//            Log.d("tag", "onGlobalLayout paddingTop" + paddingTop);
//        }
//        if (navigationResourceId > 0&&deviceHasNavigationBar) {
//            paddingTop = parentRoot.getResources()
//                    .getDimensionPixelSize(navigationResourceId);
//            heightDifference -= paddingTop;
//            Log.d("tag", "onGlobalLayout paddingTop" + paddingTop);
//        }
//        if (heightDifference > 200) {
//            keyboardHeight = heightDifference;
//            PrefUtils.setKeyboardHeight(mContext, keyboardHeight);
//            extraLp.height = keyboardHeight;
//
//        }

    }

    public boolean isShowExtra() {
        return extraView.getVisibility() == VISIBLE;
    }

    public void hideExtra() {
        extraView.setVisibility(GONE);
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
