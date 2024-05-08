package com.unibo.rootly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.RootlyRoute

@Composable
fun LoginScreen(navController: NavHostController) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(45.dp)
    ) {
        var email by rememberSaveable { mutableStateOf("") }
        var pw by rememberSaveable { mutableStateOf("") }

        Text(
            text = RootlyRoute.Login.title,
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = pw,
            onValueChange = { pw = it },
            label = { Text("Password") },
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate(RootlyRoute.Home.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Row(modifier = Modifier.padding(top = 10.dp)) {
            Text(text = "Aren't you signed in? ")
            Text(
                text = "Create an account",
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable { navController.navigate(RootlyRoute.Login.route) }
            )
        }
    }
}