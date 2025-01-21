package com.unibo.petly.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.unibo.petly.LoginActivity
import com.unibo.petly.MainActivity
import com.unibo.petly.RegistrationActivity
import com.unibo.petly.data.database.User
import com.unibo.petly.ui.composables.TextField
import com.unibo.petly.ui.composables.PasswordField
import com.unibo.petly.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

sealed class FormScreen(
    val title: String,
    val buttonText: String,
    val submit: (
        username: String,
        password: String,
        vm: UserViewModel,
        scope: CoroutineScope,
        sharedPreferences: SharedPreferences,
        snackbarHostState: SnackbarHostState,
        context: Context) -> Unit,
    val switchText: String,
    val switchButtonText: String,
    val onSwitch: (context: Context) -> Unit
) {
    data object Login: FormScreen(
        title ="rootly",
        buttonText = "Login",
        submit = { username, password, vm, scope, pref, snackbar, context ->
            scope.launch {
                checkLoginCredentials(username, password, vm, pref, snackbar, context)
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
        submit = { username, password, vm, scope, pref, snackbar, context ->
            scope.launch {
                checkRegistrationCredentials(username, password, vm, pref, snackbar, context)
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
    val vm = koinViewModel<UserViewModel>()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            var username by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }

            Text(
                text = screen.title,
                style = MaterialTheme.typography.displayMedium.copy(
                    textAlign = TextAlign.Center // Mantieni il solo textAlign personalizzato
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
                onClick = {
                    screen.submit(
                        username,
                        password,
                        vm,
                        scope,
                        sharedPreferences,
                        snackbarHostState,
                        context
                    )
                },
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
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { screen.onSwitch(context) }
                )
            }
        }
    }
}

suspend fun checkLoginCredentials(
    username: String,
    password: String,
    vm: UserViewModel,
    sharedPreferences: SharedPreferences,
    snackbarHostState: SnackbarHostState,
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
        snackbarHostState.showSnackbar(
            message = "Wrong credentials",
            duration = SnackbarDuration.Short
        )
    }
}

suspend fun checkRegistrationCredentials(
    username: String,
    password: String,
    vm: UserViewModel,
    sharedPreferences: SharedPreferences,
    snackbarHostState: SnackbarHostState,
    context: Context
) {
    if (username.isNotBlank() && username.isNotEmpty() &&
        password.isNotBlank() && password.isNotEmpty()) {
        val userId = vm.register(User(username = username, password = password)).toInt()
        if (userId > 0) {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putInt("userId", userId)
            editor.apply()
            context.startActivity(Intent(context, MainActivity::class.java))
            (context as Activity).finish()
        }
    } else {
        snackbarHostState.showSnackbar(
            message = "All fields are required",
            duration = SnackbarDuration.Short
        )
    }
}
