package com.daya.obsoletedatadeleteexample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.daya.obsoletedatadeleteexample.db.NoteDatabase

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val noteDao by lazy {
        NoteDatabase.getInstance(getApplication()).let { it.noteDao() }
    }

    val getAllNote = noteDao.getAllNote()
}