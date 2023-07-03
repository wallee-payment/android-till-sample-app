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
            MainActivity app = (MainActivity) getApplicationContext();
            app.finish();
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

            String type = appLinkData.getQueryParameter("type");
            String amount = appLinkData.getQueryParameter("amount");
            String currency = appLinkData.getQueryParameter("currency");
            String callbackScheme = appLinkData.getQueryParameter("callbackscheme");
            String callbackHost = appLinkData.getQueryParameter("callbackhost");

            Log.d(TAG, "processing PaymentLink transaction data: ");
            Log.d(TAG, "type = " + type);
            Log.d(TAG, "amount = " + amount);
            Log.d(TAG, "currency = " + currency);
            Log.d(TAG, "callbackScheme = " + callbackScheme);
            Log.d(TAG, "callbackHost = " + callbackHost);

            if (type == null || amount == null || currency == null || callbackScheme == null) {
                Log.e(TAG, "params missing for the payment link");
                this.showError("Params missing for the payment link. Check log and the link");
                return;
            }
            PaymentLinkData app = (PaymentLinkData) getApplicationContext();
            app.setPaymentLinkClientDestination((callbackHost!=null)?callbackScheme+"://"+callbackHost : callbackScheme);
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
            client.authorizeTransaction(transaction);
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
        super.onDestroy();
        client.unbind(this);
    }


}
