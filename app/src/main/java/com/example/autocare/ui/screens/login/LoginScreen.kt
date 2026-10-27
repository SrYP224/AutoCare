package com.example.autocare.ui.screens.login

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autocare.R
import com.example.autocare.viewmodel.AuthViewModel
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var modoRegistro by remember { mutableStateOf(false) }
    var errorLocal by remember { mutableStateOf<String?>(null) }

    val uiState by authViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val mensajeError = errorLocal ?: uiState.error

    val primaryColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 28.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.autocare_logo),
                contentDescription = "Logo de AutoCare",
                modifier = Modifier.size(130.dp)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Bienvenido a AutoCare",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = if (modoRegistro) "Crea tu cuenta para continuar"
                else "Inicia sesión para continuar",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Correo electrónico") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Correo electrónico")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Contraseña")
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff
                            else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Ocultar contraseña"
                            else "Mostrar contraseña"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp)
            )

            if (modoRegistro) {
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = confirmar,
                    onValueChange = { confirmar = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Confirmar contraseña") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Confirmar contraseña")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(12.dp)
                )
            } else {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            errorLocal = null
                            authViewModel.recuperarContrasena(email)
                        },
                    color = primaryColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(16.dp))

            if (mensajeError != null) {
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
            }
            if (uiState.mensaje != null) {
                Text(
                    text = uiState.mensaje ?: "",
                    color = primaryColor,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
            }

            Button(
                onClick = click@{
                    val problema = when {
                        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() ->
                            "Escribe un correo válido"
                        password.length < 6 ->
                            "La contraseña debe tener al menos 6 caracteres"
                        modoRegistro && password != confirmar ->
                            "Las contraseñas no coinciden"
                        else -> null
                    }
                    errorLocal = problema
                    if (problema != null) return@click

                    if (modoRegistro) {
                        authViewModel.registrar(email, password, onLoginSuccess)
                    } else {
                        authViewModel.login(email, password, onLoginSuccess)
                    }
                },
                enabled = !uiState.cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState.cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = if (modoRegistro) "Crear cuenta" else "Iniciar sesión",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            OutlinedButton(
                onClick = {
                    errorLocal = null
                    authViewModel.loginConGoogle(context, onLoginSuccess)
                },
                enabled = !uiState.cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = "Google",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text("Continuar con Google", fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = { errorLocal = "Inicio de sesión con Apple: próximamente" },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.apple_logo),
                    contentDescription = "Apple",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text("Continuar con Apple", fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = if (modoRegistro) "¿Ya tienes una cuenta?" else "¿No tienes una cuenta?",
                fontSize = 14.sp,
                color = textColor
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (modoRegistro) "Inicia sesión" else "Regístrate",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier.clickable {
                    modoRegistro = !modoRegistro
                    confirmar = ""
                    errorLocal = null
                    authViewModel.limpiarError()
                }
            )
        }
    }
}