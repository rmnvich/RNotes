package rmnvich.apps.notes.data.repositories

import android.content.Context
import org.jetbrains.anko.defaultSharedPreferences
import rmnvich.apps.notes.data.common.Constants.KEY_PIN_EXISTS
import rmnvich.apps.notes.domain.repositories.PreferencesRepository

class PreferencesRepositoryImpl(applicationContext: Context) : PreferencesRepository {

    private val sharedPreferences = applicationContext.defaultSharedPreferences

    override fun isPinCodeExists(): Boolean {
        return sharedPreferences.getBoolean(KEY_PIN_EXISTS, false)
    }

    override fun savePinCode() {
        sharedPreferences.edit().putBoolean(KEY_PIN_EXISTS, true).apply()
    }

    override fun deletePinCode() {
        sharedPreferences.edit().putBoolean(KEY_PIN_EXISTS, false).apply()
    }
}