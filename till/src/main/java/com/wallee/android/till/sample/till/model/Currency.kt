package com.wallee.android.till.sample.till.model

data class Currency(val shortName:String ) {
    override fun toString(): String {
        return shortName
    }
}
