package rmnvich.apps.notes.data.repositories.datasource

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import rmnvich.apps.notes.data.repositories.datasource.dao.NoteDao
import rmnvich.apps.notes.data.repositories.datasource.dao.ReminderDao
import rmnvich.apps.notes.data.repositories.datasource.dao.TagDao
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.Reminder
import rmnvich.apps.notes.domain.entity.Tag

@Database(entities = [Note::class, Tag::class, Reminder::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun noteDao() : NoteDao

    abstract fun tagDao() : TagDao

    abstract fun reminderDao(): ReminderDao
}