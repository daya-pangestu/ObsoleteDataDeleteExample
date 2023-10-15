package com.daya.obsoletedatadeleteexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.daya.obsoletedatadeleteexample.data.NoteEntity
import com.daya.obsoletedatadeleteexample.ui.theme.ObsoleteDataDeleteExampleTheme
import com.daya.obsoletedatadeleteexample.worker.DeleteNoteWorker

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            ObsoleteDataDeleteExampleTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Item On Database",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            },
                            colors =TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                        )
                        MainScreen()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        deleteOldestData()
    }

    private fun deleteOldestData() {
        val deleteWorker = OneTimeWorkRequestBuilder<DeleteNoteWorker>().build()
        WorkManager.getInstance(this)
            .enqueueUniqueWork("delete_worker_tag",ExistingWorkPolicy.REPLACE,deleteWorker)
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val notes by viewModel.getAllNote.collectAsStateWithLifecycle(initialValue = emptyList())
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = notes
        ) { note ->
            ItemNote(note)
        }
    }
}

@Composable
fun ItemNote(note : NoteEntity) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = note.note,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ObsoleteDataDeleteExampleTheme {

    }
}