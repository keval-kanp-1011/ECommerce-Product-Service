package com.kevalkanpariya.data.local

import com.kevalkanpariya.data.local.DatabaseSingleton.dbQuery
import com.kevalkanpariya.data.models.EditProductDetailsDB
import com.kevalkanpariya.data.models.InsertProductDetailsDB
import com.kevalkanpariya.data.models.Product
import com.kevalkanpariya.data.models.Products
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

class ProductDAOImpl: ProductDAO {

    fun resultRowToProduct(row: ResultRow) = Product(
        productId = row[Products.productId],
        name = row[Products.name],
        desc = row[Products.desc],
        mrp = row[Products.mrp],
        sellingPrice = row[Products.sellingPrice]
    )
    override suspend fun insertProduct(productDetails: InsertProductDetailsDB): Product? = dbQuery {
        val insertStatement = Products.insert {
            it[Products.name] = productDetails.name
            it[Products.mrp] = productDetails.mrp
            it[Products.sellingPrice] = productDetails.sellingPrice
            it[Products.desc] = productDetails.desc
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToProduct)
    }

    override suspend fun deleteProduct(productId: Int): Boolean = dbQuery {

        Products.deleteWhere {  Products.productId eq productId } > 0
    }

    override suspend fun editProductDetails(productId: Int, productDetails: EditProductDetailsDB): Boolean = dbQuery {


        synchronized("$productId" + productDetails.name) {


            Products.update(where = { Products.productId eq productId}) {
                //delay(3000)
                productDetails.name?.let { name ->
                    it[Products.name] = name
                }

                productDetails.desc?.let {desc ->
                    it[Products.desc] = productDetails.desc
                }

                productDetails.mrp?.let {desc ->
                    it[Products.mrp] = productDetails.mrp
                }

                productDetails.sellingPrice?.let {desc ->
                    it[Products.sellingPrice] = productDetails.sellingPrice
                }




            } > 0
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