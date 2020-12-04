package com.wallee.android.till.sample.till;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.wallee.android.till.sdk.ApiClient;

public class CancelLastTransactionOperationActivity extends AppCompatActivity {
    private ApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_last_transaction_operation_activity);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        findViewById(R.id.cancelButton).setOnClickListener(view -> {
            try {
                progressBar.setVisibility(View.VISIBLE);
                client.cancelLastTransactionOperation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.returnButton).setOnClickListener(view -> finish());

        client = new ApiClient(new MockResponseHandler(this));
        client.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.unbind(this);
    }
}
