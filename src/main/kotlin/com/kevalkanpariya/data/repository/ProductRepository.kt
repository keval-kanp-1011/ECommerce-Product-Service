package com.kevalkanpariya.data.repository

import com.kevalkanpariya.data.models.EditProductDetailsDB
import com.kevalkanpariya.data.models.InsertProductDetailsDB
import com.kevalkanpariya.data.models.Product
import com.kevalkanpariya.util.BasicResponse

interface ProductRepository {

    suspend fun createProduct(
        insertProductDetailsDB: InsertProductDetailsDB
    ): BasicResponse<Product>

    suspend fun deleteProduct(
        productId: Int
    ): BasicResponse<Boolean>

    suspend fun editProductDetails(
        productId: Int,
        editProductDetailsDB: EditProductDetailsDB
    ): BasicResponse<Boolean>

    suspend fun fetchProducts(

    ): BasicResponse<List<Product>>

    suspend fun fetchProductById(
        productId: Int
    ): BasicResponse<Product>
}