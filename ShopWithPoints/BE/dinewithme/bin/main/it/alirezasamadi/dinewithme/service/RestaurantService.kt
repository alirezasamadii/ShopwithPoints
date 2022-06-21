package it.alirezasamadi.dinewithme.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import it.alirezasamadi.dinewithme.model.Restaurant
import jakarta.inject.Singleton
import org.bson.Document
import java.lang.ref.Cleaner.create

@Singleton
class RestaurantService(val databaseService: DatabaseService) {

    fun getRestaurantList(): List<Restaurant> {
        //queries data on table restaurants
        val result: ArrayList<Document> = databaseService.connection.getCollection("restaurants")
                        .find()
                        .limit(100)
                        .into(ArrayList<Document>())

        //converts mongoDB resultSet to list of restaurants
        return result.map { row ->
            Restaurant().apply {
                _id = row.getObjectId("_id").toHexString()
                name = row.getString("name")
                image = row.getString("image")
                latitude = row.getDouble("latitude")
                longitude = row.getDouble("longitude")
            }

        }
    }
}
