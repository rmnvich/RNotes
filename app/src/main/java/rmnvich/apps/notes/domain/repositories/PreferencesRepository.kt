package rmnvich.apps.notes.domain.repositories

interface PreferencesRepository {

    fun isPinCodeExists(): Boolean

    fun savePinCode()

    fun deletePinCode()
}