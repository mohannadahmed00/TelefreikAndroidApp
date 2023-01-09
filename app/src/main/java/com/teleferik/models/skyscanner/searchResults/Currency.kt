package com.teleferik.models.skyscanner.searchResults


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Currency(
    @SerializedName("Code")
    @Expose
    val code: String?,
    @SerializedName("DecimalDigits")
    @Expose
    val decimalDigits: Int?,
    @SerializedName("DecimalSeparator")
    @Expose
    val decimalSeparator: String?,
    @SerializedName("RoundingCoefficient")
    @Expose
    val roundingCoefficient: Int?,
    @SerializedName("SpaceBetweenAmountAndSymbol")
    @Expose
    val spaceBetweenAmountAndSymbol: Boolean?,
    @SerializedName("Symbol")
    @Expose
    val symbol: String?,
    @SerializedName("SymbolOnLeft")
    @Expose
    val symbolOnLeft: Boolean?,
    @SerializedName("ThousandsSeparator")
    @Expose
    val thousandsSeparator: String?
):Parcelable