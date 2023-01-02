package com.wallee.android.till.sample.till.common

import com.wallee.android.till.sample.till.model.Currency

/**
 * Move under domain layer when refactoring to clean architecture
 */
class GetAllCurrenciesUseCase {

    private val currenciesList = listOf(
        Currency("CHF"),
        Currency("BHD"),
        Currency("EUR"),
        Currency("GBP"),
        Currency("HRK"),
        Currency("HUF"),
        Currency("JPY"),
        Currency("PLN"),
        Currency("RON"),
        Currency("RUB"),
        Currency("USD"),
    )

    fun execute() = currenciesList

}