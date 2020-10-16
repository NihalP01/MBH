package com.mridx.c_mbh.Utils

import android.content.Context
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPrefManager(context: Context) {

    companion object {
        val LOGGED_IN = preferencesKey<Boolean>("logged_in")
        val USER_ID = preferencesKey<Int>("user_id")
    }

    private val dataStore = context.createDataStore("user_pref")

    val userIdFlow: Flow<Int> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[USER_ID] ?: -1
        }

    suspend fun setUserId(id: Int) {
        dataStore.edit {
            it[USER_ID] = id
        }
    }

}