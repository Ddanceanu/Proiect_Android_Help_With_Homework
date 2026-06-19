package com.example.proiecthelpwithhomework

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Homework::class, Comment::class, CommentVote::class], version = 14, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun homeworkDao(): HomeworkDao
    abstract fun commentDao(): CommentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "homework_db_v14"
                )
                .addCallback(DatabaseCallback(context))
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            populateDatabase(database.homeworkDao(), database.commentDao())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            suspend fun populateDatabase(homeworkDao: HomeworkDao, commentDao: CommentDao) {
                // Andrei Popescu (Author)
                val h1Id = homeworkDao.insert(Homework(
                    title = "Ecuatii de gradul 2",
                    subject = "Matematica",
                    description = "Am nevoie de ajutor la rezolvarea ecuatiei x^2 - 5x + 6 = 0. Nu inteleg cum se aplica formula discriminantului.",
                    duration = "1 ora",
                    postedBy = "Andrei Popescu",
                    isSolved = false
                )).toInt()
                
                commentDao.insert(Comment(
                    homeworkId = h1Id,
                    authorName = "Profesor Vasilescu",
                    text = "Discriminantul (delta) se calculeaza: b^2 - 4ac. In cazul tau: (-5)^2 - 4*1*6 = 25 - 24 = 1.",
                    likes = 0
                ))
                
                commentDao.insert(Comment(
                    homeworkId = h1Id,
                    authorName = "Maria Ionescu",
                    text = "Formula radacinilor este x = (-b +/- sqrt(delta)) / 2a. Incearca sa inlocuiesti b cu -5, a cu 1 si delta cu 1.",
                    likes = 0
                ))

                // Maria Ionescu (Author)
                val h2Id = homeworkDao.insert(Homework(
                    title = "Algoritm sortare",
                    subject = "Informatica",
                    description = "Cum implementez Bubble Sort in C++? Am incercat ceva dar nu imi sorteaza corect elementele.",
                    duration = "2 ore",
                    postedBy = "Maria Ionescu",
                    isSolved = false
                )).toInt()
                
                commentDao.insert(Comment(
                    homeworkId = h2Id,
                    authorName = "Andrei Popescu",
                    text = "Iata un exemplu de Bubble Sort: for(int i=0; i<n-1; i++) for(int j=0; j<n-i-1; j++) if(a[j]>a[j+1]) swap(a[j], a[j+1]);",
                    likes = 0
                ))

                // Andrei Popescu (Author)
                val h3Id = homeworkDao.insert(Homework(
                    title = "Legea lui Ohm",
                    subject = "Fizica",
                    description = "Care este legatura dintre intensitate, tensiune si rezistenta intr-un circuit electric simplu?",
                    duration = "45 min",
                    postedBy = "Andrei Popescu",
                    isSolved = false
                )).toInt()
                
                commentDao.insert(Comment(
                    homeworkId = h3Id,
                    authorName = "Profesor Vasilescu",
                    text = "Legea lui Ohm spune ca I = U / R. Intensitatea este direct proportionala cu tensiunea si invers proportionala cu rezistenta.",
                    likes = 0
                ))
            }
        }
    }
}
