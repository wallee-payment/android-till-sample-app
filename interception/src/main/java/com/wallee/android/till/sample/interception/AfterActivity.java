package com.wallee.android.till.sample.interception;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AfterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after);

        findViewById(R.id.continueButton).setOnClickListener(v -> {
            setResult(Activity.RESULT_OK, getIntent());
            finish();
        });
    }
}