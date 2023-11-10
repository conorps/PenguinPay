package com.example.penguinpay.data

data class ExchangeRates(
    var disclaimer: String?,
    var license: String?,
    var timestamp: Long,
    var base: String,
    var rates: HashMap<String, Double>
)
