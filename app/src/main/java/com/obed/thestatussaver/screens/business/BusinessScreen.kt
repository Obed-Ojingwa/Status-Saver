package com.obed.thestatussaver.screens.business



import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp






@Composable
@Preview
fun PreviewStatus(){
    BusinessScreen()
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BusinessScreen() {
    var selectedTabPosition by remember { mutableStateOf(0) }
    val tabs = listOf("Photos", "Videos")

    Scaffold (
        topBar = {
            TabRow(
                selectedTabIndex = selectedTabPosition,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, s ->
                    Tab(
                        selected = selectedTabPosition == index,
                        onClick = { selectedTabPosition = index },
                        text = { Text(s) }
                    )
                }
            }
        }
    ){innerPadding ->
        Box (modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ){
            when (selectedTabPosition){
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
