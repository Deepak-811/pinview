package com.deepak.pinview;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.deepak.library.PinView;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private PinView pinview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pinview = findViewById(R.id.pinview);

        
        pinview.setPinCompleteListener(new PinView.PinCompleteListener() {
            @Override
            public void onCompletePin(String result, boolean isCompleted) {
                Toast.makeText(MainActivity.this, "Completed: " + isCompleted + "\nValue: " + result,
                        Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Getting the pin result
         */
        Log.e(TAG, "onComplete: "+pinview.getValues() );

    }
}
