package com.example.room_aid

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun UserProfileScreen() {
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
            UserIconSection()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "User Name",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Housemate",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            HousematesSection(housemates = listOf("Name", "Name", "Name", "Name"))
        }
    }
}

@Composable
fun UserIconSection() {
    Icon(
        imageVector = Icons.Default.Person,
        contentDescription = "User Profile",
        tint = Color.Blue,
        modifier = Modifier.size(80.dp)
    )
}

@Composable
fun HousematesSection(housemates: List<String>) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        for (name in housemates) {
            HousemateIcon(name = name)
        }
    }
}

@Composable
fun HousemateIcon(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Housemate",
            tint = Color.Blue,
            modifier = Modifier.size(56.dp)
        )
        Text(
            text = name,
            color = Color.White
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun UserProfileScreenPreview() {
    UserProfileScreen()
}