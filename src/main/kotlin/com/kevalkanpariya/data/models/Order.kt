package com.kevalkanpariya.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val orderId: Int,
    val productId: Int,
    val orderBy: Int,
    val orderTo: Int,
    val transactionNo: String,
    val orderTimestamp: String,
    val orderAmt: String
)