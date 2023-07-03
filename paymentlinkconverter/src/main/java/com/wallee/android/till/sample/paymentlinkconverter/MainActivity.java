package com.wallee.android.till.sample.paymentlinkconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.wallee.android.till.sdk.ApiClient;
import com.wallee.android.till.sdk.data.LineItem;
import com.wallee.android.till.sdk.data.Transaction;
import com.wallee.android.till.sdk.data.TransactionProcessingBehavior;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.launchPaymentLinkClientApp(this);

        findViewById(R.id.exitButton).setOnClickListener(view -> {
            finish();
        });
    }

    public void launchPaymentLinkClientApp(Context context) {
        try {
            Log.d(TAG, "Launching PaymentLink CLient App...");
            Intent intent = new Intent("com.wallee.android.PAYMENT_LINK_APPLICATION");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Log.d(TAG, "PaymentLink CLient App is not installed...");
                this.showError("Failed to start PaymentLink Client Application. Is it installed ?");
            }
        } catch (Exception e) {
            Log.e(TAG, "General error when launching PaymentLink CLient App", e);
            this.showError("General error when launching PaymentLink CLient");
        }
    }

    private void showError(String message) {
        TextView textViewResult = findViewById(R.id.textViewError);
        textViewResult.setText(message);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        finish();
    }


}