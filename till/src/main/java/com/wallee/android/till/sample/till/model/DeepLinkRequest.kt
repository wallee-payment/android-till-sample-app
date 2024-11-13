package com.wallee.android.till.sample.till.model

data class DPLineItem(
    val name: String = "Terminal",
    val type: String = "PRODUCT",
    val quantity: Double = 1.0,
    val totalAmountIncludingTax: String= "10.00"
)


data class DeepLinkRequest(
    var currencyCode: String = "CHF",
    var transactionType: String = "PURCHASE",
    var reserveReference: String = "",
    var acquirerId: String = "99999999998",
    var merchantReference: String = "Ref123",
    var invoiceMerchantReference: String = "Inv123",
    var callbackUrl: String = "till://v1?extra_arg1=19-02-2024&extra_arg2=Printers",
    var DPLineItems: List<DPLineItem> = listOf()

) {
    fun generateV1Request(): String {
        val base = "wallee://v1/transaction"
        val parameters = mutableListOf<String>()
        parameters.add("currencyCode=$currencyCode")
        parameters.add("transactionTypeDeepLink=$transactionType")
        if (acquirerId.isNotEmpty() && transactionType == "CANCEL_RESERVATION") parameters.add("acquirerId=$acquirerId")

        if (reserveReference.isNotEmpty() && transactionType in listOf("RESERVATION_ADJ", "PURCHASE_RESERVATION", "CANCEL_RESERVATION")) parameters.add("reserveReference=$reserveReference")

        if (transactionType in listOf("PURCHASE", "CREDIT","RESERVATION","RESERVATION_ADJ")) {
            parameters.add("merchantReference=$merchantReference")
            parameters.add("invoiceMerchantReference=$invoiceMerchantReference")

        }

        DPLineItems.forEachIndexed { index, item ->
            parameters.add("lineItems[$index].name=${item.name}")
            parameters.add("lineItems[$index].type=${item.type}")
            parameters.add("lineItems[$index].quantity=${item.quantity}")
            parameters.add("lineItems[$index].totalAmountIncludingTax=${item.totalAmountIncludingTax}")
        }

        parameters.add("callback=$callbackUrl")

        return "$base?${parameters.joinToString("&")}"
    }
}

