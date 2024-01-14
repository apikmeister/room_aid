package com.example.room_aid

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ChoresScreen(navController: NavController, dbHelper: DBHelper) {
    val tasks = remember { mutableStateOf(listOf<Task>()) }

    val context = LocalContext.current

    // Retrieve userId from shared preferences
    val sharedPref = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    val userId = sharedPref.getInt("userId", -1) // -1 as default if not found

    // Retrieve tasks from the database
    LaunchedEffect(Unit) {
        if (userId != -1) {
            tasks.value = dbHelper.getAllTasksById(userId)
        }
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }

    if (showDeleteDialog && taskToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        taskToDelete?.let { task ->
                            dbHelper.deleteTask((task.id).toString()) // Delete task by ID
                            tasks.value = dbHelper.getAllTasksById(userId) // Refresh the task list
                        }
                        showDeleteDialog = false
                        taskToDelete = null
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Chores",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Text(
                    "dd.MM.yyyy",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (tasks.value.isEmpty()) {
                    // Display a message or a custom UI element for empty tasks
                    Text(
                        "No chores found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                } else {
                    // Display tasks from the database
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
//                            tasks.value.forEach { taskName ->
//                                ChoreItem(taskName) {
//                                    // Call the function to delete the task
//                                    dbHelper.deleteTask(taskName)
//                                    // Update the task list
//                                    tasks.value = dbHelper.getAllTasksById(userId)
//                                }
//                            }
                            tasks.value.forEach { task ->
                                ChoreItem(
                                    text = task.name,
                                    checked = task.completed,
                                    onCheckedChange = { isChecked ->
                                        // Update task completion in the database
                                        dbHelper.updateTaskCompletion(task.id, isChecked)
                                    },
                                    onDeleteClick = {
                                        taskToDelete = task // Set the entire task object for deletion
                                        showDeleteDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* TODO: Implement history action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "History",
                        tint = Color.White
                    )
                }
            }
            FloatingActionButton(
                onClick = { navController.navigate("addTask") },
//            backgroundColor = Color.Blue,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Chore"
                )
            }
        }

    }
}

@Composable
fun ChoreItem(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, onDeleteClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkmarkColor = Color.White)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            textDecoration = if (checked) TextDecoration.LineThrough else null
        )
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red
            )
        }
    }
}
