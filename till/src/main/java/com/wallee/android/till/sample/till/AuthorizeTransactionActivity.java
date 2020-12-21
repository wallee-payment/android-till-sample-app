package com.wallee.android.till.sample.till;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.wallee.android.till.sdk.ApiClient;
import com.wallee.android.till.sdk.data.LineItem;
import com.wallee.android.till.sdk.data.Transaction;
import com.wallee.android.till.sdk.data.TransactionProcessingBehavior;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class AuthorizeTransactionActivity extends AppCompatActivity {
    private ApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorize_transaction_activity);

        final EditText amountField = findViewById(R.id.editTextAmount);
        final EditText currencyField = findViewById(R.id.editTextCurrency);
        final CheckBox shouldReserve = findViewById(R.id.shouldReserve);

        findViewById(R.id.authorizeButton).setOnClickListener(view -> {
            String amountString = amountField.getText().toString();
            String currencyString = currencyField.getText().toString();

            List<LineItem> lineItems = new LineItem.ListBuilder("foo", new BigDecimal(amountString))
                    .getCurrent()
                    .setName("bar")
                    .getListBuilder()
                    .build();

            Transaction transaction = new Transaction.Builder(lineItems)
                    .setCurrency(Currency.getInstance(currencyString))
                    .setInvoiceReference("1")
                    .setMerchantReference("MREF-123")
                    .setTransactionProcessingBehavior(
                            shouldReserve.isChecked() ? TransactionProcessingBehavior.RESERVE : TransactionProcessingBehavior.COMPLETE_IMMEDIATELY
                    )
                    .build();

            try {
                client.authorizeTransaction(transaction);
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