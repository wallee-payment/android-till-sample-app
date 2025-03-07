package com.wallee.android.till.sample.till

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.wallee.android.till.sdk.ApiClient
import com.wallee.android.till.sdk.data.CustomConfigurationRequest

class GetCustomConfigurationActivity: AppCompatActivity() {
    private lateinit var client: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.get_custom_config_activity)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE
        val applicationIdET = findViewById<EditText>(R.id.applicationIdEditText)

        findViewById<Button>(R.id.executeButton).setOnClickListener {
            try {
                progressBar.visibility = View.VISIBLE
                val applicationId = applicationIdET.text.toString()
                val request = CustomConfigurationRequest.Builder(applicationId).build()
                client.getCustomConfiguration(request)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        findViewById<View>(R.id.returnButton).setOnClickListener { finish() }

        client = ApiClient(MockResponseHandler(this))
        client.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        client.unbind(this)
    }
}