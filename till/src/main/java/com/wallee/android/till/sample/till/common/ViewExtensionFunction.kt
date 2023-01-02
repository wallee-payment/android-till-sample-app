package com.wallee.android.till.sample.till.common

import android.view.View
import android.widget.EditText

/**
 * @param isVisible if true the visibility of the View is set to VISIBLE
 *                      otherwise to GONE
 */
fun View.setVisibleOrGone(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

/**
 * Obtain the text inside an editText
 */
fun EditText.getTextAsString() = text.toString()