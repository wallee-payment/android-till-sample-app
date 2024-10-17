package com.wallee.android.till.sample.till;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wallee.android.till.sample.till.common.GetAllCurrenciesUseCase;
import com.wallee.android.till.sample.till.databinding.AuthorizeTransactionActivityBinding;
import com.wallee.android.till.sample.till.model.Language;
import com.wallee.android.till.sample.till.model.Languages;
import com.wallee.android.till.sdk.ApiClient;
import com.wallee.android.till.sdk.TillLog;
import com.wallee.android.till.sdk.data.LineItem;
import com.wallee.android.till.sdk.data.Transaction;
import com.wallee.android.till.sdk.data.TransactionProcessingBehavior;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class AuthorizeTransactionActivity extends AppCompatActivity {

    private ApiClient client;
    private String languageCode;

    private AuthorizeTransactionActivityBinding binding;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AuthorizeTransactionActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setUpLanguagesSpinner();
        setupCurrencySpinner();
        setupOnClickListener();
        client = new ApiClient(new MockResponseHandler(this));
        client.bind(this);
        TillLog.debug("SampleApp: Wallee ApiClient is just bound!");
    }

    private void authorizeTransaction() {
        String amountString = getTextAsString(binding.editTextAmount);
        String currencyString = binding.currencySpinner.getSelectedItem().toString();
        String customTextString = getTextAsString(binding.editTextCustomText);
        String transactionRefNumber = getTextAsString(binding.editTextTransactionRef);

        // For credit transactions the value has to be negative Ex: -10.00

        if(binding.shouldReserve.isChecked() && binding.shouldAdjustReservation.isChecked()) {
            Toast.makeText(this, "Choose one: Reservation or Reservation adjustment", Toast.LENGTH_LONG).show();
        } else if (amountString.isEmpty()) {
            Toast.makeText(this, "Amount field is empty", Toast.LENGTH_LONG).show();
        } else if (binding.shouldAdjustReservation.isChecked() && transactionRefNumber.isEmpty()){
            Toast.makeText(this, "Reserve reference field is empty", Toast.LENGTH_LONG).show();
        } else {
            hideKeyboardFrom(AuthorizeTransactionActivity.this);

            TransactionProcessingBehavior behavior;
            if(binding.shouldReserve.isChecked()){
                behavior = TransactionProcessingBehavior.RESERVE;
            } else if(binding.shouldAdjustReservation.isChecked()){
                behavior = TransactionProcessingBehavior.ADJUST_RESERVATION;
            } else {
                behavior = TransactionProcessingBehavior.COMPLETE_IMMEDIATELY;
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
                    .setTransactionProcessingBehavior(behavior)
                    .setGeneratePanToken(binding.generatePanToken.isChecked());
            if (!customTextString.isEmpty()) {
                transactionBuilder.setCustomText(customTextString);
            }
            if (binding.selectLanguage.isChecked()) {
                transactionBuilder.setLanguage(languageCode);
            }
            if(binding.shouldAdjustReservation.isChecked()) {
                transactionBuilder.setTransactionRefNumber(transactionRefNumber);
            }
            transaction = transactionBuilder.build();
            TillLog.debug("VSD Start Transaction of amount  -> " + amountString);
            try {
                client.authorizeTransaction(transaction);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpLanguagesSpinner() {
        List<Language> languages = new Languages().getLanguages();
        ArrayAdapter arrayAdapter
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                languages);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                languageCode = languages.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.languageSpinner.setAdapter(arrayAdapter);
    }

    private void setupCurrencySpinner() {
        GetAllCurrenciesUseCase getAllCurrenciesUseCase = new GetAllCurrenciesUseCase();
        ArrayAdapter<com.wallee.android.till.sample.till.model.Currency> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.item_simple, getAllCurrenciesUseCase.execute());
             binding.currencySpinner.setAdapter(arrayAdapter);
    }

    private void setupOnClickListener() {
        binding.selectLanguage.setOnCheckedChangeListener((buttonView, isChecked) -> setVisibleOrGone(binding.languageSpinner, isChecked));

        binding.authorizeButton.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            authorizeTransaction();
        });

        binding.returnButton.setOnClickListener(v -> finish());

        binding.authorizeTransactionParent.setOnClickListener(v -> hideKeyboardFrom(AuthorizeTransactionActivity.this));
        binding.shouldAdjustReservation.setOnCheckedChangeListener((buttonView, isChecked) -> setVisibleOrGone(binding.editTextTransactionRef, isChecked));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        if (client != null) {
            client.unbind(this);
        }
    }

    private String getTextAsString(android.widget.EditText editText) {
        return editText.getText().toString().trim();
    }

    private void setVisibleOrGone(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(binding.authorizeTransactionParent.getWindowToken(), 0);
        }
    }
}
