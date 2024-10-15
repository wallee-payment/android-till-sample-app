package com.wallee.android.till.sample.till;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wallee.android.till.sdk.ApiClient;
import com.wallee.android.till.sdk.data.LineItem;
import com.wallee.android.till.sdk.data.TransactionCompletion;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class CompleteTransactionActivity extends AppCompatActivity {
    private ApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_transaction_activity);

        final EditText amountField = findViewById(R.id.editTextAmount);
        final EditText editTextCurrency = findViewById(R.id.editTextCurrency);
        final EditText editTextReference = findViewById(R.id.editTextReference);

        findViewById(R.id.completeButton).setOnClickListener(view -> {
            String amountString = amountField.getText().toString();
            String currencyString = editTextCurrency.getText().toString();
            String referenceString = editTextReference.getText().toString();

            if(referenceString.isEmpty()){
                Toast.makeText(this, "Reference field must not be empty", Toast.LENGTH_SHORT).show();
            } else {
                List<LineItem> lineItems = new LineItem.ListBuilder("foo", new BigDecimal(amountString))
                        .getCurrent()
                        .setName("bar")
                        .getListBuilder()
                        .build();

                TransactionCompletion transaction = new TransactionCompletion.Builder(lineItems)
                        .setCurrency(Currency.getInstance(currencyString))
                        .setReserveReference(referenceString)
                        .build();

                try {
                    client.completeTransaction(transaction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
