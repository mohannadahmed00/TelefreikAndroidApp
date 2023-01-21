package com.teleferik.ui.privateTrip

import androidx.lifecycle.ViewModel

class PrivateTripViewModel(private val privateRepo: PrivateRepo) : ViewModel() {
    private var tripType = 1  // 0 go only // 1 go and return

    fun getTripType(): Int {
        return tripType
    }

    fun setTripType(type: Int) {
        tripType = type
    }
    // TODO: Implement the ViewModel
}