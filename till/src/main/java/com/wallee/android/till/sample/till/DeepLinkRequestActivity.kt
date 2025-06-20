package com.wallee.android.till.sample.till

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.wallee.android.till.sample.till.common.GetAllCurrenciesUseCase
import com.wallee.android.till.sample.till.model.DPLineItem
import com.wallee.android.till.sample.till.model.DeepLinkRequest
import com.wallee.android.till.sample.till.model.ItemType
import com.wallee.android.till.sample.till.model.ShowTrxResultScreen
import com.wallee.android.till.sample.till.model.TransactionType
import com.wallee.android.till.sample.till.ui.theme.AppTheme


class DeepLinkRequestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                DeepLinkScreen()
            }
        }
    }
}

@Composable
fun DeepLinkScreen() {
    val deepLinkRequest = remember { mutableStateOf(DeepLinkRequest()) }
    val lineItems = remember { mutableStateListOf(DPLineItem()) }
    val context = LocalContext.current as Activity
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val transactionTypes = TransactionType.entries.map { it.value }
    val currencies = GetAllCurrenciesUseCase().execute().map { it.shortName }
    val focusManager = LocalFocusManager.current
    val showResultScreenTypes = ShowTrxResultScreen.entries.map { it.value }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        DropdownMenuSpinner(
            selectedItem = deepLinkRequest.value.currencyCode,
            items = currencies,
            label = stringResource(R.string.currency_code),
            onItemSelected = { newCurrencyCode ->
                deepLinkRequest.value = deepLinkRequest.value.copy(currencyCode = newCurrencyCode)
            }
        )

        DropdownMenuSpinner(
            selectedItem = deepLinkRequest.value.transactionType,
            items = transactionTypes,
            label = stringResource(R.string.transaction_type),
            onItemSelected = { newTransactionType ->
                deepLinkRequest.value = deepLinkRequest.value.copy(transactionType = newTransactionType)
            }
        )

        if (deepLinkRequest.value.transactionType in listOf(
                TransactionType.PURCHASE.value,
                TransactionType.CREDIT.value,
                TransactionType.RESERVATION.value,
                TransactionType.RESERVATION_ADJ.value
            )
        ) {


            OutlinedTextField(
                value = deepLinkRequest.value.merchantReference,
                onValueChange = { newMerchantReferenceValue ->
                    deepLinkRequest.value = deepLinkRequest.value.copy(merchantReference = newMerchantReferenceValue)
                },
                label = { Text(stringResource(R.string.merchant_reference)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )

            )

            OutlinedTextField(
                value = deepLinkRequest.value.invoiceMerchantReference,
                onValueChange = { newInvoiceMerchantReference ->
                    deepLinkRequest.value =
                        deepLinkRequest.value.copy(invoiceMerchantReference = newInvoiceMerchantReference)
                },
                label = { Text(stringResource(R.string.invoice_merchant_reference)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
        }

        DropdownMenuSpinner(
            selectedItem = deepLinkRequest.value.showTrxResultScreens,
            items = showResultScreenTypes,
            label = stringResource(R.string.show_trx_result_screen),
            onItemSelected = { newShowResultScreens ->
                deepLinkRequest.value = deepLinkRequest.value.copy(showTrxResultScreens = newShowResultScreens)
            }
        )


        if (deepLinkRequest.value.transactionType in listOf(
                TransactionType.PURCHASE.value,
                TransactionType.CREDIT.value,
                TransactionType.RESERVATION.value,
                TransactionType.RESERVATION_ADJ.value,
                TransactionType.PURCHASE_RESERVATION.value
            )
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            lineItems.forEachIndexed { index, item ->
                LineItemFields(
                    lineItem = item,
                    onLineItemChange = { updatedItem ->
                        lineItems[index] = updatedItem

                    },
                    onRemoveItem = {
                        if (lineItems.size > 1) {
                            lineItems.removeAt(index)
                        }

                    },
                    showRemoveButton = lineItems.size > 1,
                    focusManager = focusManager
                )
            }

            Button(
                onClick = {
                    lineItems.add(DPLineItem())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),

                ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_line_item),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.add_line_item))
            }


        }


        if (deepLinkRequest.value.transactionType in listOf(
                TransactionType.RESERVATION_ADJ.value,
                TransactionType.PURCHASE_RESERVATION.value,
                TransactionType.CANCEL_RESERVATION.value
            )
        ) {
            OutlinedTextField(
                value = deepLinkRequest.value.reserveReference,
                onValueChange = { reserveReference ->
                    deepLinkRequest.value =
                        deepLinkRequest.value.copy(reserveReference = reserveReference.trim())
                },
                label = { Text(stringResource(R.string.reserve_reference)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
        }

        if (deepLinkRequest.value.transactionType == TransactionType.CANCEL_RESERVATION.value) {
            OutlinedTextField(
                value = deepLinkRequest.value.acquirerId,
                onValueChange = {
                    deepLinkRequest.value = deepLinkRequest.value.copy(acquirerId = it)
                },
                label = { Text(stringResource(R.string.acquirer_id)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                deepLinkRequest.value = deepLinkRequest.value.copy(dlLineItems = lineItems)
                val deepLinkUrl = deepLinkRequest.value.generateV1Request()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkUrl))
                intent.flags = (Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent);
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.start_request))
        }

        Button(
            onClick = {
                    context.finish()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.return_value))
        }
    }
}

@Composable
fun DropdownMenuSpinner(
    selectedItem: String,
    items: List<String>,
    label: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = stringResource(R.string.dropdown_icon)
                    )
                }
            }
        )


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun LineItemFields(
    lineItem: DPLineItem,
    onLineItemChange: (DPLineItem) -> Unit,
    onRemoveItem: () -> Unit,
    showRemoveButton: Boolean,
    focusManager: FocusManager
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequest = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                BorderStroke(1.dp, Color.Gray),
                shape = MaterialTheme.shapes.small
            )
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                })
            }

    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.item),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)

            )

            if (showRemoveButton) {
                IconButton(onClick = onRemoveItem) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.remove_line_item),
                        tint = Color.Red
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenuSpinner(
            selectedItem = lineItem.type,
            items = ItemType.entries.map { it.value },
            label = stringResource(R.string.type),
            onItemSelected = { newType ->
                onLineItemChange(lineItem.copy(type = newType))
            }
        )


        OutlinedTextField(
            value = lineItem.name,
            onValueChange = { newName ->
                onLineItemChange(lineItem.copy(name = newName.trim()))
            },
            label = { Text(stringResource(R.string.name)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )

        OutlinedTextField(
            value = lineItem.quantity.toString(),
            onValueChange = { newQuantity ->
                val parseQuantity = newQuantity.toDoubleOrNull() ?: 1.0
                onLineItemChange(lineItem.copy(quantity = parseQuantity))
            },
            label = { Text(stringResource(R.string.quantity)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )


        OutlinedTextField(
            value = lineItem.totalAmountIncludingTax,
            onValueChange = { newAmount ->
                onLineItemChange(lineItem.copy(totalAmountIncludingTax = newAmount.trim()))
            },
            label = { Text(stringResource(R.string.amount)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )

        )
    }
}
