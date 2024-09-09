package com.wallee.android.till.sample.till;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wallee.android.till.sample.till.model.ErrorCode;
import com.wallee.android.till.sdk.Utils;
import com.wallee.android.till.sdk.data.AcquirerData;
import com.wallee.android.till.sdk.data.Brand;
import com.wallee.android.till.sdk.data.CommAddrGateWay;
import com.wallee.android.till.sdk.data.CurrencyItem;
import com.wallee.android.till.sdk.data.Ep2TerminalConfigData;
import com.wallee.android.till.sdk.data.GetConfigDataResponse;
import com.wallee.android.till.sdk.data.TerminalApplicationConfigData;


public class TerminalConfigurationDataResponseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal_configuration_data_response_activity);
        Log.i("Config Data---->","Oncreate");
        GetConfigDataResponse result = Utils.getConfigDataResponse(getIntent().getExtras());


        if (result != null) {
            if (result.getResultCode().getCode().equals(ErrorCode.ERR_CONNECTION_FAILED.getCode())) {
                com.wallee.android.till.sample.till.Utils.showToast(this,getResources().getString(R.string.app_relaunch));
                Utils.handleFailedToConnectVpj(this);
            }
            TextView textViewResult = findViewById(R.id.textViewResult);
            textViewResult.setText(resultToString(result));
        }

        findViewById(R.id.okButton).setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
//    private String resultToString(GetConfigDataResponse result) {
//        Log.i("CONFIG DATA---->",result.getState()+"");
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        return gson.toJson(result);
//    }

    private String resultToString(GetConfigDataResponse result) {
        StringBuilder builder = new StringBuilder();

        builder.append("state - ").append(result.getState()).append("\n")
                .append("resultCode code - ").append(result.getResultCode().getCode()).append("\n")
                .append("resultCode description - ").append(result.getResultCode().getDescription()).append("\n")
                .append("ep2Version - ").append(result.getEp2Version()).append("\n")
                .append("Terminal Config Data - ").append(ep2TerminalConfigDataToString(result.getEp2Tcd())).append("\n")
                .append("Brand - ").append(brandToString(result.getBrand())).append("\n");

        // AcquirerData list
        builder.append("Acquirer Data List:").append("\n");
        for (AcquirerData acquirerData : result.getAcquirerDataList()) {
            builder.append("\tAcquirer ID: ").append(acquirerData.getAcquirerId()).append("\n");
            for (TerminalApplicationConfigData configData : acquirerData.getTerminalApplicationConfigDataList()) {
                builder.append("\t\tTerminal Application Config Data: ").append("\n")
                        .append("\t\tAID: ").append(configData.getAid()).append("\n")
                        .append("\t\tBrand: ").append(configData.getBrand()).append("\n");
            }
        }

        // CurrencyItem list
        builder.append("Currency List:").append("\n");
        for (CurrencyItem currencyItem : result.getCurrencyList()) {
            builder.append("\tCurrency Type: ").append(currencyItem.getCurrencyType()).append("\n")
                    .append("\tCurrency: ").append(currencyItem.getCurrency()).append("\n");
        }

        return builder.toString();
    }

    private String ep2TerminalConfigDataToString(Ep2TerminalConfigData ep2Tcd) {
        StringBuilder builder = new StringBuilder();
        if (ep2Tcd != null) {
            builder.append("Comm Addr Gateway - ").append(commAddrGateWayToString(ep2Tcd.getCommAddrGateWay())).append("\n")
                    .append("Terminal ID - ").append(ep2Tcd.getTrmId()).append("\n")
                    .append("DCC Provider - ").append(ep2Tcd.getDccProvider()).append("\n");
        }
        return builder.toString();
    }

    private String commAddrGateWayToString(CommAddrGateWay commAddrGateWay) {
        StringBuilder builder = new StringBuilder();
        if (commAddrGateWay != null) {
            builder.append("Internet Port No - ").append(commAddrGateWay.getInternetPortNo()).append("\n")
                    .append("Internet Addr - ").append(commAddrGateWay.getInternetAddr()).append("\n");
        }
        return builder.toString();
    }

    private String brandToString(Brand brand) {
        StringBuilder builder = new StringBuilder();
        if (brand != null) {
            builder.append("Brand Name - ").append(brand.getBrandName()).append("\n")
                    .append("Last Init Date - ").append(brand.getLastInitDate()).append("\n")
                    .append("Payment Protocol - ").append(brand.getPaymentProtocol()).append("\n");
        }
        return builder.toString();
    }

}