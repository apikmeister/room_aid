package com.example.room_aid

import android.content.Context
import android.widget.Toast
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(navController: NavController, dbHelper: DBHelper) {
    var taskName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }

    var taskNameError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var categoryError by remember { mutableStateOf(false) }
    var dueDateError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Retrieve userId from shared preferences
    val sharedPref = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    val userId = sharedPref.getInt("userId", -1) // -1 as default if not found

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
            Text(
                "Task",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = taskName,
                isError = taskNameError,
                onValueChange = { taskName = it },
                label = { Text("Task Name", color = if (taskNameError) Color.Red else Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
//                                        textColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                isError = descriptionError,
                onValueChange = { description = it },
                label = { Text("Description", color = if (descriptionError) Color.Red else Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
//                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = category,
                isError = categoryError,
                onValueChange = { category = it },
                label = { Text("Category", color = if (categoryError) Color.Red else Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
//                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = dueDate,
                isError = dueDateError,
                onValueChange = { dueDate = it },
                label = { Text("Due Date", color = if (dueDateError) Color.Red else Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
//                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
//                keyboardActions = KeyboardActions(onDone = { /* TODO: Implement action on done */ })
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Reset validation errors
                    taskNameError = false
                    descriptionError = false
                    categoryError = false
                    dueDateError = false

                    // Validation checks
                    if (taskName.isBlank()) taskNameError = true
                    if (description.isBlank()) descriptionError = true
                    if (category.isBlank()) categoryError = true
                    if (dueDate.isBlank()) dueDateError = true
                    if (!taskNameError && !descriptionError && !categoryError && !dueDateError) {
                        if (userId != -1) {
                            val isAdded =
                                dbHelper.addTask(userId, taskName, description, category, dueDate)
                            if (isAdded) {
                                // Handle successful task addition, e.g., show confirmation message
                                // TODO:
                                navController.navigate("viewTask") {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            } else {
                                // Handle failure in adding task, e.g., show error message
                                //TODO:
                                Toast.makeText(
                                    context,
                                    "Add failed!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Please fill in all required fields",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Save", color = Color.White)
            }
        }
    }
}
