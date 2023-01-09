package com.teleferik.models.tickets

data class CreateTicketBodyRequest(
    val title: String,
    val description: String,
    val section: String = "AppIssues"
)
