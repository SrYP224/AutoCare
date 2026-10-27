package com.example.autocare.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

enum class AutoCareTab(val label: String, val icon: ImageVector) {
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
    onLogout: (() -> Unit)? = null,
    snackbarHostState: SnackbarHostState? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    var menuAbierto by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val correo = FirebaseAuth.getInstance().currentUser?.email

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(Modifier.padding(horizontal = 28.dp, vertical = 24.dp)) {
                    Text(
                        "AutoCare",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Control de mantenimiento",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (correo != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(correo, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                HorizontalDivider()
                Spacer(Modifier.height(12.dp))
                AutoCareTab.entries.forEach { tab ->
                    NavigationDrawerItem(
                        label = { Text(tab.label) },
                        icon = { Icon(tab.icon, contentDescription = null) },
                        selected = tab == currentTab,
                        onClick = {
                            scope.launch { drawerState.close() }
                            onTabSelected(tab)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
                if (onLogout != null) {
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(12.dp))
                    NavigationDrawerItem(
                        label = { Text("Cerrar sesión") },
                        icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            onLogout()
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(title, style = MaterialTheme.typography.titleLarge)
                            if (subtitle != null) {
                                Text(
                                    subtitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        Box {
                            IconButton(onClick = { menuAbierto = true }) {
                                Icon(Icons.Filled.AccountCircle, contentDescription = "Cuenta")
                            }
                            DropdownMenu(
                                expanded = menuAbierto,
                                onDismissRequest = { menuAbierto = false }
                            ) {
                                if (correo != null) {
                                    DropdownMenuItem(
                                        text = { Text(correo) },
                                        onClick = {},
                                        enabled = false
                                    )
                                }
                                if (onLogout != null) {
                                    DropdownMenuItem(
                                        text = { Text("Cerrar sesión") },
                                        onClick = {
                                            menuAbierto = false
                                            onLogout()
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    AutoCareTab.entries.forEach { tab ->
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
            },
            snackbarHost = {
                if (snackbarHostState != null) SnackbarHost(snackbarHostState)
            }
        ) { padding -> content(padding) }
    }
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