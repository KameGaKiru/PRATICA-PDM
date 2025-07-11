package com.example.weatherapp

import android.R.attr.name
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.db.fb.FBDatabase
import com.example.weatherapp.db.fb.toFBUser
import com.example.weatherapp.model.User
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterPage(modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@SuppressLint("ContextCastToActivity")
@Preview(showBackground = true)
@Composable
fun RegisterPage( modifier: Modifier = Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var userName by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordConfirm by rememberSaveable { mutableStateOf("") }
    val activity = LocalContext.current as? Activity
    val db = com.google.firebase.Firebase.firestore
    val userId = Firebase.auth.currentUser?.uid


    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
    ) {
        Text(
            text = "Tela de Registro!",
            fontSize = 24.sp
        )

        OutlinedTextField(
            value = userName,
            label = { Text(text = "Digite seu Nome") },
            modifier = modifier.fillMaxWidth(fraction = 0.9f),
            onValueChange = { userName = it }
        )

        OutlinedTextField(
            value = email,
            label = { Text(text = "Digite seu e-mail") },
            modifier = modifier.fillMaxWidth(fraction = 0.9f),
            onValueChange = { email = it }
        )

        OutlinedTextField(
            value = password,
            label = { Text(text = "Digite sua senha!") },
            modifier = modifier.fillMaxWidth(fraction = 0.9f),
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedTextField(
            value = passwordConfirm,
            label = { Text(text = "Confirme sua senha!") },
            modifier = modifier.fillMaxWidth(fraction = 0.9f),
            onValueChange = { passwordConfirm = it },
            visualTransformation = PasswordVisualTransformation()
        )

        Row(modifier = modifier) {
            Button(
                onClick = {
                    Firebase.auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity!!) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    activity,
                                    "Registro OK!", Toast.LENGTH_LONG
                                ).show()

                                FBDatabase.register(User(userName, email).toFBUser())

                            } else {
                                Toast.makeText(
                                    activity,
                                    "Registro FALHOU!", Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                },
                enabled = userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()
            ) {
                Text("Registrar")
            }

            Button(
                onClick = { userName = ""; email = ""; password = ""; passwordConfirm = "" }
            ) {
                Text("Limpar")
            }
        }
    }
}

