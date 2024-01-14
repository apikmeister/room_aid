package com.example.room_aid

import android.content.Context
import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController, dbHelper: DBHelper) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginResult by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            isError = usernameError
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Reset validation errors
            usernameError = false
            passwordError = false

            // Check for validation
            if (username.isBlank()) {
                usernameError = true
            }
            if (password.isBlank()) {
                passwordError = true
            }

            if (!usernameError && !passwordError) {
                val isValidUser = dbHelper.validateUser(username, password)
                if (isValidUser) {
                    val userId = dbHelper.getUserIdByUsername(username)
                    if (userId != null) {
                        // User ID is valid, proceed to save in shared preferences
                        val sharedPref =
                            context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putBoolean("isLoggedIn", true)
                            putString("username", username)
                            putInt("userId", userId)
                            apply()
                        }
                        // Navigate to another screen
                        navController.navigate("menuScreen") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    } else {
                        // User ID is null, consider login as failed
                        loginResult = "Login Failed"
                    }
                } else {
                    loginResult = "Login Failed"
                }
            } else {
                loginResult = "Please fill in all fields"
            }
        }) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(loginResult)
    }
}
