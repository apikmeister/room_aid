package com.example.room_aid

<<<<<<< HEAD
=======
import android.widget.Toast
>>>>>>> b11428c (Update screen to use SQLiteDB)
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController, dbHelper: DBHelper) {
    var registerUsername by remember { mutableStateOf("") }
    var registerPassword by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Registration Section
            Text(
                "Register",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = registerUsername,
                onValueChange = { registerUsername = it },
                label = { Text("New Username", color = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                isError = usernameError
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = registerPassword,
                onValueChange = { registerPassword = it },
                label = { Text("New Password", color = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = passwordError
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    imeAction = ImeAction.Done
//                ),
//                keyboardActions = KeyboardActions(onDone = {
//                    // Perform registration action
//
//                })
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Perform registration action
                    usernameError = false
                    passwordError = false

                    // Check for validation
                    if (registerUsername.isBlank()) {
                        usernameError = true
                    }
                    if (registerPassword.isBlank()) {
                        passwordError = true
                    }
                    if (!usernameError && !passwordError) {
                        val isUserAdded = dbHelper.addUser(registerUsername, registerPassword)
                        if (isUserAdded) {
                            navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Registration failed, please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Please fill in all forms.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Register", color = Color.White)
            }
        }
    }
}
