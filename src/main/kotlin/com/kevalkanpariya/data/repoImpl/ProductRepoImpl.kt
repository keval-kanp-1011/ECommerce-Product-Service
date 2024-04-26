package com.kevalkanpariya.data.repoImpl

import com.kevalkanpariya.data.local.ProductDAO
import com.kevalkanpariya.data.models.EditProductDetailsDB
import com.kevalkanpariya.data.models.InsertProductDetailsDB
import com.kevalkanpariya.data.models.Product
import com.kevalkanpariya.data.repository.ProductRepository
import com.kevalkanpariya.util.BasicResponse

class ProductRepoImpl(
    private val productDAO: ProductDAO
): ProductRepository {
    override suspend fun createProduct(userId: Int, insertProductDetailsDB: InsertProductDetailsDB): BasicResponse<Product> {
        return try {
            val product = productDAO.insertProduct(userId, insertProductDetailsDB)
            BasicResponse.Success(data = product)
        } catch (e: Exception) {
            BasicResponse.Error(error = e.message?: "something went wrong")
        }
    }

    override suspend fun deleteProduct(productId: Int): BasicResponse<Boolean> {
        return try {
            val isDeleted = productDAO.deleteProduct(productId)
            BasicResponse.Success(data = isDeleted)
        } catch (e: Exception) {
            BasicResponse.Error(error = e.message?: "something went wrong")
        }
    }

    override suspend fun editProductDetails(
        userId: Int,
        productId: Int,
        editProductDetailsDB: EditProductDetailsDB
    ): BasicResponse<Boolean> {
        return try {

            //if user role is admin then allowed to edit
            val isEdited = productDAO.editProductDetails(userId,productId, editProductDetailsDB)

            BasicResponse.Success(data = isEdited)
        } catch (e: Exception) {
            BasicResponse.Error(error = e.message?: "something went wrong")
        }
    }

    override suspend fun fetchProducts(): BasicResponse<List<Product>> {
        return try {
            val products = productDAO.fetchProducts()
            BasicResponse.Success(data = products)
        } catch (e: Exception) {
            BasicResponse.Error(error = e.message?: "something went wrong")
        }
    }

    override suspend fun fetchProductById(productId: Int): BasicResponse<Product> {
        return try {
            val product = productDAO.fetchProductsById(productId)
            BasicResponse.Success(data = product)
        } catch (e: Exception) {
            BasicResponse.Error(error = e.message?: "something went wrong")
        }
    }
}