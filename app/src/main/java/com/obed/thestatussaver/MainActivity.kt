package com.obed.thestatussaver

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.obed.thestatussaver.screens.business.BusinessScreen
import com.obed.thestatussaver.screens.normal.NormalScreen
import com.obed.thestatussaver.ui.theme.TheStatusSaverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheStatusSaverTheme {

                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                    MainScreen()
                }
            }
        }
    }
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
        MainScreen()
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "normal",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("normal") { NormalScreen() }
            composable("business") { BusinessScreen() }
        }
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
