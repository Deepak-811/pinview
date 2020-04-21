package com.deepak.pinview;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.deepak.library.PinView;
import com.deepak.library.PinViewSettings;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private PinView pinview,pinview_prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pinview = findViewById(R.id.pinview);
        pinview_prog = findViewById(R.id.pinview_prog);

        /**
         * Setting pinview programmatically
         */

        PinViewSettings pinViewSettings = new PinViewSettings.Builder()
                .withMaskPassword(true)
                .withDeleteOnClick(true)
                .withKeyboardMandatory(false)
                .withSplit(null)
                .withHint("#")
                .withNumberPinBoxes(5)
                .withNativePinBox(false)
                .build();

        pinview_prog.setSettings(pinViewSettings);

        pinview_prog.setOnCompleteListener(new PinView.OnCompleteListener() {
            @Override
            public void onComplete(boolean completed, String pinResults) {
                Toast.makeText(MainActivity.this, "Completed: pinview_prog--" + completed + "\nValue: " + pinResults,
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onComplete: "+pinview_prog.getPinResults() );
            }
        });

        /**
         * Setting by xml
         */
        pinview.setOnCompleteListener(new PinView.OnCompleteListener() {
            @Override
            public void onComplete(boolean completed, String pinResults) {
                Toast.makeText(MainActivity.this, "Completed: " + completed + "\nValue: " + pinResults,
                        Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Getting the pin result
         */
        Log.e(TAG, "onComplete: "+pinview_prog.getPinResults() );

    }
}
