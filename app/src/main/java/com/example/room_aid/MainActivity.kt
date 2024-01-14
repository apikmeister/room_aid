package com.example.room_aid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbHelper = DBHelper(this)
        setContent {
            AppNavGraph(dbHelper)
        }
    }
}

@Composable
fun AppNavGraph(dbHelper: DBHelper) {
    val navController = rememberNavController();
    NavHost(navController = navController, startDestination = "register") {
        composable("register") { RegisterScreen(navController, dbHelper) }
        composable("login") { LoginScreen(navController, dbHelper) }
        composable("menuScreen") { MenuScreen(navController) }
        composable("addGrocery") { GroceryEntryScreen(navController, dbHelper) }
        composable("viewGrocery") { GroceryScreen(navController, dbHelper) }
        composable("addTask") { TaskScreen(navController, dbHelper) }
        composable("viewTask") { ChoresScreen(navController, dbHelper) }
    }
}