package com.example.room_aid

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun GroceryScreen(navController: NavController, dbHelper: DBHelper) {
    val groceries = remember { mutableStateOf(listOf<Grocery>()) }
    val context = LocalContext.current

    // Retrieve userId from shared preferences
    val sharedPref = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    val userId = sharedPref.getInt("userId", -1) // -1 as default if not found

    LaunchedEffect(Unit) {
        if (userId != -1) {
            groceries.value = dbHelper.getAllGroceriesById(userId)
        }
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var groceryToDelete by remember { mutableStateOf<Grocery?>(null) }

    if (showDeleteDialog && groceryToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Grocery") },
            text = { Text("Are you sure you want to delete this Grocery?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        groceryToDelete?.let { grocery ->
                            dbHelper.deleteGrocery((grocery.id).toString())
                            groceries.value = dbHelper.getAllGroceriesById(userId)
                        }
                        showDeleteDialog = false
                        groceryToDelete = null
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
        color = Color(0xCC78C1FC)
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Grocery",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF000000)
                )
                // Removed grocery list preview
                Spacer(modifier = Modifier.height(16.dp))
                if (groceries.value.isEmpty()) {
                    Text("No groceries found", color = Color.White)
                } else {
                    GroceryList(groceries.value, dbHelper) { grocery ->
                        groceryToDelete = grocery
                        showDeleteDialog = true
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* TODO: Implement pay action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF013A68)),
                    modifier = Modifier
                        .width(300.dp)
                        .height(56.dp)
                ) {
                    Text(
                        text = "Pay Here",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            FloatingActionButton(
                onClick = { navController.navigate("addGrocery") },
//                backgroundColor = Color.Blue,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                // Wrap the FloatingActionButton in a Surface to set the background color
                Surface(
                    color = Color(0xFF013A68), // Set your custom background color here
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = Color.White,
                        contentDescription = "Add Item"
                    )
                }
            }
        }
    }
}


@Composable
fun GroceryList(items: List<Grocery>, dbHelper: DBHelper, onDeleteClick: (Grocery) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xE61B4C74)),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
//            items.forEach { item ->
//                GroceryItem(item)
//            }
            items.forEach { grocery ->
                GroceryItem(
                    text = grocery.name,
                    checked = grocery.completed,
                    onCheckedChange = { isChecked ->
                        dbHelper.updateGroceriesCompletion(grocery.id, isChecked)
                    },
                    onDeleteClick = {
                        onDeleteClick(grocery)
                    }
                )
            }
        }
    }
}

@Composable
fun GroceryItem(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, onDeleteClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFFFFFFFF),
            modifier = Modifier.weight(1f)
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkmarkColor = Color.White)
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