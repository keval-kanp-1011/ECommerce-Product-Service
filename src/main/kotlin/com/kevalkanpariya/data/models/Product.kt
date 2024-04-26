package com.kevalkanpariya.data.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateStatement

@Serializable
data class Product(
    val productId: Int,
    val name: String,
    val desc: String,
    val mrp: String,
    val sellingPrice: String
)


object Products : Table(), OptimisticLockingAware {

    val productId = integer("id").autoIncrement()
    val name = varchar("name", 20)
    val desc = varchar("email", 20)
    val mrp = varchar("MRP", 10)
    val sellingPrice = varchar("selling_price", 10)
    val lastModifiedBy = integer("last_modified_by")
    val version = integer("version")

    override val primaryKey: PrimaryKey = PrimaryKey(productId)
    override val optimisticLockingVersion: Column<Int> = version

}

interface OptimisticLockingAware {
    val optimisticLockingVersion: Column<Int>
}

fun <T> T.optimisticLockingAwareUpdate(
    where: (SqlExpressionBuilder.() -> Op<Boolean>)? = null,
    version: Int,
    limit: Int? = null,
    body: T.(UpdateStatement) -> Unit
): Int where T : Table, T : OptimisticLockingAware {
    val optimisticLockingAwareWhereClause: (SqlExpressionBuilder.() -> Op<Boolean>) = {
        val versionEquals = this@optimisticLockingAwareUpdate.optimisticLockingVersion eq version
        when (where != null) {
            true -> where(this).and(versionEquals)
            else -> versionEquals
        }
    }

    val updateVersionAwareStatement: T.(UpdateStatement) -> Unit = {
        body(it)
        it[this.optimisticLockingVersion] = version + 1
    }

    return this.update(optimisticLockingAwareWhereClause, limit, updateVersionAwareStatement)
}


//
//open class StringIdTable(name: String = "products", columnName: String = "id", columnLength: Int = 10) : IdTable<String>(name) {
//    override val id: Column<EntityID<String>> = varchar(columnName, columnLength).entityId()
//    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }
//}
