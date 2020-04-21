package com.deepak.library;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

public class PinView extends PinViewBaseHelper {

    private PinViewSettings mPinViewSettings;
    private OnCompleteListener onCompleteListener = null;


    public PinView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setTitles(String[] titles) {
        if (titles != null) {
            mLinearLayoutPinTexts.removeAllViews();
            mPinTitles = titles;
            pinTitlesIds = new int[titles.length];
            for (int i = 0; i < titles.length; i++) {
                mLinearLayoutPinTexts.addView(generatePinText(i, titles), i);
            }
        }
    }


    @Override
    public void setPin(int numberPinBoxes) {
        setPin(numberPinBoxes, -1);
    }


    public void setPin(int numberPinBoxes, int inputType) {
        if (numberPinBoxes <= 0) {
            numberPinBoxes = mNumberPinBoxes;
        }

        mLinearLayoutPinBoxes.removeAllViews();
        setNumberPinBoxes(numberPinBoxes);
        pinBoxesIds = new int[numberPinBoxes];
        pinSplitsIds = new int[numberPinBoxes - 1];
        int index = 0;

        for (int i = 0; i < numberPinBoxes; i++) {
            mLinearLayoutPinBoxes.addView(generatePinBox(i, inputType), index);
            if (mSplit != null && !mSplit.isEmpty() && i < numberPinBoxes - 1) {
                mLinearLayoutPinBoxes.addView(generateSplit(i), index + 1);
                mLinearLayoutPinBoxes.setGravity(Gravity.CENTER_VERTICAL);
                index += 2;
            } else {
                index++;
            }
        }
    }

    @Override
    protected void notifyPinViewCompleted() {
        if (onCompleteListener != null) {
            lastCompleted = true;
            onCompleteListener.onComplete(true, getPinResults());
        }
    }


    public String getPinResults() {

        String pinResults = "";
        for (int i = 0; i < mNumberPinBoxes; i++) {
            String pinBoxValue = getPinBox(i).getText().toString();
            if (pinBoxValue.isEmpty()) {
                pinBoxValue = " ";
            }
            pinResults += pinBoxValue;
        }
        return pinResults;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (getOnFocusChangeListener() != null) {
            getOnFocusChangeListener().onFocusChange(v, hasFocus);
        }

        EditText et = (EditText) v;
        if (et.getText().toString().length() >= 1 && hasFocus && isDeleteOnClick()) {
            et.getText().clear();
        }

        if (hasFocus) {
            setImeVisibility(true);

            if (onCompleteListener != null && lastCompleted) {
                lastCompleted = false;
                if (isDeleteOnClick()) {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }
    }


    public void clear() {

        for (int i = 0; i < mNumberPinBoxes; i++) {
            getPinBox(i).getText().clear();
        }
        checkPinBoxesAvailableOrder();
    }

    /**
     * Clear PinBoxes focus
     */
    public void resetChildrenFocus() {
        for (int pinBoxesId : pinBoxesIds) {
            EditText pin = (EditText) findViewById(pinBoxesId);
            pin.setOnFocusChangeListener(this);
        }
    }

    public boolean isKeyboardMandatory() {
        return mKeyboardMandatory;
    }

    public void setKeyboardMandatory(boolean keyboardMandatory) {
        this.mKeyboardMandatory = keyboardMandatory;
    }

    public boolean isMaskPassword() {
        return mMaskPassword;
    }

    public void setMaskPassword(boolean maskPassword) {
        setMaskPassword(maskPassword, true);
    }

    private void setMaskPassword(boolean maskPassword, boolean refresh) {
        this.mMaskPassword = maskPassword;
        if (refresh) {
            setStylesPinBoxes();
        }
    }

    public boolean isDeleteOnClick() {
        return mDeleteOnClick;
    }

    public void setDeleteOnClick(boolean deleteOnClick) {
        this.mDeleteOnClick = deleteOnClick;
    }

    public boolean isNativePinBox() {
        return mNativePinBox;
    }

    public void setNativePinBox(boolean nativePinBox) {
        setNativePinBox(nativePinBox, true);
    }

    private void setNativePinBox(boolean nativePinBox, boolean refresh) {
        this.mNativePinBox = nativePinBox;
        if (refresh) {
            setStylesPinBoxes();
        }
    }

    public void setCustomDrawablePinBox(int customDrawablePinBox) {
        setCustomDrawablePinBox(customDrawablePinBox, true);
    }

    private void setCustomDrawablePinBox(int customDrawablePinBox, boolean refresh) {
        if (customDrawablePinBox != 0) {
            this.mCustomDrawablePinBox = customDrawablePinBox;
            if (refresh) {
                setStylesPinBoxes();
            }
        }
    }

    public int getNumberCharacters() {
        return mNumberCharacters;
    }

    public void setNumberCharacters(int numberCharacters) {
        setNumberCharacters(numberCharacters, true);
    }

    private void setNumberCharacters(int numberCharacters, boolean refresh) {
        if (numberCharacters > 0) {
            this.mNumberCharacters = numberCharacters;
            if (refresh) {
                setStylesPinBoxes();
            }
        }
    }

    public String getSplit() {
        return mSplit;
    }

    public String getHint() {
        return hint;
    }

    public void setSplit(String split) {
        setSplit(split, true);
    }

    private void setSplit(String split, boolean refresh) {
        if (split != null && !split.isEmpty()) {
            this.mSplit = split;
            if (refresh) {
                setStylesSplits();
            }
        }
    }

    public float getSizeSplit() {
        return mSizeSplit;
    }

    public void setSizeSplit(float sizeSplit) {
        setSizeSplit(sizeSplit, true);
    }

    private void setSizeSplit(float sizeSplit, boolean refresh) {
        if (sizeSplit != 0) {
            mSizeSplit = sizeSplit;
            if (refresh) {
                setStylesSplits();
            }
        }
    }

    public float getTextSizePinBoxes() {
        return mTextSizePinBoxes;
    }

    public void setTextSizePinBoxes(float textSizePinBoxes) {
        setTextSizePinBoxes(textSizePinBoxes, true);
    }

    private void setTextSizePinBoxes(float textSizePinBoxes, boolean refresh) {
        if (textSizePinBoxes != 0) {
            mTextSizePinBoxes = textSizePinBoxes;
            if (refresh) {
                setStylesPinBoxes();
            }
        }
    }

    public float getTextSizeTitles() {
        return mTextSizeTitles;
    }

    public void setTextSizeTitles(float textSizeTitles) {
        setTextSizeTitles(textSizeTitles, true);
    }

    private void setTextSizeTitles(float textSizeTitles, boolean refresh) {
        if (textSizeTitles != 0) {
            mTextSizeTitles = textSizeTitles;
            if (refresh) {
                setStylePinTitles();
            }
        }
    }

    public String[] getPinTitles() {
        return mPinTitles;
    }

    public int getNumberPinBoxes() {
        return mNumberPinBoxes;
    }

    private void setNumberPinBoxes(int numberPinBoxes) {
        if (numberPinBoxes > 0) {
            this.mNumberPinBoxes = numberPinBoxes;
        }
    }

    /**
     * Set the text color for the Pin boxes
     *
     * @param color the color of the text
     */
    public void setColorTextPinBoxes(int color) {
        setColorTextPinBoxes(color, true);
    }

    private void setColorTextPinBoxes(int color, boolean refresh) {
        if (color != 0) {
            int newColor;
            try {
                newColor = getResources().getColor(color);
            } catch (Resources.NotFoundException e) {
                newColor = color;
            }
            mColorTextPinBoxes = newColor;
            if (refresh) {
                setStylesPinBoxes();
            }
        }
    }

    /**
     * Sets the text color for the titles
     *
     * @param color the color of the text
     */
    public void setColorTitles(int color) {
        setColorTitles(color, true);
    }

    private void setColorTitles(int color, boolean refresh) {
        if (color != 0) {
            int newColor;
            try {
                newColor = getResources().getColor(color);
            } catch (Resources.NotFoundException e) {
                newColor = color;
            }
            mColorTextTitles = newColor;
            if (refresh) {
                setStylePinTitles();
            }
        }
    }

    /**
     * Sets the color for the splits
     *
     * @param color the color of the mSplit
     */
    public void setColorSplit(int color) {
        setColorSplit(color, true);
    }

    private void setColorSplit(int color, boolean refresh) {
        if (color != 0) {
            int newColor;
            try {
                newColor = getResources().getColor(color);
            } catch (Resources.NotFoundException e) {
                newColor = color;
            }
            mColorSplit = newColor;
            if (refresh) {
                setStylesSplits();
            }
        }
    }


    /**
     * This method sets the desired functionalities of {@link PinView} to make easy.
     *
     * @param pinViewSettings Object with all functionalities to make easy.
     */
    public void setSettings(PinViewSettings pinViewSettings) {
        mPinViewSettings = pinViewSettings;
        setColorTextPinBoxes(mPinViewSettings.getColorTextPinBox(), false);
        setColorTitles(mPinViewSettings.getColorTextTitles(), false);
        setCustomDrawablePinBox(mPinViewSettings.getCustomDrawablePinBox(), false);
        setColorSplit(mPinViewSettings.getColorSplit(), false);
        setDeleteOnClick(mPinViewSettings.isDeleteOnClick());
        setNativePinBox(mPinViewSettings.isNativePinBox(), false);
        setMaskPassword(mPinViewSettings.isMaskPassword(), false);
        setKeyboardMandatory(mPinViewSettings.isKeyboardMandatory());
        setNumberCharacters(mPinViewSettings.getNumberCharacters(), false);
        setSplit(mPinViewSettings.getSplit(), false);
        setSizeSplit(mPinViewSettings.getSizeSplit(), false);
        setTextSizePinBoxes(mPinViewSettings.getTextSizePinBox(), false);
        setTextSizeTitles(mPinViewSettings.getTextSizeTitles(), false);
        setHint(mPinViewSettings.getHint());
        setTitles(mPinViewSettings.getPinTitles());
        setPin(mPinViewSettings.getNumberPinBoxes());
    }

    /**
     * Save the state of {@link PinView} when orientation screen changed.
     */
    @Override
    public Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        saveSettings();
        bundle.putParcelable("stateSettings", mPinViewSettings);
        //save everything
        bundle.putString("statePinResults", getPinResults());
        return bundle;
    }

    /**
     * Save current attributes in {@link PinView#mPinViewSettings}
     */
    private void saveSettings() {
        mPinViewSettings = new PinViewSettings.Builder()
                .withColorSplit(mColorSplit)
                .withColorTextPinBox(mColorTextPinBoxes)
                .withColorTextTitles(mColorTextTitles)
                .withCustomDrawablePinBox(mCustomDrawablePinBox)
                .withDeleteOnClick(isDeleteOnClick())
                .withNativePinBox(isNativePinBox())
                .withSplit(getSplit())
                .withMaskPassword(isMaskPassword())
                .withKeyboardMandatory(isKeyboardMandatory())
                .withNumberCharacters(getNumberCharacters())
                .withSizeSplit(getSizeSplit())
                .withTextSizePinBox(getTextSizePinBoxes())
                .withTextSizeTitles(getTextSizeTitles())
                .withNumberPinBoxes(getNumberPinBoxes())
                .withPinTitles(getPinTitles())
                .withHint(getHint())
                .build();
    }

    /**
     * Retrieve the state of {@link PinView} when orientation screen changed.
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            //load everything
            PinViewSettings pinViewSettings = bundle.getParcelable("stateSettings");
            if (pinViewSettings != null) {
                setSettings(pinViewSettings);
            }
            String pinResults = bundle.getString("statePinResults");
            setPinResults(pinResults);
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * Set a callback listener when {@link PinView} is fully completed or not
     *
     * @param listener Callback instance.
     */
    public void setOnCompleteListener(OnCompleteListener listener) {
        onCompleteListener = listener;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    /**
     * Interface for a callback when {@link PinView} is fully completed or not.
     * Container Activity/Fragment must implement this interface
     */
    public interface OnCompleteListener {

        void onComplete(boolean completed, String pinResults);

    }
}
