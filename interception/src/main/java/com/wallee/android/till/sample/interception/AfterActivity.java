package com.wallee.android.till.sample.interception;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.wallee.android.till.sample.interception.response.TransactionResponseActivity;

public class AfterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after);

        findViewById(R.id.continueButton).setOnClickListener(v -> {
            setResult(Activity.RESULT_OK, getIntent());
            finish();
        });

        findViewById((R.id.resultButton)).setOnClickListener(v -> {
            Intent intent = new Intent(this, TransactionResponseActivity.class);
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
        });
    }
}