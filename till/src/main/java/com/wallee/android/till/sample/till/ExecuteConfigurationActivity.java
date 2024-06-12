package com.wallee.android.till.sample.till;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.wallee.android.till.sdk.ApiClient;

public class ExecuteConfigurationActivity extends AppCompatActivity {

    private static final String TAG = "ExecuteConfigurationAct";
    private ApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.execute_configuration_activity);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        findViewById(R.id.executeConfig).setOnClickListener(view -> {
            try {
                progressBar.setVisibility(View.VISIBLE);
                client.executeConfiguration();
            } catch (Exception e) {
                Log.e(TAG, "onCreate: error " + e.getMessage());
            }
        });

        findViewById(R.id.returnButton).setOnClickListener(view -> finish());

        client = new ApiClient(new MockResponseHandler(this));
        client.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.unbind(this);
    }
}