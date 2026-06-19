package com.example.feature.auth.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest

@Database(
    entities = [UserEntity::class],
    version = 11,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "auth_db_final"
                )
                .addCallback(DatabaseCallback())
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.userDao())
                    }
                }
            }

            private fun hashPassword(password: String): String {
                val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
                return bytes.joinToString("") { "%02x".format(it) }
            }

            suspend fun populateDatabase(userDao: UserDao) {
                // Andrei Popescu
                userDao.insert(UserEntity(
                    name = "Andrei Popescu",
                    email = "andrei@test.ro",
                    passwordHash = hashPassword("parola123")
                ))
                // Maria Ionescu
                userDao.insert(UserEntity(
                    name = "Maria Ionescu",
                    email = "maria@test.ro",
                    passwordHash = hashPassword("parola123")
                ))
                // Profesor Vasilescu
                userDao.insert(UserEntity(
                    name = "Profesor Vasilescu",
                    email = "profesor@test.ro",
                    passwordHash = hashPassword("parola123")
                ))
            }
        }
    }
}
