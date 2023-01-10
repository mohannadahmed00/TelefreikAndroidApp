package com.teleferik.models.seats

import com.teleferik.R

data class Seat(
    var num : Int,
    var isSelected:Boolean,
    var status:String,
    var color : Int = R.color.green
)
