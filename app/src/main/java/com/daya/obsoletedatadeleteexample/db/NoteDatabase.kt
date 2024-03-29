package com.daya.obsoletedatadeleteexample.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.daya.obsoletedatadeleteexample.data.NoteEntity
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit
import kotlin.time.toDuration


@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabase : RoomDatabase()  {
    abstract fun noteDao() : NoteDao
    companion object{
        private var INSTANCE: NoteDatabase? = null

        @Synchronized
        fun getInstance(context: Context): NoteDatabase {
            if (INSTANCE == null) {
                INSTANCE = buildDb(context)
            }
            return INSTANCE!!
        }
        @Synchronized
        private fun buildDb(context: Context): NoteDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room
                    .databaseBuilder(
                        context,
                        NoteDatabase::class.java,
                        "NoteDb"
                    )
                    .addCallback(object : Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Executors.newSingleThreadScheduledExecutor().execute {
                                getInstance(context).noteDao().addAll(prePopulateData())
                            }
                        }
                    })
                    .build()
            }
            return INSTANCE!!
        }

        /**
         * simulates adding records to the db one day apart for each new record
        * */
        fun prePopulateData(): List<NoteEntity> {
            val today = System.currentTimeMillis().toDuration(DurationUnit.MILLISECONDS).inWholeDays
            val yesterday = today - 1.days.inWholeDays
            val twoDaysAgo = today - 2.days.inWholeDays
            val threeDaysAgo = today - 3.days.inWholeDays
            return listOf(
                NoteEntity(
                    title = "Benefit of running",
                    note = "Routinely run every morning could help boost the mood all day long",
                    createdTime = threeDaysAgo
                ),
                NoteEntity(
                    title ="Grocery list",
                    note = "eggs,paper,chili,bread,milk,peanuts,salt",
                    createdTime = twoDaysAgo
                ),
                NoteEntity(
                    title = "Today's Meeting",
                    note = "13.00,16.00,21.00",
                    createdTime = yesterday
                ),
                NoteEntity(
                    title = "New Employees",
                    note = "Satria adhi,Reyhan maulana yafi,Amelia carla, Shindy Pradnya saraswati,Septiani, Eko Sanjaya, Ayu rohmana",
                    createdTime = today
                ),
            )
        }
    }
}