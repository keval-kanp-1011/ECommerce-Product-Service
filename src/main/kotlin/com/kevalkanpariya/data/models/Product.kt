package com.kevalkanpariya.data.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

@Serializable
data class Product(
    val productId: Int,
    val name: String,
    val desc: String,
    val mrp: String,
    val sellingPrice: String
)



object Products: Table() {

    val productId = integer("id").autoIncrement()
    val name = varchar("name", 20)
    val desc = varchar("email", 20)
    val mrp = varchar("MRP", 10)
    val sellingPrice = varchar("selling_price", 10)

    override val primaryKey: PrimaryKey = PrimaryKey(productId)

}


//
//open class StringIdTable(name: String = "products", columnName: String = "id", columnLength: Int = 10) : IdTable<String>(name) {
//    override val id: Column<EntityID<String>> = varchar(columnName, columnLength).entityId()
//    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }
//}
