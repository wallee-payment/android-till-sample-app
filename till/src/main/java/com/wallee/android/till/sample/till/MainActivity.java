package com.wallee.android.till.sample.till;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.wallee.android.till.sdk.TillLog;
import com.wallee.android.till.sdk.Utils;


public class MainActivity extends AppCompatActivity {
    private boolean isSystemBarEnabled = false;
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

        findViewById(R.id.reprintReceipt).setOnClickListener(v -> {
            startActivity(new Intent(this, ReprintReceiptActivity.class));
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

        findViewById(R.id.getTerminalConfigData).setOnClickListener(v -> startActivity(
                new Intent(this, TerminalConfigurationDataActivity.class))
        );

        findViewById(R.id.config).setOnClickListener(v -> {
            startActivity(new Intent(this, ExecuteConfigurationActivity.class));
        });

        findViewById(R.id.init).setOnClickListener(v -> {
            startActivity(new Intent(this, ExecuteInitialisationActivity.class));
        });

        findViewById(R.id.getCustomConfig).setOnClickListener(v -> {
            startActivity(new Intent(this, GetCustomConfigurationActivity.class));
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestOverlayPermission();
    }

    // Android 10 needs overlay permission to get transaction response
    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!Settings.canDrawOverlays(this)) {
                this.enableSystemBar();
                this.isSystemBarEnabled = true;
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
            } else {
                if (this.isSystemBarEnabled){
                    this.disableSystemBar();
                    this.isSystemBarEnabled = false;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_enable_system_bar:
                this.enableSystemBar();
                return true;
            case R.id.action_disable_system_bar:
                this.disableSystemBar();
                return true;
            case R.id.action_wallee_settings:
                this.walleeSettingsMenu();
                return true;
            case R.id.action_service_transaction:
                this.startTransactionFromService();
                return true;
            case R.id.action_deeplink_transaction:
                this.startDeepLinkTransaction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startTransactionFromService() {
        Intent intent = new Intent(this, ServiceTransaction.class);
        intent.putExtra("amount", "15.00");
        intent.putExtra("currency", "EUR");
        startService(intent);
    }

    private void startDeepLinkTransaction() {
        startActivity(new Intent(this, DeepLinkRequestActivity.class));

    }

    private void enableSystemBar() {
        Utils.enableSystemBar(this);
    }

    private void disableSystemBar() {
        Utils.disableSystemBar(this);
    }

    private void walleeSettingsMenu() {
        Utils.openSettings(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unbind
        TillLog.getInstance().unbind(this);
    }
}