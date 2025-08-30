package com.obed.thestatussaver.screens.normal

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.obed.thestatussaver.repository.StatusRepository
import java.io.File


@Composable
@Preview
fun PreviewNormal(){
    NormalScreen()
}
@Composable
fun NormalScreen() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Photos", "Videos")

    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
            when (selectedTabIndex) {
                0 -> PhotosContent()
                1 -> VideosContent()
            }
        }
    }
}

@Composable
fun PhotosContent() {
    val context = LocalContext.current
    var photos by remember { mutableStateOf<List<File>>(emptyList()) }

    LaunchedEffect(Unit) {
        photos = StatusRepository.getPhotos(isBusiness = false)
    }

    if (photos.isEmpty()) {
        Text("No Photos Found", modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn {
            items(photos) { file ->
                Text(text = file.name, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun VideosContent() {
    val context = LocalContext.current
    var videos by remember { mutableStateOf<List<File>>(emptyList()) }

    LaunchedEffect(Unit) {
        videos = StatusRepository.getVideos(isBusiness = false)
    }

    if (videos.isEmpty()) {
        Text("No Videos Found", modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn {
            items(videos) { file ->
                Text(text = file.name, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

