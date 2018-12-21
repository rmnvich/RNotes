package rmnvich.apps.notes.data.repositories.datasource

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

}