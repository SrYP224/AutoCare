package com.example.autocare.ui.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autocare.R
import androidx.compose.foundation.layout.width

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {

    // Estados de los campos
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    // Colores de la interfaz
    val primaryColor = Color(0xFF18317E)
    val backgroundColor = Color(0xFFF8FAFF)
    val textColor = Color(0xFF1C1C1C)
    val borderColor = Color(0xFFD0D5DD)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 28.dp,
                    vertical = 40.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            /*
             * LOGO AUTOCARE
             *
             * res/drawable/autocare_logo.png
             */
            Image(
                painter = painterResource(
                    id = R.drawable.autocare_logo
                ),
                contentDescription = "Logo de AutoCare",
                modifier = Modifier
                    .size(130.dp)
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // Título
            Text(
                text = "Bienvenido a AutoCare",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // Subtítulo
            Text(
                text = "Inicia sesión para continuar",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            // Campo de correo
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                label = {
                    Text("Correo electrónico")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Correo electrónico"
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = borderColor,
                    focusedLabelColor = primaryColor,
                    cursorColor = primaryColor
                )
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // Campo de contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                label = {
                    Text("Contraseña")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Contraseña"
                    )
                },
                trailingIcon = {

                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {

                        Icon(
                            imageVector = if (passwordVisible) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = if (passwordVisible) {
                                "Ocultar contraseña"
                            } else {
                                "Mostrar contraseña"
                            }
                        )
                    }
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = borderColor,
                    focusedLabelColor = primaryColor,
                    cursorColor = primaryColor
                )
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // Recuperar contraseña
            Text(
                text = "¿Olvidaste tu contraseña?",
                modifier = Modifier
                    .fillMaxWidth(),
                color = primaryColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // Botón iniciar sesión
            Button(
                onClick = {

                    /*
                     * Aquí posteriormente podemos colocar
                     * la autenticación contra el backend.
                     */
                    if (
                        email.isNotBlank() &&
                        password.isNotBlank()
                    ) {
                        onLoginSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor
                )
            ) {

                Text(
                    text = "Iniciar sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // Botón continuar con Google
            OutlinedButton(
                onClick = {
                    // Pendiente de implementar autenticación con Google
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = borderColor
                )
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.google_logo
                    ),
                    contentDescription = "Google",
                    modifier = Modifier.size(22.dp)
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = "Continuar con Google",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // Botón continuar con Apple
            OutlinedButton(
                onClick = {
                    // Pendiente de implementar autenticación con Apple
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = borderColor
                )
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.apple_logo
                    ),
                    contentDescription = "Apple",
                    modifier = Modifier.size(22.dp)
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = "Continuar con Apple",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // Texto inferior
            Text(
                text = "¿No tienes una cuenta?",
                fontSize = 14.sp,
                color = textColor
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Regístrate",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onLoginSuccess = {}
    )
}
