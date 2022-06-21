package it.alirezasamadi.dinewithme.controller

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import it.alirezasamadi.dinewithme.model.Review
import it.alirezasamadi.dinewithme.service.ReviewService

@Controller
open class ReviewsController(val reviewService: ReviewService) {

    @Post("/api/restaurants/{restaurantID}/reviews")
    @Status(HttpStatus.CREATED)
    fun createReview(@PathVariable("restaurantID") restaurantID:String,
                      @Body review: Review) : Review{
        return reviewService.createReview(restaurantID, review)
    }
    @Get("/api/restaurants/{restaurantID}/reviews")
    @Status(HttpStatus.OK)
    fun getReviews(@PathVariable("restaurantID") restaurantID:String): List<Review>{
        return reviewService.getReviews(restaurantID)
    }
    @Get("/api/restaurants/{restaurantID}/reviews/{reviewID}")
    @Status(HttpStatus.OK)
    fun getSingleReview(@PathVariable("restaurantID") restaurantID:String,
                        @PathVariable("reviewID") reviewID :String): Review{
        return reviewService.getSingleReview(restaurantID,reviewID)
    }
    @Delete("/api/restaurants/{restaurantID}/reviews/{reviewID}")
    @Status(HttpStatus.NO_CONTENT)
    fun deleteSingleReview(@PathVariable("restaurantID") restaurantID:String,
                           @PathVariable("reviewID") reviewID :String) {
        reviewService.deleteSingleReview(restaurantID,reviewID)
                           }
}