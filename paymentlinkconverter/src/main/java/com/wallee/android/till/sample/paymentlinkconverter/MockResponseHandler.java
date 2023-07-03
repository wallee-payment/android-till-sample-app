package com.wallee.android.till.sample.paymentlinkconverter;

import android.content.Context;
import android.content.Intent;

import com.wallee.android.till.sdk.ResponseHandler;
import com.wallee.android.till.sdk.Utils;
import com.wallee.android.till.sdk.data.CancelationResult;
import com.wallee.android.till.sdk.data.FinalBalanceResult;
import com.wallee.android.till.sdk.data.GeneratePanTokenResponse;
import com.wallee.android.till.sdk.data.GetPinpadInformationResponse;
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
    /* For the needs of POC we use only one Transaction Type*/
    @Override
    public void authorizeTransactionReply(TransactionResponse transactionResponse) {
        Intent intent = new Intent(context, TransactionResponseActivity.class);
        intent.putExtras(Utils.toBundle(transactionResponse));
        context.startActivity(intent);
    }
}
