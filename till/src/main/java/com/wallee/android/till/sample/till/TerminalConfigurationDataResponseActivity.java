package com.wallee.android.till.sample.till;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.wallee.android.till.sample.till.model.ErrorCode;
import com.wallee.android.till.sdk.Utils;
import com.wallee.android.till.sdk.data.AcquirerData;
import com.wallee.android.till.sdk.data.CurrencyItem;
import com.wallee.android.till.sdk.data.Ep2CtlessTrmCapPerKernel;
import com.wallee.android.till.sdk.data.Ep2TerminalConfigData;
import com.wallee.android.till.sdk.data.GetConfigDataResponse;
import com.wallee.android.till.sdk.data.ResultCode;
import com.wallee.android.till.sdk.data.TerminalApplicationConfigData;


public class TerminalConfigurationDataResponseActivity extends AppCompatActivity {

    private static final String TAG = "ConfigDataResponse";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal_configuration_data_response_activity);
        GetConfigDataResponse result = Utils.getConfigDataResponse(getIntent().getExtras());


        if (result != null) {
            if (result.getResultCode().getCode().equals(ErrorCode.ERR_CONNECTION_FAILED.getCode())) {
                com.wallee.android.till.sample.till.Utils.INSTANCE.showToast(this,getResources().getString(R.string.app_relaunch));
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

    private String resultToString(GetConfigDataResponse response) {
        StringBuilder sb = new StringBuilder();

        sb.append("State: ").append(response.getState().name()).append("\n");

        ResultCode resultCode = response.getResultCode();
        sb.append("Result Code: \n")
                .append("Code: ").append(resultCode.getCode() != null ? resultCode.getCode() : "null").append("\n")
                .append("Description: ").append(resultCode.getDescription() != null ? resultCode.getDescription() : "null").append("\n");


        if (response.getAcquirerDataList() != null) {
            for (AcquirerData acquirerData : response.getAcquirerDataList()) {
                sb.append("Acquirer Data: \n").append("\n");
                if (acquirerData != null) {
                    sb.append("  Acquirer ID: ").append(acquirerData.getAcquirerId() != null ? acquirerData.getAcquirerId() : "null").append("\n").append("\n");
                    sb.append("  Terminal Application Config Data: \n");
                    if (acquirerData.getTerminalApplicationConfigDataList() != null) {
                        for (TerminalApplicationConfigData terminalApplicationConfigData : acquirerData.getTerminalApplicationConfigDataList()) {
                            if (terminalApplicationConfigData != null) {
                                sb.append("    Aid: ").append(terminalApplicationConfigData.getAid() != null ? terminalApplicationConfigData.getAid() : "null").append("\n")
                                        .append("    Brand: ").append(terminalApplicationConfigData.getBrand() != null ? terminalApplicationConfigData.getBrand() : "null").append("\n")
                                        .append("    ReaderTechnology: ").append(terminalApplicationConfigData.getReaderTechnology() != null ? terminalApplicationConfigData.getReaderTechnology() : "null").append("\n").append("\n");
                            }
                        }
                    }
                    sb.append("\n");
                }
            }
        } else {
            sb.append("  Acquirer data list is null\n");
        }

        Ep2TerminalConfigData ep2TerminalConfigData = response.getEp2Tcd();
        if (ep2TerminalConfigData != null) {
             sb.append("Ep2 Terminal Config Data: \n")
            .append("\n")
            .append("  AddTrmCap: ").append(ep2TerminalConfigData.getAddTrmCap() != null ? ep2TerminalConfigData.getAddTrmCap() : "null").append("\n")
            .append("  AutoDeclRef: ").append(ep2TerminalConfigData.getAutoDeclRef() != null ? ep2TerminalConfigData.getAutoDeclRef() : "null").append("\n")
            .append("  CardRdType: ").append(ep2TerminalConfigData.getCardRdType() != null ? ep2TerminalConfigData.getCardRdType() : "null").append("\n")
            .append("  CommAddressPMS: InternetAddress: ").append(ep2TerminalConfigData.getCommAddressPMS() != null && ep2TerminalConfigData.getCommAddressPMS().getInternetAddress() != null ? ep2TerminalConfigData.getCommAddressPMS().getInternetAddress() : "null").append("\n")
            .append("  CommAddressPMS: InternetPortNo: ").append(ep2TerminalConfigData.getCommAddressPMS() != null ? ep2TerminalConfigData.getCommAddressPMS().getInternetPortNo() : "null").append("\n")
            .append("  CommAddressSCConf: InternetAddress: ").append(ep2TerminalConfigData.getCommAddressSCConf() != null && ep2TerminalConfigData.getCommAddressSCConf().getInternetAddress() != null ? ep2TerminalConfigData.getCommAddressSCConf().getInternetAddress() : "null").append("\n")
            .append("  CommAddressSCConf: InternetPortNo: ").append(ep2TerminalConfigData.getCommAddressSCConf() != null ? ep2TerminalConfigData.getCommAddressSCConf().getInternetPortNo() : "null").append("\n")
            .append("  CtlessInd: ").append(ep2TerminalConfigData.getCtlessInd() != null ? ep2TerminalConfigData.getCtlessInd() : "null").append("\n");

                if (ep2TerminalConfigData.getSetCtlessTrmCap() != null) {
                    sb.append("\n")
                   .append("Ep2CtlessTrmCapPerKernel: \n").append("\n");

                    for (Ep2CtlessTrmCapPerKernel ep2CtlessTrmCapPerKernel : ep2TerminalConfigData.getSetCtlessTrmCap()) {
                              sb.append("  CtlessAddTrmCap: ").append(ep2CtlessTrmCapPerKernel.getCtlessAddTrmCap() != null ? ep2CtlessTrmCapPerKernel.getCtlessAddTrmCap() : "null").append("\n")
                                .append("  CtlessKernelId: ").append(ep2CtlessTrmCapPerKernel.getCtlessKernelId() != null ? ep2CtlessTrmCapPerKernel.getCtlessKernelId() : "null").append("\n")
                                .append("  DataExchangeFlag: ").append(ep2CtlessTrmCapPerKernel.getDataExchangeFlag() != null ? ep2CtlessTrmCapPerKernel.getDataExchangeFlag() : "null").append("\n")
                                .append("  KernelVersion: ").append(ep2CtlessTrmCapPerKernel.getKernelVersion() != null ? ep2CtlessTrmCapPerKernel.getKernelVersion() : "null").append("\n")
                                .append("  TrxMode: ").append(ep2CtlessTrmCapPerKernel.getTrxMode() != null ? ep2CtlessTrmCapPerKernel.getTrxMode() : "null").append("\n").append("\n");
                    }
                } else {
                    sb.append(" Ep2CtlessTrmCapPerKernel list is null\n");
                }

             sb.append("  DataSubmMaxRetry: ").append(ep2TerminalConfigData.getDataSubmMaxRetry() != null ? ep2TerminalConfigData.getDataSubmMaxRetry() : "null").append("\n")
            .append("  DSub: ").append(ep2TerminalConfigData.getdSub() != null ? ep2TerminalConfigData.getdSub() : "null").append("\n")
            .append("  DataSubmRetryDel: ").append(ep2TerminalConfigData.getDataSubmRetryDel() != null ? ep2TerminalConfigData.getDataSubmRetryDel() : "null").append("\n")
            .append("  DataSubmTime: ").append(ep2TerminalConfigData.getDataSubmTime() != null ? ep2TerminalConfigData.getDataSubmTime() : "null").append("\n")
            .append("  DataSubmTrigg: ").append(ep2TerminalConfigData.getDataSubmTrigg() != null ? ep2TerminalConfigData.getDataSubmTrigg() : "null").append("\n")
            .append("  DataTransMaxRetry: ").append(ep2TerminalConfigData.getDataTransMaxRetry() != null ? ep2TerminalConfigData.getDataTransMaxRetry() : "null").append("\n")
            .append("  DataTransRetryDel: ").append(ep2TerminalConfigData.getDataTransRetryDel() != null ? ep2TerminalConfigData.getDataTransRetryDel() : "null").append("\n")
            .append("  DataTransTime: ").append(ep2TerminalConfigData.getDataTransTime() != null ? ep2TerminalConfigData.getDataTransTime() : "null").append("\n")
            .append("  DataTransTrigg: ").append(ep2TerminalConfigData.getDataTransTrigg() != null ? ep2TerminalConfigData.getDataTransTrigg() : "null").append("\n")
            .append("  DccProvider: ").append(ep2TerminalConfigData.getDccProvider() != null ? ep2TerminalConfigData.getDccProvider() : "null").append("\n")
            .append("  DefTrxCurrC: ").append(ep2TerminalConfigData.getDefTrxCurrC() != null ? ep2TerminalConfigData.getDefTrxCurrC() : "null").append("\n")
            .append("  MaxStor: ").append(ep2TerminalConfigData.getMaxStor() != null ? ep2TerminalConfigData.getMaxStor() : "null").append("\n")
            .append("  MctId: ").append(ep2TerminalConfigData.getMctId() != null ? ep2TerminalConfigData.getMctId() : "null").append("\n")
            .append("  PhonePrfx: ").append(ep2TerminalConfigData.getPhonePrfx() != null ? ep2TerminalConfigData.getPhonePrfx() : "null").append("\n")
            .append("  PmsId: ").append(ep2TerminalConfigData.getPmsId() != null ? ep2TerminalConfigData.getPmsId() : "null").append("\n")
            .append("  PmsPubKey: ").append(ep2TerminalConfigData.getPmsPubKey() != null ? ep2TerminalConfigData.getPmsPubKey() : "null").append("\n")
            .append("  RevRetryDelay: ").append(ep2TerminalConfigData.getRevRetryDelay() != null ? ep2TerminalConfigData.getRevRetryDelay() : "null").append("\n")
            .append("  ScConfTime: ").append(ep2TerminalConfigData.getScConfTime() != null ? ep2TerminalConfigData.getScConfTime() : "null").append("\n")
            .append("  ScId: ").append(ep2TerminalConfigData.getScId() != null ? ep2TerminalConfigData.getScId() : "null").append("\n")
            .append("  ScIntConf: ").append(ep2TerminalConfigData.getScIntConf() != null ? ep2TerminalConfigData.getScIntConf() : "null").append("\n")
            .append("  ScPubKey: ").append(ep2TerminalConfigData.getScPubKey() != null ? ep2TerminalConfigData.getScPubKey() : "null").append("\n")
            .append("  SubmInt: ").append(ep2TerminalConfigData.getSubmInt() != null ? ep2TerminalConfigData.getSubmInt() : "null").append("\n")
            .append("  SuppPhone: ").append(ep2TerminalConfigData.getSuppPhone() != null ? ep2TerminalConfigData.getSuppPhone() : "null").append("\n")
            .append("  TrmCap: ").append(ep2TerminalConfigData.getTrmCap() != null ? ep2TerminalConfigData.getTrmCap() : "null").append("\n")
            .append("  TrmCntryC: ").append(ep2TerminalConfigData.getTrmCntryC() != null ? ep2TerminalConfigData.getTrmCntryC() : "null").append("\n")
            .append("  TrmID: ").append(ep2TerminalConfigData.getTrmID() != null ? ep2TerminalConfigData.getTrmID() : "null").append("\n")
            .append("  TrmLng: ").append(ep2TerminalConfigData.getTrmLng() != null ? ep2TerminalConfigData.getTrmLng() : "null").append("\n")
            .append("  TrmRMCap: ").append(ep2TerminalConfigData.getTrmRMCap() != null ? ep2TerminalConfigData.getTrmRMCap() : "null").append("\n")
            .append("  TrmTrxFctCap: ").append(ep2TerminalConfigData.getTrmTrxFctCap() != null ? ep2TerminalConfigData.getTrmTrxFctCap() : "null").append("\n")
            .append("  TrmType: ").append(ep2TerminalConfigData.getTrmType() != null ? ep2TerminalConfigData.getTrmType() : "null").append("\n")
            .append("  ToDataSubSrv: ").append(ep2TerminalConfigData.getToDataSubSrv() != null ? ep2TerminalConfigData.getToDataSubSrv() : "null").append("\n")
            .append("  ToInitSrv: ").append(ep2TerminalConfigData.getToInitSrv() != null ? ep2TerminalConfigData.getToInitSrv() : "null").append("\n")
            .append("  ToAuthSrv: ").append(ep2TerminalConfigData.getToAuthSrv() != null ? ep2TerminalConfigData.getToAuthSrv() : "null").append("\n")
            .append("  ToCardIn: ").append(ep2TerminalConfigData.getToCardIn() != null ? ep2TerminalConfigData.getToCardIn() : "null").append("\n")
            .append("  ToCardRem: ").append(ep2TerminalConfigData.getToCardRem() != null ? ep2TerminalConfigData.getToCardRem() : "null").append("\n")
            .append("  ToConfTrx: ").append(ep2TerminalConfigData.getToConfTrx() != null ? ep2TerminalConfigData.getToConfTrx() : "null").append("\n")
            .append("  ToDatEntry: ").append(ep2TerminalConfigData.getToDatEntry() != null ? ep2TerminalConfigData.getToDatEntry() : "null").append("\n")
            .append("  ToFB: ").append(ep2TerminalConfigData.getToFB() != null ? ep2TerminalConfigData.getToFB() : "null").append("\n")
            .append("  ToICC: ").append(ep2TerminalConfigData.getToICC() != null ? ep2TerminalConfigData.getToICC() : "null").append("\n")
            .append("  ToPMS: ").append(ep2TerminalConfigData.getToPMS() != null ? ep2TerminalConfigData.getToPMS() : "null").append("\n")
            .append("  ToPosTrxRq: ").append(ep2TerminalConfigData.getToPosTrxRq() != null ? ep2TerminalConfigData.getToPosTrxRq() : "null").append("\n")
            .append("  ToRmdr: ").append(ep2TerminalConfigData.getToRmdr() != null ? ep2TerminalConfigData.getToRmdr() : "null").append("\n")
            .append("  ToSCReq: ").append(ep2TerminalConfigData.getToSCReq() != null ? ep2TerminalConfigData.getToSCReq() : "null").append("\n")
            .append("  ToTrxTrans: ").append(ep2TerminalConfigData.getToTrxTrans() != null ? ep2TerminalConfigData.getToTrxTrans() : "null").append("\n")
            .append("  TrxSubmLim: ").append(ep2TerminalConfigData.getTrxSubmLim() != null ? ep2TerminalConfigData.getTrxSubmLim() : "null").append("\n")
            .append("  TrxTransLim: ").append(ep2TerminalConfigData.getTrxTransLim() != null ? ep2TerminalConfigData.getTrxTransLim() : "null").append("\n")
            .append("  TransInt: ").append(ep2TerminalConfigData.getTransInt() != null ? ep2TerminalConfigData.getTransInt() : "null").append("\n")
            .append("  UsrRetryCnt: ").append(ep2TerminalConfigData.getUsrRetryCnt() != null ? ep2TerminalConfigData.getUsrRetryCnt() : "null").append("\n")
            .append("  VersSW: ").append(ep2TerminalConfigData.getVersSW() != null ? ep2TerminalConfigData.getVersSW() : "null").append("\n").append("\n");
        } else {
            sb.append("ep2TerminalConfigData : null\n");
        }


        if (response.getCurrencyList() != null && !response.getCurrencyList().isEmpty()) {
            sb.append("Currency List: \n").append("\n");
            for (CurrencyItem currencyItem : response.getCurrencyList()) {
                sb.append("Currency Type: ").append(currencyItem.getCurrencyType() != null ? currencyItem.getCurrencyType() : "null").append("\n");
                sb.append("Currency: ").append(currencyItem.getCurrency() != null ? currencyItem.getCurrency() : "null").append("\n").append("\n");
            }
        } else {
            sb.append("Currency list is null/empty \n");
        }

        return sb.toString();
    }


}