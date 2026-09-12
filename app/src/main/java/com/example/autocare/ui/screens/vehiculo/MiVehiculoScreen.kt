package com.example.autocare.ui.screens.vehiculo


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.example.autocare.ui.components.AutoCareScaffold
import com.example.autocare.ui.components.AutoCareTab
import com.example.autocare.ui.components.navigateToTab
import androidx.compose.ui.Modifier


@Composable
fun MiVehiculoScreen(navController: NavHostController) {
    AutoCareScaffold(
        title = "AutoCare",
        currentTab = AutoCareTab.VEHICULO,
        onTabSelected = { tab -> navigateToTab(navController, tab) }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("TODO: Mi vehículo") // reemplazar con la pantalla real
        }
    }
}