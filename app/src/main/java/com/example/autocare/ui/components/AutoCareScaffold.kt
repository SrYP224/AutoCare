package com.example.autocare.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

enum class AutoCareTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    INICIO("Inicio", Icons.Filled.Home),
    VEHICULO("Mi vehículo", Icons.Filled.DirectionsCar),
    MANTENIMIENTOS("Mantenimientos", Icons.Filled.Build)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCareScaffold(
    title: String,
    subtitle: String? = null,
    currentTab: AutoCareTab,
    onTabSelected: (AutoCareTab) -> Unit,
    showFab: Boolean = false,
    fabLabel: String = "Añadir",
    onFabClick: () -> Unit = {},
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    androidx.compose.foundation.layout.Column {
                        Text(title, style = MaterialTheme.typography.titleLarge, color = Color(0xFF18317E))
                        subtitle?.let {
                            Text(it, style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {}) { Icon(Icons.Filled.Menu, contentDescription = "Menú") }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Filled.AccountCircle, contentDescription = "Cuenta") }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                AutoCareTab.values().forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = { onTabSelected(tab) },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        },
        floatingActionButton = {
            if (showFab) {
                ExtendedFloatingActionButton(
                    onClick = onFabClick,
                    icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                    text = { Text(fabLabel) }
                )
            }
        }
    ) { padding -> content(padding) }
}

fun navigateToTab(navController: NavController, tab: AutoCareTab) {
    val route = when (tab) {
        AutoCareTab.INICIO -> "inicio"
        AutoCareTab.VEHICULO -> "vehiculo"
        AutoCareTab.MANTENIMIENTOS -> "mantenimientos"
    }
    navController.navigate(route) {
        popUpTo("inicio")
        launchSingleTop = true
    }
}