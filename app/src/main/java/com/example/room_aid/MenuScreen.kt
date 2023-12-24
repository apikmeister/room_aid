package com.example.room_aid


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MenuScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Hi Guest 1!",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(32.dp))
            MenuButton("Chores")
            Spacer(modifier = Modifier.height(8.dp))
            MenuButton("Expenses")
            Spacer(modifier = Modifier.height(8.dp))
            MenuButton("Grocery")
            Spacer(modifier = Modifier.height(8.dp))
            MenuButton("Payment")
        }
    }
}

@Composable
fun MenuButton(text: String) {
    Button(
        onClick = { /* TODO: Implement onClick action */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(8.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp, brush = SolidColor(Color.White)),
        modifier = Modifier.fillMaxWidth().height(50.dp)
    ) {
        Text(
            text,
            color = Color.White
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun MenuScreenPreview() {
    MenuScreen()
}