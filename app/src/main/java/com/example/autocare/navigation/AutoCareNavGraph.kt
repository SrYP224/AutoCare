package com.example.autocare.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autocare.ui.screens.inicio.InicioScreen
import com.example.autocare.ui.screens.login.LoginScreen
import com.example.autocare.ui.screens.mantenimientos.MantenimientosScreen
import com.example.autocare.ui.screens.vehiculo.MiVehiculoScreen

@Composable
fun AutoCareNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "inicio") { //Aqui cambiar a login o a la pantala que necesiten para probar
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("inicio") { popUpTo("login") { inclusive = true } }
            })
        }
        composable("inicio") { InicioScreen(navController) }
        composable("vehiculo") { MiVehiculoScreen(navController) }
        composable("mantenimientos") { MantenimientosScreen(navController) }
    }
}