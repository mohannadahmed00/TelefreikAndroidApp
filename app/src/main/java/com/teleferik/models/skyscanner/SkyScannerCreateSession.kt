package com.teleferik.models.skyscanner

data class SkyScannerCreateSession(
    val cabinclass: String = "Economy",
    val country: String = "UK",
    val currency: String = "GBP",
    val locale: String = "en-GB",
    val locationSchema: String = "iata",
    val originplace: String = "EDI",
    val destinationplace: String = "LHR",
    val outbounddate: String = "2021-12-30",
    val inbounddate: String = "2022-01-02",
    val adults: Int = 1,
    val children: Int = 0,
    val infants: Int = 0
)
