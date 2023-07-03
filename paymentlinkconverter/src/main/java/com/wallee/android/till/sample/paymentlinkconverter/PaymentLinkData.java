package com.wallee.android.till.sample.paymentlinkconverter;

import android.app.Application;

/* This class is used to hold PaymentLink data which has to be preserved between request and response */

public class PaymentLinkData extends Application {

    private String PaymentLinkClientDestination = null;

    public String getPaymentLinkClientDestination() {
        return PaymentLinkClientDestination;
    }

    public void setPaymentLinkClientDestination(String destination) {
        PaymentLinkClientDestination = destination;
    }

}
