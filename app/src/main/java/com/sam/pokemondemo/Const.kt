package com.sam.pokemondemo

import retrofit2.Response
import java.io.IOException

/**
 * 處理 Response 的錯誤拋出錯誤
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
