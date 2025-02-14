package com.sam.pokemondemo.source.repo

import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceRepository @Inject constructor(
    private val sharedPreference: SharedPreferences,
    private val moshi: Moshi,
) {
    val isEverLoad: Boolean
        get() = sharedPreference.contains(KEY_FIRST_LOAD_FINISH)

    var isFirstTimeLoadFinished: Boolean
        get() = sharedPreference.getBoolean(KEY_FIRST_LOAD_FINISH, false)
        set(value) {
            sharedPreference.edit { putBoolean(KEY_FIRST_LOAD_FINISH, value) }
        }

    companion object {
        const val KEY_FIRST_LOAD_FINISH = "KEY_FIRST_LOAD_FINISH"
    }
}
