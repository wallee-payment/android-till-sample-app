package com.wallee.android.till.sample.till;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wallee.android.till.sdk.Utils;
import com.wallee.android.till.sdk.data.FinalBalanceResult;

public class FinalBalanceResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_balance_result_activity);

        FinalBalanceResult result = Utils.getFinalBalanceResult(getIntent().getExtras());

        if (result != null) {
            TextView textViewResult = findViewById(R.id.textViewResult);
            textViewResult.setText(resultToString(result));
        }

        findViewById(R.id.okButton).setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private String resultToString(FinalBalanceResult result) {
        return "state - " + result.getState() + "\n" +
                "resultCode code - " + result.getResultCode().getCode() + "\n" +
                "resultCode description - " + result.getResultCode().getDescription() + "\n" +
                "receipts - " + result.getReceipts();
    }
}
