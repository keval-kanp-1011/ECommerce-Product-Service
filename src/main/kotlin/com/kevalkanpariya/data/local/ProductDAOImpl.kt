package com.kevalkanpariya.data.local

import com.kevalkanpariya.data.local.DatabaseSingleton.dbQuery
import com.kevalkanpariya.data.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID
import kotlin.collections.singleOrNull

class ProductDAOImpl: ProductDAO {

    fun resultRowToProduct(row: ResultRow) = Product(
        productId = row[Products.productId],
        name = row[Products.name],
        desc = row[Products.desc],
        mrp = row[Products.mrp],
        sellingPrice = row[Products.sellingPrice]
    )
    override suspend fun insertProduct(userId: Int, productDetails: InsertProductDetailsDB): Product? = dbQuery {
        val insertStatement = Products.insert {
            it[Products.name] = productDetails.name
            it[Products.mrp] = productDetails.mrp
            it[Products.sellingPrice] = productDetails.sellingPrice
            it[Products.desc] = productDetails.desc
            it[Products.lastModifiedBy] = userId
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToProduct)
    }

    override suspend fun deleteProduct(productId: Int): Boolean = dbQuery {

        Products.deleteWhere {  Products.productId eq productId } > 0
    }

    override suspend fun editProductDetails(userId: Int,productId: Int, productDetails: EditProductDetailsDB): Boolean = dbQuery {

        val updatedRows = Products.optimisticLockingAwareUpdate(where = { Products.productId eq productId }, version = 0 ) {
            // update something
            Products.update(where = { Products.productId eq productId}) {
                //delay(3000)
                productDetails.name?.let { name ->
                    it[Products.name] = name
                }

                it[Products.lastModifiedBy] = userId

                productDetails.desc?.let {desc ->
                    it[Products.desc] = productDetails.desc
                }

                productDetails.mrp?.let {desc ->
                    it[Products.mrp] = productDetails.mrp
                }

                productDetails.sellingPrice?.let {desc ->
                    it[Products.sellingPrice] = productDetails.sellingPrice
                }

            }

        }

        if (updatedRows == 0) {
            throw RuntimeException("Optimistic locking occurred")
        } else {
            true
        }




    }

    override suspend fun fetchProducts(): List<Product> = dbQuery{
        Products
            .selectAll()
            .map(::resultRowToProduct)

    }

    override suspend fun fetchProductsById(productId: Int): Product? = dbQuery{
        Products
            .select {
                Products.productId eq productId
            }
            .map(::resultRowToProduct)
            .singleOrNull()
    }
}