package com.example.quiz;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleButtonClick(R.id.false_button, R.string.correct_toast);
        handleButtonClick(R.id.true_button, R.string.incorrect_toast);
    }

    private void handleButtonClick(int btnId, int textResId) {
        findViewById(btnId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(MainActivity.this, textResId, Toast.LENGTH_SHORT);
//                https://developer.android.com/reference/android/widget/Toast.html#setGravity(int,%20int,%20int)
//                Warning: Starting from Android Build.VERSION_CODES#R, for apps targeting API level Build.VERSION_CODES#R or higher, this method is a no-op when called on text toasts.
//                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
            }
        });
    }
}