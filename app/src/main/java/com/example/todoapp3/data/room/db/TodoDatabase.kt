package com.example.todoapp3.data.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todoapp3.data.room.dao.TodoDao
import com.example.todoapp3.data.room.entity.DateConverter
import com.example.todoapp3.data.room.entity.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE todo_items ADD COLUMN files TEXT")
    }
}


/**
 * Singleton class for the Room database representing
 */
@Database(entities = [TodoItem::class], version = 2)
@TypeConverters(DateConverter::class)
abstract class TodoRoomDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao


    companion object {

        @Volatile
        private var INSTANCE: TodoRoomDatabase? = null

        fun getDatabase(
            context: Context
        ): TodoRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoRoomDatabase::class.java,
                    "todo_database67"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class TodoDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.todoDao())
                }
            }
        }

        suspend fun populateDatabase(todoDao: TodoDao) {
//            var todo = TodoItem(
//                id = "7ec71b48-4346-4cad-9659-897b7e1c8da3",
//                text = "Meditate for 10 minutes",
//                importance = Importance.HIGH,
//                deadline = null,
//                isCompleted = false,
//                createdAt = Date(),
//                modifiedAt = Date(),
//                isSynced = true,
//                isModified = true,
//                isDeleted = false
//            )
//            todoDao.insert(todo)
        }
    }
}

