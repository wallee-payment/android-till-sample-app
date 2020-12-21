package com.wallee.android.till.sample.till;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.checkApiServiceCompatibility).setOnClickListener(v -> {
            startActivity(new Intent(this, CheckApiServiceCompatibilityActivity.class));
        });

        findViewById(R.id.authorizeTransaction).setOnClickListener(v -> {
            startActivity(new Intent(this, AuthorizeTransactionActivity.class));
        });

        findViewById(R.id.completeTransaction).setOnClickListener(v -> {
            startActivity(new Intent(this, CompleteTransactionActivity.class));
        });

        findViewById(R.id.voidTransaction).setOnClickListener(v -> {
            startActivity(new Intent(this, VoidTransactionActivity.class));
        });

        findViewById(R.id.cancelLastTransactionOperation).setOnClickListener(v -> {
            startActivity(new Intent(this, CancelLastTransactionOperationActivity.class));
        });

        findViewById(R.id.executeTransmission).setOnClickListener(v -> {
            startActivity(new Intent(this, ExecuteTransmissionActivity.class));
        });

        findViewById(R.id.executeSubmission).setOnClickListener(v -> {
            startActivity(new Intent(this, ExecuteSubmissionActivity.class));
        });

        findViewById(R.id.executeFinalBalance).setOnClickListener(v -> {
            startActivity(new Intent(this, ExecuteFinalBalanceActivity.class));
        });
    }
}
