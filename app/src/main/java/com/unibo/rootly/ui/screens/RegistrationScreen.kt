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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.RootlyRoute

@Composable
fun RegistrationScreen(navController: NavHostController) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(45.dp)
    ) {
        var name by rememberSaveable { mutableStateOf("") }
        var email by rememberSaveable { mutableStateOf("") }
        var pw by rememberSaveable { mutableStateOf("") }

        Text(
            text = RootlyRoute.Registration.title,
            style = MaterialTheme.typography.displayMedium.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            label = {
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    text = "Email",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = pw,
            onValueChange = { pw = it },
            label = {
                Text(
                    text = "Password",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate(RootlyRoute.Home.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Sign up",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(modifier = Modifier.padding(top = 10.dp)) {
            Text(
                text = "Already have an account? ",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { navController.navigate(RootlyRoute.Login.route) }
            )
        }
    }
}