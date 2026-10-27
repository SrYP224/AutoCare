package com.example.autocare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autocare.data.AuthRepository
import com.example.autocare.ui.screens.inicio.InicioScreen
import com.example.autocare.ui.screens.login.LoginScreen
import com.example.autocare.ui.screens.mantenimientos.MantenimientosScreen
import com.example.autocare.ui.screens.vehiculo.MiVehiculoScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AutoCareNavGraph(navController: NavHostController = rememberNavController()) {
    val destinoInicial = remember {
        if (FirebaseAuth.getInstance().currentUser != null) "inicio" else "login"
    }

    val cerrarSesion: () -> Unit = {
        AuthRepository().logout()
        navController.navigate("login") {
            popUpTo("inicio") { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = destinoInicial) {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("inicio") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("inicio") { InicioScreen(navController, cerrarSesion) }
        composable("vehiculo") { MiVehiculoScreen(navController, cerrarSesion) }
        composable("mantenimientos") { MantenimientosScreen(navController, cerrarSesion) }
    }
}