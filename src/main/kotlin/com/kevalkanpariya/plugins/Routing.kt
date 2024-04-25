package com.kevalkanpariya.plugins

import com.kevalkanpariya.data.local.ProductDAOImpl
import com.kevalkanpariya.data.models.EditProductDetailsDB
import com.kevalkanpariya.data.models.InsertProductDetailsDB
import com.kevalkanpariya.data.models.Order
import com.kevalkanpariya.data.repoImpl.ProductRepoImpl
import com.kevalkanpariya.security.JwtConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(client: HttpClient) {

    val productDao = ProductDAOImpl()

    val productRepo = ProductRepoImpl(productDao)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        authenticate {
            get("/products") {
                val response = productRepo.fetchProducts()
                call.respond(response.data?: "products are not available")
            }

            get("products/{id}/orders") {
                val productId = call.parameters["id"]?.toIntOrNull()?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val data = client.get {
                    url("http://order-service/orders")
                    //parameter("id", productId)
                    header("authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJlY29tbWVyY2UtYXV0aCIsImF1ZCI6ImVjb21tZXJjZS1hdXRoIiwiaWQiOjF9.MZQlON3DmFhrz5lpGAaEBcJijm8rWIDOFwfZ838qpH4")
                }

                val d = data.body<List<Order>>()

                println("ddddd: $d")

                call.respond(d)
            }

            post("/create-product") {
                val productDetails = runCatching {
                    call.receive<InsertProductDetailsDB>()

                }.getOrNull()?: kotlin.run {
                    call.respond(HttpStatusCode.OK)
                    call.respondText("${call.receive<InsertProductDetailsDB>()}")
                    return@post
                }

                val response = productRepo.createProduct(productDetails)

                call.respond(response.data?:"product is not created ")
            }

            put("/edit-product") {
                val productId = call.parameters["id"]?.toIntOrNull()?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@put
                }
                val productDetails = call.receive<EditProductDetailsDB>()

                val response = productRepo.editProductDetails(productId,productDetails)

                call.respond(response.data?:"data not found")
            }

            delete("/delete-product") {

                val productId = call.parameters["id"]?.toIntOrNull()?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }

                val response = productRepo.deleteProduct(productId)

                call.respond(response.data?:"response not available")

            }
        }
    }
}
