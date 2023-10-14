package com.daya.obsoletedatadeleteexample.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.daya.obsoletedatadeleteexample.NoteDao
import com.daya.obsoletedatadeleteexample.data.NoteEntity
import java.util.concurrent.Executors


@Database(
    entities = [NoteEntity::class],
    version = 1
)
abstract class NoteDatabase : RoomDatabase()  {
    abstract fun noteDao() :NoteDao
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
                    .addCallback(object :Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Executors.newSingleThreadScheduledExecutor().execute {
                                getInstance(context).noteDao().addAll(prePopulateData)
                            }
                        }
                    })
                    .build()
            }
            return INSTANCE!!
        }

        val prePopulateData = listOf(
            NoteEntity(title = "Manfaat lari", note = "Lari pagi membantu menaikkan semangat setiap hari"),
            NoteEntity(title = "Daftar Belanja", note = "Sembako,Baju Anak,sereal,buah-buahan,kacang,wortel,sop-sopan"),
            NoteEntity(title = "Jam meeting", note = "13.00,16.00,21.00"),
            NoteEntity(title = "Karyawan Baru", note = "Satria adhi,Reyhan maulana yafi,Amelia carla,Shindy Pradnya saraswati,Septiani,Eko Sanjaya"),
        )
    }
}