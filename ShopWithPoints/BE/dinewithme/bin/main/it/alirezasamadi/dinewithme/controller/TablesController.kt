package it.alirezasamadi.dinewithme.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.RequestAttribute
import io.swagger.v3.oas.annotations.Parameter
import it.alirezasamadi.dinewithme.model.Restaurant
import it.alirezasamadi.dinewithme.model.Table
import it.alirezasamadi.dinewithme.service.RestaurantService
import it.alirezasamadi.dinewithme.service.TableService
import jakarta.inject.Inject
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Controller
open class TablesController(val tableservice: TableService) {

    @Get("/api/restaurants/{restaurantID}/tables")
    fun getTables(@PathVariable restaurantID:String): List<Table> {
        return tableservice.getTableList(restaurantID)
    }

}
