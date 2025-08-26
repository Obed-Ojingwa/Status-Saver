package com.obed.thestatussaver.screens.normal

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


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
    Text(
        text = "Normal WhatsApp Photos Placeholder",
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun VideosContent() {
    Text(
        text = "Normal WhatsApp Videos Placeholder",
        modifier = Modifier.padding(16.dp)
    )
}
