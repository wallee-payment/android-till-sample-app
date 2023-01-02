package com.wallee.android.till.sample.till.model

data class Language(private val name: String, val code: String) {
    override fun toString(): String {
        return name
    }
}