package com.wallee.android.till.sample.till.model

import android.util.Base64
import android.util.Log
import com.wallee.android.till.sample.till.Utils

data class DPLineItem(
    val name: String = "Terminal",
    val type: String = ItemType.PRODUCT.value,
    val quantity: Double = 1.0,
    val totalAmountIncludingTax: String = "10.00"
)

enum class TransactionType(val value: String) {
    PURCHASE("PURCHASE"),
    CREDIT("CREDIT"),
    RESERVATION("RESERVATION"),
    RESERVATION_ADJ("RESERVATION_ADJ"),
    PURCHASE_RESERVATION("PURCHASE_RESERVATION"),
    CANCEL_RESERVATION("CANCEL_RESERVATION");
}

enum class ItemType(val value: String) {
    PRODUCT("PRODUCT"),
    TIP("TIP");
}

enum class ShowTrxResultScreen(val value: String) {
    TRUE("true"),
    FALSE("false");
}


data class DeepLinkRequest(
    var currencyCode: String = "CHF",
    var transactionType: String = TransactionType.PURCHASE.value,
    var reserveReference: String = "",
    var acquirerId: String = "99999999998",
    var merchantReference: String = "Merchant123",
    var invoiceMerchantReference: String = "Invoice123",
    var callbackUrl: String = "link://v1/response?extra_arg1=Keyboard&extra_arg2=Printers",
    var dlLineItems: List<DPLineItem> = listOf(),
    var showTrxResultScreens: String = ShowTrxResultScreen.TRUE.value //Default is true

) {
    fun generateV1Request(): String {
        val base = "wallee://v1/transaction"
        val parameters = mutableListOf<String>()
        parameters.add("currencyCode=$currencyCode")
        parameters.add("transactionTypeDeepLink=$transactionType")
        if (acquirerId.isNotEmpty() && transactionType == TransactionType.CANCEL_RESERVATION.value) parameters.add(
            "acquirerId=$acquirerId"
        )

        if (reserveReference.isNotEmpty() && transactionType in listOf(
                TransactionType.RESERVATION_ADJ.value,
                TransactionType.PURCHASE_RESERVATION.value,
                TransactionType.CANCEL_RESERVATION.value
            )
        ) parameters.add("reserveReference=$reserveReference")

        if (transactionType in listOf(
                TransactionType.PURCHASE.value,
                TransactionType.CREDIT.value,
                TransactionType.RESERVATION.value,
                TransactionType.RESERVATION_ADJ.value
            )
        ) {
            parameters.add("merchantReference=$merchantReference")
            parameters.add("invoiceMerchantReference=$invoiceMerchantReference")

        }

        if (showTrxResultScreens.isNotEmpty()) {
            parameters.add("showTrxResultScreens=$showTrxResultScreens")
        }

        dlLineItems.forEachIndexed { index, item ->
            parameters.add("lineItems[$index].name=${item.name}")
            parameters.add("lineItems[$index].type=${item.type}")
            parameters.add("lineItems[$index].quantity=${item.quantity}")
            parameters.add("lineItems[$index].totalAmountIncludingTax=${item.totalAmountIncludingTax}")
        }

        parameters.add("callback=$callbackUrl")

        val params = parameters.joinToString("&")

        val encodeParameters = Utils.encodeToBase64(params)

        return "$base?${encodeParameters}"
    }

}

