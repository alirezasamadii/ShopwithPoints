package it.alirezasamadi.dinewithme.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import it.alirezasamadi.dinewithme.model.Restaurant
import it.alirezasamadi.dinewithme.model.Table
import jakarta.inject.Singleton
import org.bson.Document
import java.lang.ref.Cleaner.create
import com.mongodb.client.model.Filters.*


@Singleton
class TableService(val databaseService: DatabaseService) {
    fun getTableList(restaurantID:String): List<Table> {
        val equalComparison = eq("restaurantID",restaurantID)
        //queries data on table restaurants
        val result: ArrayList<Document> = databaseService.connection.getCollection("tables")
            .find(equalComparison)
            .limit(100)
            .into(ArrayList<Document>())
        //converts mongoDB resultSet to list of restaurants
        return result.map { row ->
            Table().apply {
                _id = row.getObjectId("_id").toHexString()
                tableNumber = row.getInteger("tableNumber")
                numberOfSeats = row.getInteger("numberOfSeats")
                this.restaurantID = row.getString("restaurantID")
            }

        }
    }
}