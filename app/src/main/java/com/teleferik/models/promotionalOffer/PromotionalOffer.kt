package com.teleferik.models.promotionalOffer

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PromotionalOffer(
    @SerializedName("offers")
    @Expose
    val offers: List<Offer>
)
