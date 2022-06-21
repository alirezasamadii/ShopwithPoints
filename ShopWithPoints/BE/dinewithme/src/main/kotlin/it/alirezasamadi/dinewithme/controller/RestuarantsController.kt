package it.alirezasamadi.dinewithme.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.RequestAttribute
import io.swagger.v3.oas.annotations.Parameter
import it.alirezasamadi.dinewithme.model.Restaurant
import it.alirezasamadi.dinewithme.service.RestaurantService
import jakarta.inject.Inject
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Controller
open class RestuarantsController(val restaurantService: RestaurantService) {

@Get("/api/restaurants")
fun getRestaurants(
    @QueryValue(defaultValue = "") latitude: Double?,
    @QueryValue(defaultValue = "") longitude: Double?
): List<Restaurant> {
    return restaurantService.getRestaurantList(latitude, longitude)
}
}
