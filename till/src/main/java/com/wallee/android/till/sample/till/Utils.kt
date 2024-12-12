package com.wallee.android.till.sample.till

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

object Utils {

    fun hideKeyboardFrom(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showToast(activity: Activity?, message: String?) {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun decodeFromBase64(encoded: String): String {
        return try {
            val uri = Uri.parse(encoded)
            val decodedBytes = Base64.decode(uri.query, Base64.DEFAULT)
            String(decodedBytes)
        } catch (e: IllegalArgumentException) {
            encoded
        } catch (e: Exception) {
            encoded
        }
    }

    fun encodeToBase64(input: String): String {
        return Base64.encodeToString(input.toByteArray(), Base64.DEFAULT).trim()
    }
}
