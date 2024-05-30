package com.example.keepnotes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.keepnotes.mode.Notes
import com.example.keepnotes.model.SecretNotes
import com.example.keepnotes.model.Todo


@Database(entities = [Notes::class,SecretNotes::class,Todo::class], version = 1)
abstract class NotesDataBase: RoomDatabase() {

    abstract fun getNotes(): NotesDao

    companion object {
        @Volatile
        var INSTANCE: NotesDataBase? = null

        fun getDatabase(context: Context): NotesDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            } else {
                synchronized(this) {
                    val roomDatabaseInstance = Room.databaseBuilder(
                        context, NotesDataBase::class.java, "all_notes"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = roomDatabaseInstance
                    return roomDatabaseInstance
                }
            }
        }
    }
}