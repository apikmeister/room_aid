package com.example.room_aid

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GroceryScreen() {
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
                GroceryList(listOf("Lorem ipsum","Lorem ipsum","Lorem ipsum","Lorem ipsum"))
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
                onClick = { /* TODO: Implement add item action */ },
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
fun GroceryList(items: List<String>) {
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
            items.forEach { item ->
                GroceryItem(item)
            }
        }
    }
}

@Composable
fun GroceryItem(text: String) {
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
            checked = false,
            onCheckedChange = { /* TODO: Implement check action */ },
            colors = CheckboxDefaults.colors(checkmarkColor = Color.White)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun GroceryScreenPreview() {
    GroceryScreen()
}