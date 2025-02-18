package com.sam.pokemondemo.source.repo

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceRepository @Inject constructor(
    private val sharedPreference: SharedPreferences,
) {
    var isLoadFinished: Boolean
        get() = sharedPreference.getBoolean(KEY_LOAD_FINISH, false)
        set(value) {
            sharedPreference.edit { putBoolean(KEY_LOAD_FINISH, value) }
        }

    companion object {
        const val KEY_LOAD_FINISH = "KEY_LOAD_FINISH"
    }
}
