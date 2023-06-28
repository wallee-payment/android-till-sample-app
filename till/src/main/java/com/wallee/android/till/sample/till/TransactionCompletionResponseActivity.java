package com.wallee.android.till.sample.till;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wallee.android.till.sample.till.model.ErrorCode;
import com.wallee.android.till.sdk.Utils;
import com.wallee.android.till.sdk.data.TransactionCompletionResponse;

public class TransactionCompletionResponseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_completion_response_activity);

        TransactionCompletionResponse response = Utils.getTransactionCompletionResponse(getIntent().getExtras());

        if (response != null) {
            if (response.getResultCode().getCode().equals(ErrorCode.ERR_CONNECTION_FAILED.getCode())) {
                com.wallee.android.till.sample.till.Utils.showToast(this,getResources().getString(R.string.app_relaunch));
                Utils.handleFailedToConnectVpj(this);
            }
            TextView textViewResult = findViewById(R.id.textViewResult);
            textViewResult.setText(responseToString(response));
        }

        findViewById(R.id.okButton).setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private String responseToString(TransactionCompletionResponse response) {
        return "state - " + response.getState() + "\n" +
                "resultCode code - " + response.getResultCode().getCode() + "\n" +
                "resultCode description - " + response.getResultCode().getDescription() + "\n" +
                "amount - " + response.getTransactionCompletion().getTotalAmountIncludingTax().toString() + "\n" +
                "reserveReference - " + response.getTransactionCompletion().getReserveReference() + "\n" +
                "authorizationCode - " + response.getAuthorizationCode() + "\n" +
                "terminalId - " + response.getTerminalId() + "\n" +
                "sequenceCount - " + response.getSequenceCount() + "\n" +
                "transactionTime - " + response.getTransactionTime() + "\n" +
                "receipts - " + response.getReceipts();
    }
}
