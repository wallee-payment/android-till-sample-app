package com.wallee.android.till.sample.till

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wallee.android.till.sample.till.Utils.showToast
import com.wallee.android.till.sample.till.model.ErrorCode
import com.wallee.android.till.sdk.Utils
import com.wallee.android.till.sdk.data.GetCustomConfigurationResponse

class GetCustomConfigurationResponseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.get_custom_config_response_activity)

        val result = Utils.getCustomConfigurationResponse(intent.extras)
        if (result != null) {
            if (result.resultCode.code == ErrorCode.ERR_CONNECTION_FAILED.code) {
                showToast(this, resources.getString(R.string.app_relaunch))
                Utils.handleFailedToConnectVpj(this)
            }
            val textViewResult = findViewById<TextView>(R.id.textViewResult)
            textViewResult.text = resultToString(result)
        }

        findViewById<View>(R.id.okButton).setOnClickListener {
            val intent = Intent(
                this,
                MainActivity::class.java
            )
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun resultToString(result: GetCustomConfigurationResponse): String {
        return """
            state - ${result.state}
            resultCode code - ${result.resultCode.code}
            resultCode description - ${result.resultCode.description}
            customConfig - ${result.customConfiguration}
            """.trimIndent()
    }

}