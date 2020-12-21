package com.wallee.android.till.sample.till;

import android.content.Context;
import android.content.Intent;

import com.wallee.android.till.sdk.ResponseHandler;
import com.wallee.android.till.sdk.Utils;
import com.wallee.android.till.sdk.data.CancelationResult;
import com.wallee.android.till.sdk.data.FinalBalanceResult;
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
        Intent intent = new Intent(context, CancelationResultActivity.class);
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
}
