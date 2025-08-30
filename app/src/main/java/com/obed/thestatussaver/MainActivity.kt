package com.obed.thestatussaver

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.obed.thestatussaver.screens.business.BusinessScreen
import com.obed.thestatussaver.screens.normal.NormalScreen
import com.obed.thestatussaver.ui.theme.TheStatusSaverTheme
import java.io.File

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.obed.thestatussaver.repository.StatusRepository


class MainActivity : ComponentActivity() {
    private lateinit var pickFolder: ActivityResultLauncher<Uri?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SAF picker (Android 11+)
        pickFolder = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            uri?.let {
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                getSharedPreferences("status_saver", MODE_PRIVATE)
                    .edit()
                    .putString("normal_path", it.toString())
                    .apply()
            }
        }

        enableEdgeToEdge() // ✅ keep your system bar styling

        setContent {
            TheStatusSaverTheme {
                StatusTabScreen(isBusiness = false, repository = StatusRepository)
                /*MainScreen(
                    onPickFolderClick = { openFolderPicker() }
                )*/
            }
        }
    }

    fun openFolderPicker() {
        pickFolder.launch(null)
    }
}

@Preview
@Composable
fun NewPrev(){
    StatusTabScreen(isBusiness = false, repository = StatusRepository)
}

/*@Composable
fun StatusTabScreen(
    isBusiness: Boolean, // pass true for business WhatsApp, false for normal
    repository: StatusRepository
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Photos, 1 = Videos

    val photos = remember(isBusiness) { repository.getPhotos(isBusiness) }
    val videos = remember(isBusiness) { repository.getVideos(isBusiness) }

    Column {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Photos") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Videos") }
            )
        }

        when (selectedTab) {
            0 -> StatusGrid(files = photos)
            1 -> StatusGrid(files = videos)
        }
    }
}*/

@Composable
fun StatusTabScreen(
    isBusiness: Boolean,
    repository: StatusRepository
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Photos, 1 = Videos

    val statusFiles = remember(selectedTab, isBusiness) {
        if (selectedTab == 0) {
            repository.getPhotos(isBusiness) // ✅ passes isBusiness
        } else {
            repository.getVideos(isBusiness) // ✅ passes isBusiness
        }
    }

    Column {
        // Tabs
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Photos") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Videos") }
            )
        }

        // List/Grid of files
        LazyColumn {
            items(statusFiles) { file ->
                Text(file.name) // later replace with Image/Video thumbnail
            }
        }
    }
}


@Composable
fun StatusGrid(files: List<File>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(files.size) { index ->
            val file = files[index]
            if (file.extension.lowercase() in listOf("jpg", "jpeg", "png")) {
                Image(
                    painter = rememberAsyncImagePainter(file),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            } else if (file.extension.lowercase() in listOf("mp4", "3gp", "mkv")) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(file),
                        contentDescription = null,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}


@Composable
fun PermissionHandler(
    permission: String,
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(permission)
    }
}

fun fetchStatuses(context: Context, isVideo: Boolean): List<File> {
    val statuses = mutableListOf<File>()
    val statusFolder = File(
        Environment.getExternalStorageDirectory().toString() +
                "/WhatsApp/Media/.Statuses"
    )

    if (statusFolder.exists()) {
        val files = statusFolder.listFiles()
        files?.forEach { file ->
            if (isVideo && file.name.endsWith(".mp4")) {
                statuses.add(file)
            } else if (!isVideo && (file.name.endsWith(".jpg") || file.name.endsWith(".png"))) {
                statuses.add(file)
            }
        }
    }
    return statuses.sortedByDescending { it.lastModified() }
}

@Preview
@Composable
fun PreviewMyStat() {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Preview placeholder…")
    }
}

@Composable
fun MainScreen(onPickFolderClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onPickFolderClick) {
            Text("Pick WhatsApp Folder")
        }
        Spacer(Modifier.height(16.dp))
        Text("Your app UI here...")
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Normal", "normal"),
        BottomNavItem("Business", "business")
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                label = { Text(item.label) },
                icon = { Icon(Icons.Default.Home, contentDescription = null) }
            )
        }
    }
}

data class BottomNavItem(val label: String, val route: String)
/*

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheStatusSaverTheme {
        Greeting("Android")
    }
}*/
