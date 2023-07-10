package com.wallee.android.till.sample.paymentlinkconverter;

import android.app.Application;

/* This class is used to hold PaymentLink data which has to be preserved between request and response */

public class PaymentLinkData extends Application {

    private String PaymentLinkClientSuccess = null;
    private String PaymentLinkClientFailure = null;

    /*
    Storing receiptId here as it's not really needed within Wallee system but only from client App
    as they want to have some identifier receiving back for the transaction the initiated
    They named it as receiptId and this is relevant only to them and has no value on trx for Wallee
    therefore it is not included in TransactionResponse but sent back as some extra in the intent
    */
    private String PaymentLinkClientReceiptId = null;

    public String getPaymentLinkClientSuccess() {
        return PaymentLinkClientSuccess;
    }

    public void setPaymentLinkClientSuccess(String paymentLinkClientSuccess) {
        PaymentLinkClientSuccess = paymentLinkClientSuccess;
    }

    public String getPaymentLinkClientFailure() {
        return PaymentLinkClientFailure;
    }

    public void setPaymentLinkClientFailure(String paymentLinkClientFailure) {
        PaymentLinkClientFailure = paymentLinkClientFailure;
    }

    public String getPaymentLinkClientReceiptId() {
        return PaymentLinkClientReceiptId;
    }

    public void setPaymentLinkClientReceiptId (String receiptId) {
        PaymentLinkClientReceiptId = receiptId;
    }

}
