package com.wallee.android.till.sample.paymentlinkconverter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wallee.android.till.sdk.ApiClient;
import com.wallee.android.till.sdk.data.LineItem;
import com.wallee.android.till.sdk.data.Transaction;
import com.wallee.android.till.sdk.data.TransactionProcessingBehavior;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.logging.Logger;

public class ProcessPaymentLinkActivity extends AppCompatActivity {
    private ApiClient client;
    private final String TAG = "ProcessPaymentLink";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_activity);
        try {
            handleIntent();
        } catch (RemoteException e) {
            Log.e(TAG, "handleIntent failed");
            e.printStackTrace();
        }

        findViewById(R.id.exitButton).setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        try {
            handleIntent();
        } catch (RemoteException e) {
            Log.e(TAG, "handleIntent failed");
            e.printStackTrace();
        }
    }

    private void handleIntent() throws RemoteException {
        Log.d(TAG, "handleIntent called");
        Intent appLinkIntent = getIntent();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null) {
            Log.d(TAG, "Link received: " + appLinkData);

            String type = appLinkData.getQueryParameter("type");
            String amount = appLinkData.getQueryParameter("amount");
            String currency = appLinkData.getQueryParameter("currency");
            String successCallbackScheme = appLinkData.getQueryParameter("callbackSuccess");
            String failureCallbackScheme = appLinkData.getQueryParameter("callbackFail");
            String receiptId = appLinkData.getQueryParameter("receiptId");

            Log.d(TAG, "processing PaymentLink transaction data: ");
            Log.d(TAG, "type = " + type);
            Log.d(TAG, "amount = " + amount);
            Log.d(TAG, "currency = " + currency);
            Log.d(TAG, "callbackSuccess = " + successCallbackScheme);
            Log.d(TAG, "callbackFail = " + failureCallbackScheme);
            Log.d(TAG, "receiptId = " + receiptId);

            if (type == null || amount == null || currency == null || successCallbackScheme == null || failureCallbackScheme == null) {
                Log.e(TAG, "params missing for the payment link");
                this.showError("Params missing for the payment link. Check log and the link");
                return;
            }
            PaymentLinkData app = (PaymentLinkData) getApplicationContext();
            app.setPaymentLinkClientSuccess(successCallbackScheme);
            app.setPaymentLinkClientFailure(failureCallbackScheme);
            app.setPaymentLinkClientReceiptId(receiptId);
            startTransaction(type, amount, currency);
        } else {
            Log.e(TAG, "appLinkData absent");
            this.showError("Payment link data missing or corrupted");
        }
    }

    private void startTransaction(String type, String amount, String currency) {
        List<LineItem> lineItems = new LineItem.ListBuilder("foo", new BigDecimal(amount))
                .getCurrent()
                .setName("bar")
                .getListBuilder()
                .build();

        Transaction.Builder transactionBuilder = new Transaction.Builder(lineItems)
                .setCurrency(Currency.getInstance(currency))
                .setInvoiceReference("1")
                .setMerchantReference("MREF-123")
                .setTransactionProcessingBehavior(TransactionProcessingBehavior.COMPLETE_IMMEDIATELY)
                .setGeneratePanToken(false);

        Transaction transaction = transactionBuilder.build();
        client = new ApiClient(new MockResponseHandler(this));
        client.bind(this);
        Log.d(TAG, "ApiClient is bound for transaction");

        try {
            if (type.equals("1")) {
                client.authorizeTransaction(transaction);
            } else {
                this.showError("Wrong Transaction type set. Type: " + type);
            }
        } catch (Exception e){
            e.printStackTrace();
            this.showError("Failed to connect to VSD. Is app installed ?");
        }
    }

    private void showError(String message) {
        TextView textViewResult = findViewById(R.id.textViewError);
        textViewResult.setText(message);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        client.unbind(this);
    }


}
