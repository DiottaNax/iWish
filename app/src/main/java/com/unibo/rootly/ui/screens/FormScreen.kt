package com.unibo.rootly.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unibo.rootly.LoginActivity
import com.unibo.rootly.MainActivity
import com.unibo.rootly.RegistrationActivity
import com.unibo.rootly.data.database.User
import com.unibo.rootly.ui.composables.TextField
import com.unibo.rootly.ui.composables.PasswordField
import com.unibo.rootly.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class FormScreen(
    val title: String,
    val buttonText: String,
    val submit: (
        username: String,
        password: String,
        vm: UserViewModel,
        scope: CoroutineScope,
        sharedPreferences: SharedPreferences,
        context: Context) -> Unit,
    val switchText: String,
    val switchButtonText: String,
    val onSwitch: (context: Context) -> Unit
) {
    data object Login: FormScreen(
        title ="rootly",
        buttonText = "Login",
        submit = { username, password, vm, scope, sharedPreferences, context ->
            scope.launch {
                checkLoginCredentials(username, password, vm, sharedPreferences, context)
            }
        },
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
        submit = { username, password, vm, scope, sharedPreferences, context ->
            scope.launch {
                checkRegistrationCredentials(username, password, vm, sharedPreferences, context)
            }
        },
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
    sharedPreferences: SharedPreferences,
    context: Context
) {
    val vm = hiltViewModel<UserViewModel>()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(innerPadding)
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
                TextField(
                    value = username,
                    onChange = { username = it },
                    label = "Username",
                    modifier = Modifier.fillMaxWidth()
                )

                PasswordField(
                    value = password,
                    onChange = { password = it },
                    submit = { screen.submit },
                    label = "Password",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = { screen.submit(username, password, vm, scope, sharedPreferences, context) },
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
}

fun checkLoginCredentials(
    username: String,
    password: String,
    vm: UserViewModel,
    sharedPreferences: SharedPreferences,
    context: Context
) {
    val userId = vm.login(username, password)
    if (userId > 0) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("userId", userId)
        editor.apply()
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as Activity).finish()
    } else {
        Toast.makeText(context, "Wrong Credentials", Toast.LENGTH_SHORT).show()
    }
}

suspend fun checkRegistrationCredentials(
    username: String,
    password: String,
    vm: UserViewModel,
    sharedPreferences: SharedPreferences,
    context: Context
) {
    val userId = vm.register(User( username = username, password = password )).toInt()
    if (userId > 0) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("userId", userId)
        editor.apply()
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as Activity).finish()
    } else {
        Toast.makeText(context, "Wrong Credentials", Toast.LENGTH_SHORT).show()
    }
}
