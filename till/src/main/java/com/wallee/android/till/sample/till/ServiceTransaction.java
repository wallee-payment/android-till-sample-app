package com.wallee.android.till.sample.till;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.wallee.android.till.sdk.ApiClient;
import com.wallee.android.till.sdk.data.LineItem;
import com.wallee.android.till.sdk.data.Transaction;
import com.wallee.android.till.sdk.data.TransactionProcessingBehavior;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

/**
 * ServiceTransaction example of how to start a transaction from a service
 *
 *
 * Start Transactions from other apps
 * Intent intent = new Intent();
 * intent.setComponent(new ComponentName("com.wallee.android.till.sample.till", "com.wallee.android.till.sample.till.ServiceTransaction"));
 * intent.putExtra("amount", "50.00");
 * intent.putExtra("currency", "CHF");
 * context.startService(intent);
 *
 */

public class ServiceTransaction extends Service {

    private static final String TAG = "ServiceTransaction";
    private ApiClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        this.client = new ApiClient(new MockResponseHandler(this));
        this.client.bind(this);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        if (intent != null) {
            authorizeTransaction(intent);
        }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        this.client.unbind(this);
    }

    private void authorizeTransaction(Intent intent) {
        Log.d(TAG, "authorizeTransaction: ");
        if (intent.hasExtra("amount") && intent.hasExtra("currency")) {
            String amountString = intent.getStringExtra("amount");
            String currency = intent.getStringExtra("currency");
            if (amountString == null || amountString.isEmpty()) {
                Toast.makeText(this, "Amount field is empty", Toast.LENGTH_LONG).show();
            } else {
                List<LineItem> lineItems = new LineItem.ListBuilder("foo", new BigDecimal(amountString))
                        .getCurrent()
                        .setName("bar")
                        .getListBuilder()
                        .build();

                Transaction transaction;
                Transaction.Builder transactionBuilder = new Transaction.Builder(lineItems)
                        .setCurrency(Currency.getInstance(currency))
                        .setInvoiceReference("1")
                        .setMerchantReference("MREF-123")
                        .setTransactionProcessingBehavior(
                                TransactionProcessingBehavior.COMPLETE_IMMEDIATELY);

                transaction = transactionBuilder.build();
                try {
                    client.authorizeTransaction(transaction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
