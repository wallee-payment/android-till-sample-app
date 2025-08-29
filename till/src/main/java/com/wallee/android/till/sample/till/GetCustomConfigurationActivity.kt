package com.wallee.android.till.sample.till

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.wallee.android.till.sdk.ApiClient

class GetCustomConfigurationActivity: AppCompatActivity() {
    private lateinit var client: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.get_custom_config_activity)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        findViewById<Button>(R.id.executeButton).setOnClickListener {
            try {
                progressBar.visibility = View.VISIBLE
                client.getCustomConfiguration()
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