package com.wallee.android.till.sample.till;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wallee.android.till.sdk.ApiClient;
import com.wallee.android.till.sdk.data.LineItem;
import com.wallee.android.till.sdk.data.Transaction;
import com.wallee.android.till.sdk.data.TransactionProcessingBehavior;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class AuthorizeTransactionActivity extends AppCompatActivity {
    private ApiClient client;
    private String languageCode;
    private Spinner languageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorize_transaction_activity);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final EditText amountField = findViewById(R.id.editTextAmount);
        final EditText currencyField = findViewById(R.id.editTextCurrency);
        final EditText customTextField = findViewById(R.id.editTextCustomText);
        final CheckBox shouldReserve = findViewById(R.id.shouldReserve);
        final CheckBox selectLanguage = findViewById(R.id.selectLanguage);
        languageSpinner = findViewById(R.id.languageSpinner);


        selectLanguage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    languageSpinner.setVisibility(View.VISIBLE);
                } else {
                    languageSpinner.setVisibility(View.INVISIBLE);
                }
            }
        });

        setUpLanguagesSpinner();

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

            if (selectLanguage.isChecked())
                transactionBuilder.setLanguage(languageCode);

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

    private void setUpLanguagesSpinner() {
        ArrayList<Language> languages = new Languages().getLanguages();
        ArrayAdapter arrayAdapter
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                languages);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);;
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                languageCode = languages.get(position).getCode();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        languageSpinner.setAdapter(arrayAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.unbind(this);
    }
}