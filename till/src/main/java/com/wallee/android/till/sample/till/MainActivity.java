package com.wallee.android.till.sample.till;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import com.wallee.android.till.sdk.TillLog;
import com.wallee.android.till.sdk.Utils;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.checkApiServiceCompatibility).setOnClickListener(v -> {
            startActivity(new Intent(this, CheckApiServiceCompatibilityActivity.class));
        });

        findViewById(R.id.authorizeTransaction).setOnClickListener(v -> {
            startActivity(new Intent(this, AuthorizeTransactionActivity.class));
        });

        findViewById(R.id.completeTransaction).setOnClickListener(v -> {
            startActivity(new Intent(this, CompleteTransactionActivity.class));
        });

        findViewById(R.id.voidTransaction).setOnClickListener(v -> {
            startActivity(new Intent(this, VoidTransactionActivity.class));
        });

        findViewById(R.id.cancelLastTransactionOperation).setOnClickListener(v -> {
            startActivity(new Intent(this, CancelLastTransactionOperationActivity.class));
        });

        findViewById(R.id.executeTransmission).setOnClickListener(v -> {
            startActivity(new Intent(this, ExecuteTransmissionActivity.class));
        });

        findViewById(R.id.executeSubmission).setOnClickListener(v -> {
            startActivity(new Intent(this, ExecuteSubmissionActivity.class));
        });

        findViewById(R.id.executeFinalBalance).setOnClickListener(v -> {
            startActivity(new Intent(this, ExecuteFinalBalanceActivity.class));
        });

        findViewById(R.id.generatePanTokenReq).setOnClickListener(v -> {
            startActivity(new Intent(this, GeneratePanTokenActivity.class));
        });

        findViewById(R.id.getConfigInfo).setOnClickListener(v -> {
            startActivity(new Intent(this, PinpadInformationActivity.class));

        });

        findViewById(R.id.settings).setOnClickListener(v -> {
            Utils.openSettings(this);
        });

        findViewById(R.id.exit).setOnClickListener(v -> {
            finish();
        });

        // init & bind
        TillLog.getInstance().bind(this);

        TillLog.debug("VSD Test Debug");
        TillLog.error("VSD Test Error");
        TillLog.warning("VSD Test Warning");
        TillLog.lAssert("VSD Send Assert");

        requestOverlayPermission();

    }


    // Android 10 needs overlay permission to get transaction response
    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unbind
        TillLog.getInstance().unbind(this);
    }
}