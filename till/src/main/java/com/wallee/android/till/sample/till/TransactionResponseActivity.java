package com.wallee.android.till.sample.till;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wallee.android.till.sample.till.model.ErrorCode;
import com.wallee.android.till.sdk.TillLog;
import com.wallee.android.till.sdk.Utils;
import com.wallee.android.till.sdk.data.TransactionResponse;

public class TransactionResponseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_response_activity);

        TransactionResponse response = Utils.getTransactionResponse(getIntent().getExtras());

        TillLog.debug("Log transaction response  -> " + responseToString(response));


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
                "panToken - " + response.getPanToken();

    }
}
