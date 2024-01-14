package com.example.room_aid

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

@Composable
fun GroceryEntryScreen(navController: NavController, dbHelper: DBHelper) {

    var groceryName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    val context = LocalContext.current

    var groceryNameError by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xCC78C1FC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Grocery",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = groceryName,
                isError = groceryNameError,
                onValueChange = { groceryName = it },
                label = { Text("Grocery Name", color = if (groceryNameError) Color.Red else Color.Gray) },
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
                value = amount,
                isError = amountError,
                onValueChange = { amount = it },
                label = { Text("Amount", color = if (amountError) Color.Red else Color.Gray) },
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
                value = price,
                isError = priceError,
                onValueChange = { price = it },
                label = { Text("Price", color = if (priceError) Color.Red else Color.Gray) },
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
                    groceryNameError = false
                    amountError = false
                    priceError = false

                    // Validation checks
                    if (groceryName.isBlank()) groceryNameError = true
                    if (amount.isBlank()) amountError = true
                    if (price.isBlank()) priceError = true
                    if (!groceryNameError && !amountError && !priceError) {
                        val isSuccess = dbHelper.addGrocery(groceryName, amount, price)
                        if (isSuccess) {
                            Toast.makeText(
                                context,
                                "Grocery Added!",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate("viewGrocery") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Grocery Add Failed!",
                                Toast.LENGTH_SHORT
                            ).show()
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
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Save", color = Color.White)
            }
        }
    }
}
