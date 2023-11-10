package com.example.penguinpay.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.penguinpay.api.ExchangeRateService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionViewModel : ViewModel() {
    var firstName by mutableStateOf("")
        private set

    fun updateFirstName(input: String) {
        firstName = input
    }

    var lastName by mutableStateOf("")
        private set

    fun updateLastName(input: String) {
        lastName = input
    }

    var phoneNumber by mutableStateOf("")
        private set
    var formattedPhoneNumber by mutableStateOf("")
        private set

    fun updatePhoneNumber(input: String) {
        phoneNumber = input
        formattedPhoneNumber = countryCode + phoneNumber
    }

    var binaryAmount by mutableStateOf("")
        private set

    var exchangeAmount by mutableDoubleStateOf(0.0)
        private set

    fun updateAmount(input: String) {
        binaryAmount = input
    }

    var country by mutableStateOf(Country.UNKNOWN)
        private set

    fun updateCountry(input: Country) {
        country = input
        when (country) {
            Country.UNKNOWN -> countryCode = ""
            Country.KENYA -> {
                countryCode = "+254"
                formattedPhoneNumber = countryCode + phoneNumber
            }

            Country.NIGERIA -> {
                countryCode = "+234"
                formattedPhoneNumber = countryCode + phoneNumber
            }

            Country.TANZANIA -> {
                countryCode = "+255"
                formattedPhoneNumber = countryCode + phoneNumber
            }

            Country.UGANDA -> {
                countryCode = "+256"
                formattedPhoneNumber = countryCode + phoneNumber
            }
        }
        viewModelScope.launch {
            refreshExchangeRates(country.currency)
        }
    }

    var countryCode by mutableStateOf("")
        private set
    private var exchangeRate by mutableDoubleStateOf(0.0)

    private fun updateExchangeAmount() {
        exchangeAmount = if (!binaryAmount.isNullOrEmpty()) {
            binaryAmount.toInt(2) * exchangeRate
        } else 0.0
    }

    fun isReviewEnabled(): Boolean{
        return isNameValid() && isPhoneNumberValid() && isCountryValid() && isAmountValid()
    }

    private fun isNameValid(): Boolean {
        return !firstName.isNullOrEmpty() && !lastName.isNullOrEmpty()
    }

    private fun isPhoneNumberValid(): Boolean {
        return !phoneNumber.isNullOrEmpty() && phoneNumber.length == country.digits
    }

    private fun isCountryValid(): Boolean {
        return country != Country.UNKNOWN
    }

    private fun isAmountValid(): Boolean {
        return !binaryAmount.isNullOrEmpty()
    }

    /**
     * TODO: Wait for refreshExchangeRates to return before moving to review
     * Ideally we want the most up-to-date exchange rate when we advance to the
     * review screen
     */
    fun onReviewClicked() {
        viewModelScope.launch {
            refreshExchangeRates(country.currency)
        }
    }

    fun onSubmitClicked() {
        firstName = ""
        lastName = ""
        phoneNumber = ""
        country = Country.UNKNOWN
        countryCode = ""
        binaryAmount = ""
    }

    /**
     * Given more time this would be moved to a repository class.
     * Typically I'd have a repository that used both a local database and
     * the network service. But I think with currency exchange rates, they go
     * out of date fast, so there's not much reason to store them
     */
    private suspend fun refreshExchangeRates(symbol: String) {
        withContext(Dispatchers.IO) {
            val exchangeRates =
                ExchangeRateService
                    .ExchangeRateNetwork
                    .exchangeRates
                    .getLatestExchangeRates(
                        "5a990adc320d4f59971c2f3cd8792711",
                        symbols = symbol
                    )
            exchangeRate = exchangeRates.rates.getValue(symbol)
            updateExchangeAmount()
        }
    }
}

enum class Country(val countryString: String, val currency: String, val digits: Int) {
    UNKNOWN("Select Country", "", 0),
    KENYA("Kenya", "KES", 9),
    NIGERIA("Nigeria", "NGN", 7),
    TANZANIA("Tanzania", "TZS", 9),
    UGANDA("Uganda", "UGX", 7)
}