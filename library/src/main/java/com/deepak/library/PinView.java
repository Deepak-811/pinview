package com.deepak.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;

public class PinView extends LinearLayout implements TextWatcher {

    public static final String TAG = PinView.class.getSimpleName();
    private TypedArray typedArray;
    private Context context;

    /**
     * Default pin size
     */
    private int defaultPinSize = 4;

    /**
     * Default box width
     */
    private float boxWidth = 40f;

    /**
     * Default box height
     */
    private float boxHeight = 40f;

    /**
     * Default box spacing
     */
    private float boxSpacing = 8f;

    /**
     * Default pin hint
     */
    private String hint = "*";

    /**
     * Default box background
     */
    private int resource = R.drawable.sample_background;

    private int currentPosition = 0;
    private LayoutParams layoutParams;
    private AttributeSet attrs;
    private PinCompleteListener pinCompleteListener;
    private InputFilter[] filterArray = new InputFilter[1];


    public PinView(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public PinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        init(context, attrs);
    }

    public PinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        init(context, attrs);
    }

    public void setPinCompleteListener(PinCompleteListener pinCompleteListener) {
        this.pinCompleteListener = pinCompleteListener;
    }

    private float dpTopixel(Context c, float dp) {
        if (dp == 0 || dp == 0.0) {
            dp = 40f;
        }
        float density = c.getResources().getDisplayMetrics().density;
        float pixel = dp * density;
        return pixel;
    }

    public String getValues() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");

        for (int i = 0; i < defaultPinSize; i++) {
            EditText editText = (EditText) getChildAt(i);
            stringBuilder.append(editText.getText());
        }

        return stringBuilder.toString();
    }


    public void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinView, 0, 0);
            defaultPinSize = typedArray.getInt(R.styleable.PinView_pinSize, 4);
            hint = typedArray.getString(R.styleable.PinView_pinHint);
            boxHeight = typedArray.getDimension(R.styleable.PinView_boxHeight, -1);
            boxWidth = typedArray.getDimension(R.styleable.PinView_boxWidth, -1);
            boxSpacing = typedArray.getDimension(R.styleable.PinView_boxSpacing, -1);
            resource = typedArray.getResourceId(R.styleable.PinView_boxBackground, R.drawable.sample_background);
        }


        if (boxSpacing == -1) {
            boxSpacing = dpTopixel(context, 8f);
        }

        if (boxWidth == -1) {
            boxWidth = dpTopixel(context, 40f);
            boxHeight = dpTopixel(context, 40f);
        }

        if (boxHeight == -1) {
            boxWidth = dpTopixel(context, 40f);
            boxHeight = dpTopixel(context, 40f);
        }

        if (hint == null || hint.isEmpty()) {
            hint = "*";
        }


        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        removeAllViews();
        for (int i = 0; i < defaultPinSize; i++) {
            EditText editText = new EditText(context);
            editText.setBackgroundResource(resource);
            editText.setMaxLines(1);
            filterArray[0] = new InputFilter.LengthFilter(1);
            editText.setFilters(filterArray);
            editText.setHint(hint);
            layoutParams = new LayoutParams((int) boxWidth, (int) boxHeight);
            layoutParams.leftMargin = (int) boxSpacing;
            layoutParams.topMargin = (int) boxSpacing;
            layoutParams.bottomMargin = (int) boxSpacing;
            layoutParams.rightMargin = (int) boxSpacing;
            editText.setPadding(8, 4, 8, 4);
            editText.setLayoutParams(layoutParams);
            editText.setGravity(Gravity.CENTER);
            editText.setClickable(false);
            editText.addTextChangedListener(this);
            addView(editText);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {


        if (s.length() == 1) {
            if (currentPosition == defaultPinSize - 1) {

            } else {
                EditText editText = (EditText) getChildAt(currentPosition + 1);
                editText.requestFocus();
                currentPosition++;
            }
        } else {
            if (currentPosition > 0) {
                currentPosition--;
            }
            // currentPosition--;
            EditText editText = (EditText) getChildAt(currentPosition);
            editText.requestFocus();

        }

        if (pinCompleteListener!=null){
            if (defaultPinSize==getValues().length()){
                pinCompleteListener.onCompletePin(getValues(),true);
            }else {
                pinCompleteListener.onCompletePin(getValues(),false);
            }
        }

    }

    public interface PinCompleteListener {
        void onCompletePin(String result,boolean isCompleted);
    }

}
