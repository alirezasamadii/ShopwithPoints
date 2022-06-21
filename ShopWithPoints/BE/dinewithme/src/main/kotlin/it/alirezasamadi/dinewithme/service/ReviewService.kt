package it.alirezasamadi.dinewithme.service

import com.mongodb.client.model.Filters
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import io.netty.util.internal.ObjectCleaner
import it.alirezasamadi.dinewithme.model.Review
import it.alirezasamadi.dinewithme.model.Table
import jakarta.inject.Singleton
import org.bson.Document
import org.bson.types.ObjectId
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotFoundException
@Singleton
class ReviewService(val databaseService: DatabaseService, val walletService: WalletService) {

    fun createReview(restaurantID: String, review: Review): Review {
        val filter = Filters.and(
            Filters.eq("userID",review.userID),
            Filters.eq("restaurantID",restaurantID)
        )
        val filterResult: ArrayList<Document> =
            databaseService.connection.getCollection("reviews").find(filter).limit(1)
                .into(ArrayList<Document>())
        if (filterResult.isNotEmpty())
            throw HttpStatusException(HttpStatus.BAD_REQUEST, "You have already posted a review! Delete previous one to post a new review")

        review.restaurantID = restaurantID
        val reviewId = ObjectId()
        //convert review to type document for the insert
        var insert = Document().append("_id", reviewId)
            .append("restaurantID", review.restaurantID)
            .append("reviewText", review.reviewText)
            .append("score", review.score)
            .append("userID", review.userID)
            .append("userName", review.userName)
            .append("createdAt", review.createdAt)

        review.userID?.let { walletService.increasePoints(it) }

        val result = databaseService.connection.getCollection("reviews").insertOne(insert)
        review._id = reviewId.toHexString()

        return review
    }


    fun getReviews(restaurantID: String): List<Review> {
        val equalComparison = Filters.eq("restaurantID", restaurantID)
        //queries data on reviews of  restaurants
        val result: ArrayList<Document> =
            databaseService.connection.getCollection("reviews").find(equalComparison).limit(100)
                .into(ArrayList<Document>())
        //converts mongoDB resultSet to list of restaurants
        return result.map { row ->
            Review().apply {
                _id = row.getObjectId("_id").toHexString()
                reviewText = row.getString("reviewText")
                score = row.getInteger("score")
                this.restaurantID = row.getString("restaurantID")
                userID = row.getString("userID")
                createdAt = row.getLong("createdAt")
                userName = row.getString("userName")
            }
        }
    }

    fun getSingleReview(restaurantID: String, reviewID: String): Review {
        val equalComparison = Filters.eq("_id", ObjectId(reviewID))
        val result: ArrayList<Document> =
            databaseService.connection.getCollection("reviews").find(equalComparison).limit(1)
                .into(ArrayList<Document>())
        //converts mongoDB resultSet to list of restaurants
        return result.map { row ->
            Review().apply {
                _id = row.getObjectId("_id").toHexString()
                reviewText = row.getString("review")
                score = row.getInteger("score")
                this.restaurantID = row.getString("restaurantID")
                userID = row.getString("userID")
                createdAt = row.getLong("createdAt")
                userName = row.getString("userName")
            }
        }.firstOrNull() ?: throw HttpStatusException(HttpStatus.NOT_FOUND, "review not found")
    }
    fun deleteSingleReview(restaurantID: String,reviewID: String){
        val equalComparison = Filters.eq("_id", ObjectId(reviewID))
        getSingleReview(restaurantID, reviewID)
        databaseService.connection.getCollection("reviews").deleteOne(equalComparison)
    }

}
