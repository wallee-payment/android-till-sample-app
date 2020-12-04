package com.wallee.android.till.sample.interception;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.wallee.android.till.sdk.ApiClient;
import com.wallee.android.till.sdk.ResponseHandler;
import com.wallee.android.till.sdk.Utils;
import com.wallee.android.till.sdk.data.LineItem;
import com.wallee.android.till.sdk.data.Transaction;

import java.math.BigDecimal;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class BeforeActivity extends AppCompatActivity {
    private ApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before);

        findViewById(R.id.modifybutton).setOnClickListener(v -> {
            Transaction t = Utils.getTransaction(getIntent().getExtras());

            LineItem.ListBuilder lineItemsBuilder = new LineItem.ListBuilder(t.getLineItems());
            List<LineItem.Builder> lineItems = lineItemsBuilder.getLineItems();
            lineItems.get(0).addTax("ThirdPartyTax", BigDecimal.TEN);

            Transaction processedTransaction = new Transaction.Builder(t)
                  .setMerchantReference(t.getMerchantReference() + "/ThirdPartyTax")
                  .setLineItems(lineItemsBuilder.build())
                  .build();

            setResult(Activity.RESULT_OK, new Intent().putExtras(Utils.toBundle(processedTransaction)));
            finish();
        });

        findViewById(R.id.cancelButton).setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        findViewById(R.id.versionButton).setOnClickListener(v -> {
            try {
                client.checkApiServiceCompatibility();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //noinspection HandlerLeak
        client = new ApiClient(new ResponseHandler() {
            @Override
            public void checkApiServiceCompatibilityReply(Boolean isCompatible, String apiServiceVersion) {
                Toast.makeText(
                      BeforeActivity.this,
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
