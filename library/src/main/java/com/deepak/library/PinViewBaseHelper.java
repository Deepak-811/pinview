package com.deepak.library;

import android.app.Service;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class PinViewBaseHelper extends LinearLayout implements TextWatcher, View.OnFocusChangeListener {

    private static final String LOG_TAG = PinViewBaseHelper.class.getSimpleName();

    /**
     * Attributes
     */
    int mNumberPinBoxes         = PinViewSettings.DEFAULT_NUMBER_PIN_BOXES;
    int mNumberCharacters       = PinViewSettings.DEFAULT_NUMBER_CHARACTERS;
    String mSplit               = PinViewSettings.DEFAULT_SPLIT;
    String hint                 = "*";
    boolean mKeyboardMandatory  = PinViewSettings.DEFAULT_KEYBOARD_MANDATORY;
    boolean mDeleteOnClick      = PinViewSettings.DEFAULT_DELETE_ON_CLICK;
    boolean mMaskPassword       = PinViewSettings.DEFAULT_MASK_PASSWORD;
    boolean mNativePinBox       = PinViewSettings.DEFAULT_NATIVE_PIN_BOX;
    int mCustomDrawablePinBox   = PinViewSettings.DEFAULT_CUSTOM_PIN_BOX;
    int mColorTextPinBoxes      = PinViewSettings.DEFAULT_TEXT_COLOR_PIN_BOX;
    int mColorTextTitles        = PinViewSettings.DEFAULT_TEXT_COLOR_TITLES;
    int mColorSplit             = PinViewSettings.DEFAULT_COLOR_SPLIT;
    float mTextSizePinBoxes;
    float mTextSizeTitles;
    float mSizeSplit;
    String[] mPinTitles;

    private int currentFocus;
    boolean lastCompleted = false;
    private InputMethodManager inputMethodManager;
    LinearLayout mLinearLayoutPinTexts;
    LinearLayout mLinearLayoutPinBoxes;
    int[] pinBoxesIds;
    int[] pinTitlesIds;
    int[] pinSplitsIds;


    public PinViewBaseHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            createEditModeView(context);
        } else {
            createView(context);
            getAttributes(context, attrs);
        }
    }


    private void createEditModeView(Context context) {
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.pin_view_edit_mode, this,
                        true);
    }

    /**
     * This method inflates the PinView
     *
     * @param context {@link PinViewBaseHelper} needs a context to inflate the layout
     */
    private void createView(Context context) {
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.pin_view, this,
                true);
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
        mLinearLayoutPinTexts = (LinearLayout) findViewById(R.id.ll_pin_texts);
        mLinearLayoutPinBoxes = (LinearLayout) findViewById(R.id.ll_pin_edit_texts);
    }

    /**
     * Retrieve styles attributes
     */
    private void getAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinView);

        if (typedArray != null) {
            try {
                mNumberPinBoxes = typedArray
                        .getInteger(R.styleable.PinView_numberPinBoxes, PinViewSettings.DEFAULT_NUMBER_PIN_BOXES);
                mMaskPassword = typedArray
                        .getBoolean(R.styleable.PinView_password, PinViewSettings.DEFAULT_MASK_PASSWORD);
                mNumberCharacters = typedArray
                        .getInteger(R.styleable.PinView_numberCharacters, PinViewSettings.DEFAULT_NUMBER_CHARACTERS);
                mSplit = typedArray.getString(R.styleable.PinView_split);
                hint   = typedArray.getString(R.styleable.PinView_pinHint);
                mKeyboardMandatory = typedArray
                        .getBoolean(R.styleable.PinView_keyboardMandatory, PinViewSettings.DEFAULT_KEYBOARD_MANDATORY);
                mDeleteOnClick = typedArray
                        .getBoolean(R.styleable.PinView_deleteOnClick, PinViewSettings.DEFAULT_DELETE_ON_CLICK);
                mNativePinBox = typedArray
                        .getBoolean(R.styleable.PinView_nativePinBox, PinViewSettings.DEFAULT_NATIVE_PIN_BOX);
                mCustomDrawablePinBox = typedArray
                        .getResourceId(R.styleable.PinView_drawablePinBox, PinViewSettings.DEFAULT_CUSTOM_PIN_BOX);
                mColorTextPinBoxes = typedArray
                        .getColor(R.styleable.PinView_colorTextPinBox,
                                getResources().getColor(PinViewSettings.DEFAULT_TEXT_COLOR_PIN_BOX));
                mColorTextTitles = typedArray
                        .getColor(R.styleable.PinView_colorTextTitles,
                                getResources().getColor(PinViewSettings.DEFAULT_TEXT_COLOR_TITLES));
                mColorSplit = typedArray
                        .getColor(R.styleable.PinView_colorSplit,
                                getResources().getColor(PinViewSettings.DEFAULT_COLOR_SPLIT));
                mTextSizePinBoxes = typedArray
                        .getDimension(R.styleable.PinView_textSizePinBox,
                                getResources().getDimension(PinViewSettings.DEFAULT_TEXT_SIZE_PIN_BOX));
                mTextSizeTitles = typedArray
                        .getDimension(R.styleable.PinView_textSizeTitles,
                                getResources().getDimension(PinViewSettings.DEFAULT_TEXT_SIZE_TITLES));
                mSizeSplit = typedArray
                        .getDimension(R.styleable.PinView_sizeSplit,
                                getResources().getDimension(PinViewSettings.DEFAULT_SIZE_SPLIT));

                int titles;
                titles = typedArray.getResourceId(R.styleable.PinView_titles, -1);
                if (titles != -1) {
                    setTitles(getResources().getStringArray(titles));
                }

                if (this.mNumberPinBoxes != 0) {
                    setPin(this.mNumberPinBoxes);
                }

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error while creating the view PinView: ", e);
            } finally {
                typedArray.recycle();
            }
        }
    }



    EditText generatePinBox(int i, int inputType) {
        EditText editText = (EditText) LayoutInflater.from(getContext()).inflate(R.layout.partial_pin_box, this, false);
        int generateViewId = PinViewUtils.generateViewId();
        editText.setId(generateViewId);
        editText.setTag(i);
        if (inputType != -1) {
            editText.setInputType(inputType);
        }
        setStylePinBox(editText);
        setHintPin(editText);

        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);
        pinBoxesIds[i] = generateViewId;

        return editText;
    }

    private void setHintPin(EditText editText) {
        editText.setHint(hint);
    }

    /**
     * Set a PinBox with all attributes
     *
     * @param editText to set attributes
     */
    private void setStylePinBox(EditText editText) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mNumberCharacters)});

        if (mMaskPassword) {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        else{
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }

        if (mNativePinBox) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                //noinspection deprecation
                editText.setBackgroundDrawable(new EditText(getContext()).getBackground());
            } else {
                editText.setBackground(new EditText(getContext()).getBackground());
            }
        } else {
            editText.setBackgroundResource(mCustomDrawablePinBox);
        }

        if (mColorTextPinBoxes != PinViewSettings.DEFAULT_TEXT_COLOR_PIN_BOX) {
            editText.setTextColor(mColorTextPinBoxes);
        }
        editText.setTextSize(PinViewUtils.convertPixelToDp(getContext(), mTextSizePinBoxes));
    }


    TextView generateSplit(int i) {
        TextView split = new TextView(getContext());
        int generateViewId = PinViewUtils.generateViewId();
        split.setId(generateViewId);
        setStylesSplit(split);
        pinSplitsIds[i] = generateViewId;
        return split;
    }


    TextView generatePinText(int i, String[] titles) {
        TextView pinTitle = (TextView) LayoutInflater.from(getContext())
                .inflate(R.layout.partial_pin_text, this, false);
        int generateViewId = PinViewUtils.generateViewId();
        pinTitle.setId(generateViewId);
        pinTitle.setText(titles[i]);
        setStylesPinTitle(pinTitle);
        pinTitlesIds[i] = generateViewId;
        return pinTitle;
    }


    private void setStylesPinTitle(TextView pinTitle) {
        if (mColorTextTitles != PinViewSettings.DEFAULT_TEXT_COLOR_TITLES) {
            pinTitle.setTextColor(mColorTextTitles);
        }
        pinTitle.setTextSize(PinViewUtils.convertPixelToDp(getContext(), mTextSizeTitles));
    }


    void setStylesPinBoxes() {
        for (int i = 0; i < mNumberPinBoxes; i++) {
            setStylePinBox(getPinBox(i));
        }
    }


    void setStylePinTitles() {
        for (int i = 0; i < mPinTitles.length; i++) {
            setStylesPinTitle(getPinTitle(i));
        }
    }


    void setStylesSplits() {
        for (int i = 0; i < pinSplitsIds.length; i++) {
            setStylesSplit(getSplit(i));
        }
    }


    private void setStylesSplit(TextView split) {
        if(split!=null){
            split.setText(mSplit);
            split.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            split.setGravity(Gravity.CENTER_VERTICAL);
            if (mColorSplit != PinViewSettings.DEFAULT_COLOR_SPLIT) {
                split.setTextColor(mColorSplit);
            }
            split.setTextSize(PinViewUtils.convertPixelToDp(getContext(), mSizeSplit));
        }
    }


    private void chooseNextAction(int index) {
        if (index == -1) {
            notifyPinViewCompleted();
        } else {
            moveToPinBox(index);
        }
    }


    private void moveToPinBox(int index) {
        findViewById(pinBoxesIds[index]).requestFocus();
    }

    EditText getPinBox(int i) {
        return (EditText) findViewById(pinBoxesIds[i]);
    }

    private TextView getPinTitle(int i) {
        return (TextView) findViewById(pinTitlesIds[i]);
    }

    private TextView getSplit(int i) {
        return (TextView) findViewById(pinSplitsIds[i]);
    }


    private void checkPinBoxesAvailable() {

        int index = -1;
        int i = currentFocus + 1;
        while (i != currentFocus) {

            if (i > (mNumberPinBoxes - 1)) {
                i = 0;
            }

            if (pinBoxIsEmpty(i)) {
                index = i;
                break;
            }
            i++;
        }
        chooseNextAction(index);
    }


    void setPinResults(String pinResults) {
        for (int i = 0; i < mNumberPinBoxes; i++) {
            if (pinResults != null) {
                int start = i*mNumberCharacters;
                String valuePinBox = pinResults.substring(start, start + mNumberCharacters);
                if (!valuePinBox.trim().isEmpty()) {
                    getPinBox(i).setText(valuePinBox);
                }
                else{
                    break;
                }
            }
        }
    }


    void checkPinBoxesAvailableOrder() {

        int index = -1;
        for (int i = 0; i < mNumberPinBoxes; i++) {
            if (pinBoxIsEmpty(i)) {
                index = i;
                break;
            }
        }
        chooseNextAction(index);
    }


    private boolean pinBoxIsEmpty(int i) {
        return getPinBox(i).getText().toString().isEmpty();
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (findFocus() != null) {
            currentFocus = Integer.parseInt(findFocus().getTag().toString());
        }

        if (count == 1 && s.length() == mNumberCharacters) {
            if (currentFocus == (mNumberPinBoxes - 1) || currentFocus == 0) {
                checkPinBoxesAvailableOrder();
            } else {
                checkPinBoxesAvailable();
            }

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    /**
     * Keyboard back button
     */
    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {

        if (mKeyboardMandatory) {
            if (getContext() != null) {
                InputMethodManager imm = (InputMethodManager) getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm.isActive() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    setImeVisibility(true);
                    return true;
                }
            }
        }
        return super.dispatchKeyEventPreIme(event);
    }

    void setImeVisibility(final boolean visible) {
        if (visible) {
            post(mShowImeRunnable);
        } else {
            removeCallbacks(mShowImeRunnable);
            PinViewUtils.hideKeyboard(getContext());
        }
    }

    private final Runnable mShowImeRunnable = new Runnable() {
        public void run() {
            if (findFocus() != null) {
                inputMethodManager.showSoftInput(findFocus(), InputMethodManager.SHOW_FORCED);
            }
        }
    };

    public abstract void setTitles(String[] titles);

    public abstract void setPin(int numberPinBoxes);

    protected abstract void notifyPinViewCompleted();
}
