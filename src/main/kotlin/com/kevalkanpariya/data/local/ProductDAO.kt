package com.kevalkanpariya.data.local

import com.kevalkanpariya.data.models.EditProductDetailsDB
import com.kevalkanpariya.data.models.InsertProductDetailsDB
import com.kevalkanpariya.data.models.Product


interface ProductDAO {

    suspend fun insertProduct(
        productDetails: InsertProductDetailsDB
    ): Product?

    suspend fun deleteProduct(
        productId: Int
    ): Boolean

    suspend fun editProductDetails(
        productId: Int,
        productDetails: EditProductDetailsDB
    ): Boolean

    suspend fun fetchProducts(
    ): List<Product>

    suspend fun fetchProductsById(
        productId: Int
    ): Product?
}