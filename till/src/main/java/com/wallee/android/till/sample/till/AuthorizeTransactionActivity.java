package com.wallee.android.till.sample.till;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final EditText amountField = findViewById(R.id.editTextAmount);
        final EditText currencyField = findViewById(R.id.editTextCurrency);
        final EditText customTextField = findViewById(R.id.editTextCustomText);
        final CheckBox shouldReserve = findViewById(R.id.shouldReserve);

        findViewById(R.id.authorizeButton).setOnClickListener(view -> {
            String amountString = amountField.getText().toString();
            String currencyString = currencyField.getText().toString();
            String customTextString = customTextField.getText().toString();
            if (amountString.isEmpty() || currencyString.isEmpty()) {
                Toast.makeText(this, "Please fill Amount and Currency fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            List<LineItem> lineItems = new LineItem.ListBuilder("foo", new BigDecimal(amountString))
                    .getCurrent()
                    .setName("bar")
                    .getListBuilder()
                    .build();

            Transaction transaction;
            Transaction.Builder transactionBuilder = new Transaction.Builder(lineItems)
                    .setCurrency(Currency.getInstance(currencyString))
                    .setInvoiceReference("1")
                    .setMerchantReference("MREF-123")
                    .setTransactionProcessingBehavior(
                            shouldReserve.isChecked() ? TransactionProcessingBehavior.RESERVE : TransactionProcessingBehavior.COMPLETE_IMMEDIATELY
                    );
            if (!customTextString.isEmpty()) {
                transactionBuilder.setCustomText(customTextString);
            }
            transaction = transactionBuilder.build();

            try {
                client.authorizeTransaction(transaction);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.returnButton).setOnClickListener(view -> finish());

        findViewById(R.id.authorizeTransactionParent).setOnClickListener(view -> Utils.hideKeyboardFrom(this));

        client = new ApiClient(new MockResponseHandler(this));
        client.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.unbind(this);
    }
}