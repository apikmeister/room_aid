package com.example.room_aid

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbHelper = DBHelper(this)
        val isUserLoggedIn = checkLoginState()
        setContent {
            AppNavGraph(dbHelper, isUserLoggedIn) { navController ->
                logout(this, navController)
            }
        }
    }

    private fun checkLoginState(): Boolean {
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isLoggedIn", false)
    }

    private fun logout(context: Context, navController: NavController) {
        // Clear the login state
        val sharedPref = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("isLoggedIn")
            remove("username")
            apply()
        }
        // Navigate to the login screen
        navController.navigate("login") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }
}

@Composable
fun AppNavGraph(dbHelper: DBHelper, isUserLoggedIn: Boolean, onLogout: (NavController) -> Unit) {
    val navController = rememberNavController();
    val startDestination = if (isUserLoggedIn) "menuScreen" else "register"

    NavHost(navController = navController, startDestination) {
        composable("register") { RegisterScreen(navController, dbHelper) }
        composable("login") { LoginScreen(navController, dbHelper) }
        composable("menuScreen") { MenuScreen(navController, onLogout = { onLogout(navController) }) }
        composable("addGrocery") { GroceryEntryScreen(navController, dbHelper) }
        composable("viewGrocery") { GroceryScreen(navController, dbHelper) }
        composable("addTask") { TaskScreen(navController, dbHelper) }
        composable("viewTask") { ChoresScreen(navController, dbHelper) }
    }
}