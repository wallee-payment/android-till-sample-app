package com.wallee.android.till.sample.till;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.wallee.android.till.sdk.Utils;

import com.wallee.android.till.sdk.data.GetPinpadInformationResponse;


public class PinpadInformationResponseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinpad_information_response_activity);
        Log.i("PINPAd---->","Oncreate");
        GetPinpadInformationResponse result = Utils.getPinpadInformationResponse(getIntent().getExtras());


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
    private String resultToString(GetPinpadInformationResponse result) {
        Log.i("PINPAd---->",result.getState()+"");
        return "state - " + result.getState() + "\n" +
                "resultCode code - " + result.getResultCode().getCode() + "\n" +
                "resultCode description - " + result.getResultCode().getDescription() + "\n"+
                "Terminal Id - "+ result.getTerminalId() + "\n" +
                "Serial Number - "+ result.getSerialNumber() + "\n" +
                "Merchant ID - "+ result.getMerchantId() + "\n"+
                "Merchant Name - "+ result.getMerchantName() +"\n"+
                "Space ID - "+ result.getSpaceID() +"\n"
                ;
    }

}