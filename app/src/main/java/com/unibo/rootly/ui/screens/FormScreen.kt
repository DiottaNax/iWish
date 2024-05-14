package com.unibo.rootly.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.unibo.rootly.LoginActivity
import com.unibo.rootly.MainActivity
import com.unibo.rootly.RegistrationActivity
import com.unibo.rootly.ui.composables.LoginField
import com.unibo.rootly.ui.composables.PasswordField

sealed class FormScreen(
    val title: String,
    val buttonText: String,
    val submit: (username: String, password: String, context: Context) -> Unit,
    val switchText: String,
    val switchButtonText: String,
    val onSwitch: (context: Context) -> Unit
) {
    data object Login: FormScreen(
        title ="rootly",
        buttonText = "Login",
        submit = { username, password, context -> checkCredentials(username, password, context) },
        switchText = "Aren't you signed yet? ",
        switchButtonText = "Create an account",
        onSwitch = {context ->
            context.startActivity(Intent(context, RegistrationActivity::class.java))
            (context as Activity).finish()
        }
    )
    data object Registration: FormScreen(
        title ="Welcome to the rootly family!",
        buttonText = "Sign up",
        submit = { username, password, context -> /* TODO */ },
        switchText = "Already have an account? ",
        switchButtonText = "Login",
        onSwitch = {context ->
            context.startActivity(Intent(context, LoginActivity::class.java))
            (context as Activity).finish()
        }
    )

}
@Composable
fun FormScreen(
    screen: FormScreen,
    context: Context
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var username by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        Text(
            text = screen.title,
            style = MaterialTheme.typography.displayMedium.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column {
            LoginField(
                value = username,
                onChange = { username = it },
                label = "Username",
                modifier = Modifier.fillMaxWidth()
            )

            PasswordField(
                value = password,
                onChange = { password = it },
                submit = { checkCredentials(username, password, context) },
                label = "Password",
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = { screen.submit(username, password, context) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = screen.buttonText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Row {
            Text(
                text = screen.switchText,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = screen.switchButtonText,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { screen.onSwitch(context) }
            )
        }
    }
}

fun checkCredentials(
    username: String,
    password: String,
    context: Context
) {
    //TODO: check if data is correct from db
    context.startActivity(Intent(context, MainActivity::class.java))
    (context as Activity).finish()
}