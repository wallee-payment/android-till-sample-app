package com.wallee.android.till.sample.till;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.wallee.android.till.sdk.ApiClient;
import com.wallee.android.till.sdk.data.TransactionVoid;

public class VoidTransactionActivity extends AppCompatActivity {
    private ApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.void_transaction_activity);

        final EditText editTextReference = findViewById(R.id.editTextReference);
        final EditText editTextAcquirerId = findViewById(R.id.editTextAcquirerId);

        findViewById(R.id.voidButton).setOnClickListener(view -> {
            String referenceString = editTextReference.getText().toString();
            try {
                client.voidTransaction(new TransactionVoid(Long.valueOf(referenceString), editTextAcquirerId.getText().toString()));
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
