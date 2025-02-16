package com.sam.pokemondemo

import retrofit2.Response
import java.io.IOException

/**
 * Handle response errors by throwing exceptions
 */
fun <T> Response<T>.handleResponseError() {
    when {
        !isSuccessful -> {
            throw IOException(" ${code()} ${message()}")
        }

        body() == null -> {
            throw IOException(" ${code()} data empty")
        }
    }

}
