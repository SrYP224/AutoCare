package com.example.autocare.ui.screens.mantenimientos

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
fun MantenimientosScreen(navController: NavHostController) {
    AutoCareScaffold(
        title = "AutoCare",
        currentTab = AutoCareTab.MANTENIMIENTOS,
        onTabSelected = { tab -> navigateToTab(navController, tab) },
        showFab = true
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("TODO: Mantenimientos") // reemplazar con la pantalla real
        }
    }
}