package com.daya.obsoletedatadeleteexample.data

import androidx.room.Entity

@Entity
data class NoteEntity(
    val id : Int,
    val title : String,
    val note : String
)