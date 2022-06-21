package it.alirezasamadi.dinewithme

import io.micronaut.runtime.Micronaut.build

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("it.alirezasamadi.sts")
        .start()
}
