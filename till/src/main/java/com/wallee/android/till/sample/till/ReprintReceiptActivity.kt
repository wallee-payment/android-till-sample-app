package com.wallee.android.till.sample.till

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wallee.android.till.sample.till.ui.theme.AppTheme
import com.wallee.android.till.sdk.ApiClient

class ReprintReceiptActivity: ComponentActivity() {
    private val  apiClient: ApiClient = ApiClient(MockResponseHandler(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ReprintReceiptScreen(
                    onFinish = { finish() },
                    onSubmit = { apiClient.reprintReceipt() }
                )
            }
        }

        apiClient.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        apiClient.unbind(this)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReprintReceiptScreen(onFinish: () -> Unit, onSubmit: () -> Unit) {
    val isLoading = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.reprint_receipt)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.colorPrimaryDark),
                    titleContentColor = colorResource(R.color.cardview_light_background),
                    navigationIconContentColor = colorResource(R.color.colorPrimary)
                )
            )
        },
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                onClick = {
                    onSubmit()
                    isLoading.value = true
                }
            ) {
                Text(text = stringResource(R.string.start_request))
            }
            if (isLoading.value) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            Button(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                onClick = onFinish
            ) {
                Text(text = stringResource(R.string.exit))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReprintReceiptScreenPreview() {
    ReprintReceiptScreen(onFinish = {}, onSubmit = {})
}