package com.wallee.android.till.sample.till;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wallee.android.till.sdk.Utils;
import com.wallee.android.till.sdk.data.TransactionResponse;

public class TransactionResponseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_response_activity);

        TransactionResponse response = Utils.getTransactionResponse(getIntent().getExtras());

        if (response != null) {
            TextView textViewResult = findViewById(R.id.textViewResult);
            textViewResult.setText(responseToString(response));
        }

        findViewById(R.id.okButton).setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
                "receipts - " + response.getReceipts();
    }
}
