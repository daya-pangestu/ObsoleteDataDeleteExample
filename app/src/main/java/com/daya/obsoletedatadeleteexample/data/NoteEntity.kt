package com.daya.obsoletedatadeleteexample.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_entity")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title : String,
    val note : String
)