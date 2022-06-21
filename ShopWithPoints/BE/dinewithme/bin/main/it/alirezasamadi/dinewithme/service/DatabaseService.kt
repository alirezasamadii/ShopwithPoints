package it.alirezasamadi.dinewithme.service

import com.mongodb.client.MongoClients
import jakarta.inject.Singleton
// SINGLETON CREATES A UNIQUE INSTANCE ACCESSIBLE IN ALL THE PROJECT
@Singleton
class DatabaseService {
    private val uri = "mongodb+srv://DineWithMe:DineWithMe@cluster0.tbs2c.mongodb.net/myFirstDatabase?retryWrites=true&w=majority"
    private val mongoClient = MongoClients.create(uri)
    val connection = mongoClient.getDatabase("myFirstDatabase")
}