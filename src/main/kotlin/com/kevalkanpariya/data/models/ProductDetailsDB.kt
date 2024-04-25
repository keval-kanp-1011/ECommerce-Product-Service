package com.kevalkanpariya.data.models

import kotlinx.serialization.Serializable

@Serializable
data class InsertProductDetailsDB(
    val name: String,
    val desc: String,
    val mrp: String,
    val sellingPrice: String
)
@Serializable
data class EditProductDetailsDB(
    val name: String? = null,
    val desc: String? = null,
    val mrp: String? = null,
    val sellingPrice: String? = null
)
