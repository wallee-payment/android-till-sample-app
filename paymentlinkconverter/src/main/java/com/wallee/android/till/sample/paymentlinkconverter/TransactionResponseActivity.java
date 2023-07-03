package com.wallee.android.till.sample.paymentlinkconverter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wallee.android.till.sdk.TillLog;
import com.wallee.android.till.sdk.Utils;
import com.wallee.android.till.sdk.data.TransactionResponse;

import java.util.List;

public class TransactionResponseActivity extends AppCompatActivity {

    private final String TAG = "TransactionResponse";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_response_activity);
        TransactionResponse response = Utils.getTransactionResponse(getIntent().getExtras());
        TillLog.debug("Log transaction response  -> " + responseToString(response));

        if (response != null) {
            sendPaymentLinkResponse(response.getResultCode().getCode());
        } else {
            Log.e(TAG, "response should not be null");
            sendPaymentLinkResponse("-1");
        }

        findViewById(R.id.exitButton).setOnClickListener(view -> {
            MainActivity app = (MainActivity) getApplicationContext();
            app.finish();
        });
    }

    private void showError(String message) {
        TextView textViewResult = findViewById(R.id.textViewError);
        textViewResult.setText(message);
    }

    private void sendPaymentLinkResponse(String resultCode) {
        Log.d(TAG, "sendPaymentLinkResponse called @ resultCode = " + resultCode);
        String status = (resultCode.equals("0") ? "0" : "-1");

        PaymentLinkData app = (PaymentLinkData) getApplicationContext();
        String destination = app.getPaymentLinkClientDestination();

        if (destination == null) {
            Log.e(TAG, "failed to get PaymentLink app destination");
            this.showError("Failed to get PaymentLink app destination. Was it properly set ?");
            return;
        }

        destination += "/?status=" + status;
        Uri uri = Uri.parse(destination);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);

        //Verify if paymentlinkclientapp has this screen path
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isIntentSafe = activities.size() > 0;

        //Start HomeActivity of app paymentlinkclientapp if it exists
        if (isIntentSafe) {
            Log.d(TAG, "calling PaymentLink Client App with " + destination);
            startActivity(mapIntent);
        } else {
            this.showError("Payment link client failed. Is app installed ?");
        }
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
