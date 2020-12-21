package com.wallee.android.till.sample.till;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wallee.android.till.sdk.ApiClient;
import com.wallee.android.till.sdk.ResponseHandler;

@SuppressLint("HandlerLeak")
public class CheckApiServiceCompatibilityActivity extends AppCompatActivity {
    private ApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_api_service_compatibility_activity);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        findViewById(R.id.checkButton).setOnClickListener(view -> {
            try {
                progressBar.setVisibility(View.VISIBLE);
                client.checkApiServiceCompatibility();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.returnButton).setOnClickListener(view -> finish());

        client = new ApiClient(new ResponseHandler() {
            @Override
            public void checkApiServiceCompatibilityReply(Boolean isCompatible, String apiServiceVersion) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(
                        CheckApiServiceCompatibilityActivity.this,
                        "isCompatible: " + isCompatible + "\napiServiceVersion: " + apiServiceVersion,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
        client.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.unbind(this);
    }
}
