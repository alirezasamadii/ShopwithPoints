package it.alirezasamadi.dinewithme.service

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import it.alirezasamadi.dinewithme.model.Wallet
import jakarta.inject.Singleton
import org.bson.Document
import org.bson.types.ObjectId


@Singleton
class WalletService(private val dbService: DatabaseService) {

    fun increasePoints(userId: String) {
        val existingWallet = findWalletByUserId(userId) ?: createWallet(userId)
        val query = Document().append("userID", userId)
        val updates = Updates.combine(
            Updates.set("points", (existingWallet.points ?: 0) + 1)
        )
        val result: UpdateResult = dbService.connection.getCollection("wallet")
            .updateOne(query, updates)
    }

    fun decreasePoints(userId: String, points: Int) {
        val existingWallet = findWalletByUserId(userId) ?:throw HttpStatusException(HttpStatus.NOT_FOUND,"")
        val query = Document().append("userID", userId)

        val decreasedPoints = (existingWallet.points ?: 0) - points
        if(decreasedPoints<0) {
            throw HttpStatusException(HttpStatus.BAD_REQUEST,"")
        }

        val updates = Updates.combine(
            Updates.set("points", decreasedPoints)
        )
        val result: UpdateResult = dbService.connection.getCollection("wallet")
            .updateOne(query, updates)
    }


    fun findWalletByUserId(userId: String): Wallet? {
        val equalComparison = Filters.eq("userID", userId)
        val result: ArrayList<Document> =
            dbService.connection.getCollection("wallet").find(equalComparison).limit(1)
                .into(ArrayList<Document>())
        return result.map { row ->
            Wallet().apply {
                _id = row.getObjectId("_id").toHexString()
                userID = row.getString("userID")
                points = row.getInteger("points")
            }
        }.firstOrNull()
    }

    fun createWallet(userId: String): Wallet {
        val walletId = ObjectId()
        //convert review to type document for the insert
        val insert = Document().append("_id", walletId)
            .append("userID", userId)
            .append("points", 0)

        val result = dbService.connection.getCollection("wallet").insertOne(insert)

        return Wallet().apply {
            userID = userId
            points = 0
            _id = walletId.toHexString()
        }
    }
}
