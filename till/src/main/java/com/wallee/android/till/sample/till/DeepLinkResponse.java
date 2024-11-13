package com.wallee.android.till.sample.till;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DeepLinkResponse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link_response);
        TextView textViewResult = findViewById(R.id.textViewResult);

        if(getIntent().getData() != null) {
            textViewResult.setText(getIntent().getDataString());
            findViewById(R.id.okButton).setOnClickListener(view -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}