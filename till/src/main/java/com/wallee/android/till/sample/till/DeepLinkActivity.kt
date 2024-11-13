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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.wallee.android.till.sample.till.common.GetAllCurrenciesUseCase
import com.wallee.android.till.sample.till.model.DPLineItem
import com.wallee.android.till.sample.till.model.DeepLinkRequest


class DeepLinkActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeepLinkScreen()
        }
    }
}

@Composable
fun DeepLinkScreen() {
    val deepLinkRequest = remember { mutableStateOf(DeepLinkRequest()) }
    val lineItems = remember { mutableStateListOf(DPLineItem()) }
    val context = LocalContext.current as Activity
    val scrollState = rememberScrollState()
    val transactionTypes = listOf(
        "PURCHASE",
        "CREDIT",
        "RESERVATION",
        "RESERVATION_ADJ",
        "PURCHASE_RESERVATION",
        "CANCEL_RESERVATION"
    )
    val currencies = GetAllCurrenciesUseCase().execute().map { it.shortName }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        DropdownMenuSpinner(
            selectedItem = deepLinkRequest.value.currencyCode,
            items = currencies,
            label = "Currency Code",
            onItemSelected = { deepLinkRequest.value = deepLinkRequest.value.copy(currencyCode = it) }
        )

        DropdownMenuSpinner(
            selectedItem = deepLinkRequest.value.transactionType,
            items = transactionTypes,
            label = "Transaction Type",
            onItemSelected = {
                deepLinkRequest.value = deepLinkRequest.value.copy(transactionType = it)
            }
        )

        if (deepLinkRequest.value.transactionType in listOf(
                "PURCHASE",
                "CREDIT",
                "RESERVATION",
                "RESERVATION_ADJ"
            )
        ) {


            OutlinedTextField(
                value = deepLinkRequest.value.merchantReference,
                onValueChange = {
                    deepLinkRequest.value = deepLinkRequest.value.copy(merchantReference =  it)
                },
                label = { Text("Merchant Reference") },
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )

            OutlinedTextField(
                value = deepLinkRequest.value.invoiceMerchantReference,
                onValueChange = {
                    deepLinkRequest.value = deepLinkRequest.value.copy(invoiceMerchantReference =  it)
                },
                label = { Text("Invoice Merchant Reference") },
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
        }


        if (deepLinkRequest.value.transactionType in listOf(
                "PURCHASE",
                "CREDIT",
                "RESERVATION",
                "RESERVATION_ADJ",
                "PURCHASE_RESERVATION"
            )
        ) {

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
                    showRemoveButton = lineItems.size > 1
                )
            }

            Button(
                onClick = {
                    lineItems.add(DPLineItem())
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)

            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Line Item",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("+ Add Line Item")
            }


        }



        if (deepLinkRequest.value.transactionType in listOf(
                "RESERVATION_ADJ",
                "PURCHASE_RESERVATION",
                "CANCEL_RESERVATION"
            )
        ) {
            OutlinedTextField(
                value = deepLinkRequest.value.reserveReference,
                onValueChange = { reserveReference ->
                    deepLinkRequest.value = deepLinkRequest.value.copy(reserveReference = reserveReference.trim())
                },
                label = { Text("Reserve Reference") },
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
        }

        if (deepLinkRequest.value.transactionType == "CANCEL_RESERVATION") {
            OutlinedTextField(
                value = deepLinkRequest.value.acquirerId,
                onValueChange = {
                    deepLinkRequest.value = deepLinkRequest.value.copy(acquirerId = it)
                },
                label = { Text("Acquirer ID") },
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                deepLinkRequest.value = deepLinkRequest.value.copy(DPLineItems = lineItems)
                val deepLinkUrl = deepLinkRequest.value.generateV1Request()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkUrl))
                intent.flags = (Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent);
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Request")
        }

        Button(
            onClick = {
                context.finish()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Return")
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
                        contentDescription = "Dropdown Icon"
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
    showRemoveButton: Boolean
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
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
                text = "Item",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)

            )

            if (showRemoveButton) {
                IconButton(onClick = onRemoveItem) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove Line Item",
                        tint = Color.Red
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenuSpinner(
            selectedItem = lineItem.type,
            items = listOf("PRODUCT", "TIP"),
            label = "Type",
            onItemSelected = { newType ->
                onLineItemChange(lineItem.copy(type = newType))
            }
        )


        OutlinedTextField(
            value = lineItem.name,
            onValueChange = { newName ->
                onLineItemChange(lineItem.copy(name = newName.trim()))
            },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            )
        )

        OutlinedTextField(
            value = lineItem.quantity.toString(),
            onValueChange = { newQuantity ->
                val parseQuantity = newQuantity.toDoubleOrNull() ?: 1.0
                onLineItemChange(lineItem.copy(quantity = parseQuantity))
            },
            label = { Text("Quantity") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            )
        )


        OutlinedTextField(
            value = lineItem.totalAmountIncludingTax,
            onValueChange = { newAmount ->
                onLineItemChange(lineItem.copy(totalAmountIncludingTax = newAmount.trim()))
            },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            )

        )
    }
}
