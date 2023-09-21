package com.wallee.android.till.sample.till

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wallee.android.till.sample.till.common.GetAllCurrenciesUseCase
import com.wallee.android.till.sample.till.common.getTextAsString
import com.wallee.android.till.sample.till.common.setVisibleOrGone
import com.wallee.android.till.sample.till.databinding.AuthorizeTransactionActivityBinding
import com.wallee.android.till.sample.till.model.Languages
import com.wallee.android.till.sdk.ApiClient
import com.wallee.android.till.sdk.TillLog
import com.wallee.android.till.sdk.data.LineItem
import com.wallee.android.till.sdk.data.Transaction
import com.wallee.android.till.sdk.data.TransactionProcessingBehavior
import java.math.BigDecimal
import java.util.*

class AuthorizeTransactionActivity : AppCompatActivity() {

    private var client: ApiClient? = null
    private var languageCode: String? = null

    private var _binding: AuthorizeTransactionActivityBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var lastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = AuthorizeTransactionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        setUpLanguagesSpinner()
        setupCurrencySpinner()
        setupOnClickListener()
        client = ApiClient(MockResponseHandler(this))
        client?.bind(this)
        TillLog.debug("SampleApp: Wallee ApiClient is just bound!")
    }

    private fun authorizeTransaction() {
        with(binding) {
            val amountString = editTextAmount.getTextAsString()
            val currencyString = currencySpinner.selectedItem.toString()
            val customTextString = editTextCustomText.getTextAsString()

            if (amountString.isBlank()) {
                Toast.makeText(
                    this@AuthorizeTransactionActivity,
                    "Amount field is empty",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val lineItems = LineItem.ListBuilder("foo", BigDecimal(amountString))
                    .current
                    .setName("bar")
                    .listBuilder
                    .build()

                val transaction: Transaction
                val transactionBuilder = Transaction.Builder(lineItems)
                    .setCurrency(Currency.getInstance(currencyString))
                    .setInvoiceReference("1")
                    .setMerchantReference("MREF-123")
                    .setTransactionProcessingBehavior(if (shouldReserve.isChecked) TransactionProcessingBehavior.RESERVE else TransactionProcessingBehavior.COMPLETE_IMMEDIATELY)
                    .setGeneratePanToken(generatePanToken.isChecked)
                if (customTextString.isNotEmpty()) {
                    transactionBuilder.customText = customTextString
                }
                if (selectLanguage.isChecked) transactionBuilder.language = languageCode
                transaction = transactionBuilder.build()
                TillLog.debug("VSD Start Transaction of amount  -> $amountString")
                try {
                    client?.authorizeTransaction(transaction)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setUpLanguagesSpinner() {
        val languages = Languages().languages
        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            languages
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.languageSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                languageCode = languages[position].code
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.languageSpinner.adapter = arrayAdapter
    }

    private fun setupCurrencySpinner() {
        val getAllCurrenciesUseCase = GetAllCurrenciesUseCase()
        val currencies = getAllCurrenciesUseCase.execute()
        val arrayAdapter = ArrayAdapter(
            this,
            R.layout.item_simple,
            currencies
        )
        binding.currencySpinner.adapter = arrayAdapter
    }

    private fun setupOnClickListener() {
        with(binding) {
            selectLanguage.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                languageSpinner.setVisibleOrGone(isChecked)
            }
            authorizeButton.setOnClickListener {
                // preventing double, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                    return@setOnClickListener
                }
                lastClickTime = SystemClock.elapsedRealtime();
                authorizeTransaction()
            }
            returnButton.setOnClickListener { finish() }
            authorizeTransactionParent.setOnClickListener {
                Utils.hideKeyboardFrom(this@AuthorizeTransactionActivity)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        client?.unbind(this)
    }
}
