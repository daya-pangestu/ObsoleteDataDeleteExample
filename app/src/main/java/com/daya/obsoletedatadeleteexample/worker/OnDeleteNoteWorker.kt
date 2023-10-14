package com.daya.obsoletedatadeleteexample.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.daya.obsoletedatadeleteexample.db.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteNoteWorker(appContext : Context, parameters: WorkerParameters) : CoroutineWorker(appContext,parameters) {
    private val noteDao by lazy {
        NoteDatabase.getInstance(appContext).noteDao()
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        try {
            val oldestDataId = noteDao.getOldestDataId()
            noteDao.deleteById(oldestDataId)
            return@withContext Result.success()
        } catch (e: Exception) {
            return@withContext Result.failure()
        }
    }
}