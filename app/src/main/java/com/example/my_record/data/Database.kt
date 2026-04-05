package com.example.my_record

//import androidx.room.Database
//import androidx.room.RoomDatabase
//import com.example.my_record.MemoDao
//import com.example.my_record.MemoEntity
//
//@Database(entities = [MemoEntity::class], version = 1)
//abstract class MemoDatabase : RoomDatabase() {
//    abstract fun memoDao(): MemoDao
//}

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RecordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recordDao(): RecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "record.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}