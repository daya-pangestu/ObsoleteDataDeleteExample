package com.daya.obsoletedatadeleteexample

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.daya.obsoletedatadeleteexample.data.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * from note_entity")
    fun getAllNote() : Flow<List<NoteEntity>>

    @Query("DELETE FROM note_entity WHERE id =:noteId")
    suspend fun deleteById(noteId : Int) : Int

    @Insert
    fun addAll(data: List<NoteEntity>)

}