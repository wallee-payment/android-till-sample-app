package com.wallee.android.till.sample.till;

import android.content.Context;
import android.content.Intent;

import com.wallee.android.till.sdk.ResponseHandler;
import com.wallee.android.till.sdk.Utils;
import com.wallee.android.till.sdk.data.CancelationResult;
import com.wallee.android.till.sdk.data.ConfigurationResult;
import com.wallee.android.till.sdk.data.FinalBalanceResult;
import com.wallee.android.till.sdk.data.GeneratePanTokenResponse;
import com.wallee.android.till.sdk.data.GetConfigDataResponse;
import com.wallee.android.till.sdk.data.GetPinpadInformationResponse;
import com.wallee.android.till.sdk.data.InitialisationResult;
import com.wallee.android.till.sdk.data.SubmissionResult;
import com.wallee.android.till.sdk.data.TransactionCompletionResponse;
import com.wallee.android.till.sdk.data.TransactionResponse;
import com.wallee.android.till.sdk.data.TransactionVoidResponse;
import com.wallee.android.till.sdk.data.TransmissionResult;

class MockResponseHandler extends ResponseHandler {
    private final Context context;

    MockResponseHandler(Context context) {
        this.context = context;
    }

    @Override
    public void authorizeTransactionReply(TransactionResponse transactionResponse) {
        Intent intent = new Intent(context, TransactionResponseActivity.class);
        intent.putExtras(Utils.toBundle(transactionResponse));
        context.startActivity(intent);
    }

    @Override
    public void completeTransactionReply(TransactionCompletionResponse transactionCompletionResponse) {
        Intent intent = new Intent(context, TransactionCompletionResponseActivity.class);
        intent.putExtras(Utils.toBundle(transactionCompletionResponse));
        context.startActivity(intent);
    }

    @Override
    public void voidTransactionReply(TransactionVoidResponse transactionVoidResponse) {
        Intent intent = new Intent(context, TransactionVoidResponseActivity.class);
        intent.putExtras(Utils.toBundle(transactionVoidResponse));
        context.startActivity(intent);
    }

    @Override
    public void cancelLastTransactionOperationReply(CancelationResult cancelationResult) {
        Intent intent = new Intent(context, CancellationResultActivity.class);
        intent.putExtras(Utils.toBundle(cancelationResult));
        context.startActivity(intent);
    }

    @Override
    public void executeSubmissionReply(SubmissionResult submissionResult) {
        Intent intent = new Intent(context, SubmissionResultActivity.class);
        intent.putExtras(Utils.toBundle(submissionResult));
        context.startActivity(intent);
    }

    @Override
    public void executeTransmissionReply(TransmissionResult transmissionResult) {
        Intent intent = new Intent(context, TransmissionResultActivity.class);
        intent.putExtras(Utils.toBundle(transmissionResult));
        context.startActivity(intent);
    }

    @Override
    public void executeFinalBalanceReply(FinalBalanceResult finalBalanceResult) {
        Intent intent = new Intent(context, FinalBalanceResultActivity.class);
        intent.putExtras(Utils.toBundle(finalBalanceResult));
        context.startActivity(intent);
    }

    @Override
    public void executeGeneratePanTokenResponse(GeneratePanTokenResponse generatePanTokenResponse) {
        Intent intent = new Intent(context, GeneratePanTokenResponseActivity.class);
        intent.putExtras(Utils.toBundle(generatePanTokenResponse));
        context.startActivity(intent);
    }

    @Override
    public void executeGetConfigInfoResponse(GetPinpadInformationResponse configInfoResponse ) {
        Intent intent = new Intent(context, PinpadInformationResponseActivity.class);
        intent.putExtras(Utils.toBundle(configInfoResponse));
        context.startActivity(intent);
    }

    @Override
    public void executeGetConfigDataResponse(GetConfigDataResponse result) {
        Intent intent = new Intent(context, TerminalConfigurationDataResponseActivity.class);
        intent.putExtras(Utils.toBundle(result));
        context.startActivity(intent);
    }

    @Override
    public void executeConfigurationReply(ConfigurationResult result) {
        Intent intent = new Intent(context, ConfigurationResultActivity.class);
        intent.putExtras(Utils.toBundle(result));
        context.startActivity(intent);
    }

    @Override
    public void executeInitialisationReply(InitialisationResult result) {
        Intent intent = new Intent(context, InitialisationResultActivity.class);
        intent.putExtras(Utils.toBundle(result));
        context.startActivity(intent);
    }
}
