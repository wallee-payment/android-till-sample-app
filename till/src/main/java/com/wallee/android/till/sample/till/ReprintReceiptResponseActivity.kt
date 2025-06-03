package com.wallee.android.till.sample.till

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.wallee.android.till.sample.till.ui.theme.AppTheme
import com.wallee.android.till.sdk.TillLog
import com.wallee.android.till.sdk.Utils
import com.wallee.android.till.sdk.data.CancelationResult
import com.wallee.android.till.sdk.data.ReprintReceiptResponse
import com.wallee.android.till.sdk.data.TransactionResponse
import com.wallee.android.till.sdk.data.TransactionVoidResponse

class ReprintReceiptResponseActivity : ComponentActivity() {
    private var screenTitle = "Reprint Receipt Response"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val response = Utils.getReprintReceiptResponse(intent.extras)
        Log.e(screenTitle, Gson().toJson(response))
        TillLog.debug("Log reprint receipt response  -> " + responseToString(response))

        setContent {
            AppTheme {
                ReprintReceiptResponseScreen(
                    onFinish = {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    },
                    title = screenTitle,
                    responseContent = responseToString(response)
                )
            }
        }
    }

    private fun responseToString(response: ReprintReceiptResponse): String {
        return when {
            response.lastTrx != null -> transactionResponseToString(response.lastTrx)
            response.lastRev != null -> cancellationResponseToString(response.lastRev)
            response.lastCancelRes != null -> reservationCancellationToString(response.lastCancelRes)
            else -> responseErrorToString(response)
        }
    }

    private fun responseErrorToString(response: ReprintReceiptResponse): String {
        return """
            state - ${response.state}
            resultCode code - ${response.resultCode.code}
            resultCode description - ${response.resultCode.description}
        """.trimIndent()
    }

    private fun transactionResponseToString(response: TransactionResponse?): String {
        if (response == null) return ""
        screenTitle = "Reprint - Last Transaction"
        return """
            state - ${response.state}
            resultCode code - ${response.resultCode.code}
            resultCode description - ${response.resultCode.description}
            authorizationCode - ${response.authorizationCode}
            terminalId - ${response.terminalId}
            sequenceCount - ${response.sequenceCount}
            transactionResult - ${response.transactionResult}
            amountAuth - ${response.amountAuth}
            amountAuthCurrency - ${response.amountAuthCurrency}
            amountRemaining - ${response.amountRemaining}
            transactionTime - ${response.transactionTime}
            reserveReference - ${response.reserveReference}
            acquirerId - ${response.acquirerId}
            cardIssuingCountry - ${response.cardIssuingCountry}
            cardAppLabel - ${response.cardAppLabel}
            cardAppId - ${response.cardAppId}
            cardNumber - ${response.cardNumber}
            cardExpirationDate - ${response.cardExpirationDate}
            dccTransactionAmount - ${response.dccTransactionAmount}
            dccTransactionCurrency - ${response.dccTransactionCurrency}
            cvm - ${response.cvm}
            partialApprovalFlag - ${response.partialApprovalFlag}
            amountTip - ${response.amountTip}
            panToken - ${response.panToken}
            merchantReference - ${response.merchantReference}
            transactionSyncNumber - ${response.transactionSyncNumber}
            paymentEntryMethod - ${response.paymentEntryMethod}
            """.trimIndent()
    }

    private fun cancellationResponseToString(response: CancelationResult?): String {
        if (response == null) return ""
        screenTitle = "Reprint - Last Reverse Transaction"
        return """
                state - ${response.state}
                resultCode code - ${response.resultCode.code}
                resultCode description - ${response.resultCode.description}
                terminalId - ${response.terminalId}
                sequenceCount - ${response.sequenceCount}
                cancelledSequenceCount - ${response.cancelledSequenceCount}
                amountRev - ${response.transactionAmount}
                amountRevCurr - ${response.transactionCurrency}
                cardNumber - ${response.cardNumber}
                cardSeqNumber - ${response.cardSeqNumber}
                cardExpDate - ${response.cardExpDate}
                cardAppLabel - ${response.cardAppLabel}
                cardAppId - ${response.cardAppId}
                acquirerId - ${response.acquirerId}
                transactionTime - ${response.transactionTime}
                """.trimIndent()
    }

    private fun reservationCancellationToString(response: TransactionVoidResponse?): String {
        if (response == null) return ""
        screenTitle = "Reprint - Last Reservation Cancellation"
        return """
                 state - ${response.state}
                 resultCode code - ${response.resultCode.code}
                 resultCode description - ${response.resultCode.description}
                 terminalId - ${response.terminalId}
                 sequenceCount - ${response.sequenceCount}
                 transactionTime - ${response.transactionTime}
                 acquirerId - ${response.acquirerId}
                 transactionReferenceNumber - ${response.transactionRefNumber}
                 amountAuth - ${response.amountAuth}
                 amountAuthCurrency - ${response.amountAuthCurr}
                 """.trimIndent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReprintReceiptResponseScreen(
    onFinish: () -> Unit,
    title: String = "Reprint Receipt Response",
    responseContent: String = "No response content"
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.colorPrimaryDark),
                    titleContentColor = colorResource(R.color.cardview_light_background),
                    navigationIconContentColor = colorResource(R.color.colorPrimary)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                Text(text = responseContent)
            }
            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                onClick = onFinish
            ) {
                Text(text = stringResource(R.string.exit))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReprintReceiptResponseScreenPreview() {
    ReprintReceiptResponseScreen(onFinish = {})
}