package com.wallee.android.till.sample.interception.response;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.wallee.android.till.sdk.TillLog;
import com.wallee.android.till.sdk.Utils;
import com.wallee.android.till.sdk.data.TransactionResponse;

public class TransactionResponseActivity extends AppCompatActivity {
    private String TAG = "TransactionResponseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.wallee.android.till.sample.interception.R.layout.transaction_response_activity);
        TransactionResponse response = Utils.getTransactionResponse(getIntent().getExtras());
        TillLog.debug("Log transaction response  -> " + responseToString(response));

        if (response != null) {
            TextView textViewResult = findViewById(com.wallee.android.till.sample.interception.R.id.textViewResult);
            textViewResult.setText(responseToString(response));
        }

        findViewById(com.wallee.android.till.sample.interception.R.id.okButton).setOnClickListener(view -> {
            finish();
        });
    }

    private String responseToString(TransactionResponse response) {
        return "state - " + response.getState() + "\n" +
                "resultCode code - " + response.getResultCode().getCode() + "\n" +
                "resultCode description - " + response.getResultCode().getDescription() + "\n" +
                "amount - " + response.getTransaction().getTotalAmountIncludingTax().toString() + "\n" +
                "authorizationCode - " + response.getAuthorizationCode() + "\n" +
                "terminalId - " + response.getTerminalId() + "\n" +
                "sequenceCount - " + response.getSequenceCount() + "\n" +
                "transactionTime - " + response.getTransactionTime() + "\n" +
                "reserveReference - " + response.getReserveReference() + "\n" +
                "acquirerId - " + response.getAcquirerId() + "\n" +
                "receipts - " + response.getReceipts() + "\n" +
                "cardIssuingCountry - " + response.getCardIssuingCountry() + "\n" +
                "cardAppLabel - " + response.getCardAppLabel() + "\n" +
                "cardAppId - " + response.getCardAppId() + "\n" +
                "amountTip - " + response.getAmountTip() + "\n" +
                "panToken - " + response.getPanToken() + "\n" +
                "merchantReference - " + response.getMerchantReference() + "\n" +
                "transactionSyncNumber - " + response.getTransactionSyncNumber() + "\n" +
                "paymentEntryMethod - " + response.getPaymentEntryMethod();
    }
}
