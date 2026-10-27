package com.example.autocare.ui.screens.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.autocare.data.EstadoMantenimiento
import com.example.autocare.data.Mantenimiento
import com.example.autocare.data.ProximoMantenimiento
import com.example.autocare.data.Vehiculo
import com.example.autocare.data.calcularProximo
import com.example.autocare.ui.components.AutoCareScaffold
import com.example.autocare.ui.components.AutoCareTab
import com.example.autocare.ui.components.MantenimientoFormSheet
import com.example.autocare.ui.components.miles
import com.example.autocare.ui.components.navigateToTab
import com.example.autocare.ui.components.pesos
import com.example.autocare.viewmodel.MantenimientosViewModel
import com.example.autocare.viewmodel.VehiculoViewModel
import com.example.autocare.ui.components.KilometrajeDialog

@Composable
fun InicioScreen(
    navController: NavHostController,
    onLogout: () -> Unit,
    vehiculoVm: VehiculoViewModel = viewModel(),
    mantenimientosVm: MantenimientosViewModel = viewModel()
) {
    val vehiculoState by vehiculoVm.uiState.collectAsState()
    val mantState by mantenimientosVm.uiState.collectAsState()
    val errorVehiculo by vehiculoVm.error.collectAsState()
    val errorMant by mantenimientosVm.error.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(errorVehiculo) {
        errorVehiculo?.let {
            snackbarHostState.showSnackbar(it)
            vehiculoVm.limpiarError()
        }
    }
    LaunchedEffect(errorMant) {
        errorMant?.let {
            snackbarHostState.showSnackbar(it)
            mantenimientosVm.limpiarError()
        }
    }

    var mostrarForm by remember { mutableStateOf(false) }
    var actualizandoKm by remember { mutableStateOf(false) }

    val vehiculo = vehiculoState.vehiculo
    val kmActual = vehiculo?.kilometraje ?: 0
    val proximo = remember(mantState.lista, kmActual) {
        calcularProximo(mantState.lista, kmActual)
    }

    AutoCareScaffold(
        title = "AutoCare",
        subtitle = "Control de mantenimiento",
        currentTab = AutoCareTab.INICIO,
        onTabSelected = { tab -> navigateToTab(navController, tab) },
        showFab = true,
        onFabClick = {
            // Sin vehículo no se puede registrar un mantenimiento
            if (vehiculo == null) navigateToTab(navController, AutoCareTab.VEHICULO)
            else mostrarForm = true
        },
        onLogout = onLogout,
        snackbarHostState = snackbarHostState
    ) { padding ->
        if (vehiculoState.cargando || mantState.cargando) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (vehiculo == null) {
                    SinVehiculoCard(
                        onRegistrar = { navigateToTab(navController, AutoCareTab.VEHICULO) }
                    )
                } else {
                    VehiculoResumenCard(vehiculo, onActualizarKm = { actualizandoKm = true })
                }

                ProximoMantenimientoCard(proximo = proximo, kmActual = kmActual)

                UltimoMantenimientoCard(
                    ultimo = mantState.ultimo,
                    onVerHistorial = { navigateToTab(navController, AutoCareTab.MANTENIMIENTOS) }
                )

                TotalGastadoCard(
                    total = mantState.totalGastado,
                    onClick = { navigateToTab(navController, AutoCareTab.MANTENIMIENTOS) }
                )

                Spacer(Modifier.height(72.dp)) // espacio para que el botón Añadir no tape nada
            }
        }
    }

    if (mostrarForm) {
        MantenimientoFormSheet(
            inicial = null,
            kmActual = kmActual,
            onGuardar = { m ->
                mantenimientosVm.crear(m)
                if (m.kilometraje > kmActual) vehiculoVm.actualizarKilometraje(m.kilometraje)
                mostrarForm = false
            },
            onCerrar = { mostrarForm = false }
        )
    }

    if (actualizandoKm && vehiculo != null) {
        KilometrajeDialog(
            kmActual = vehiculo.kilometraje,
            onGuardar = {
                vehiculoVm.actualizarKilometraje(it)
                actualizandoKm = false
            },
            onCerrar = { actualizandoKm = false }
        )
    }

}

@Composable
private fun SinVehiculoCard(onRegistrar: () -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Aún no registras tu vehículo", style = MaterialTheme.typography.titleMedium)
            Text(
                "Registra tu vehículo para empezar a llevar el control de sus mantenimientos.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = onRegistrar) { Text("Registrar vehículo") }
        }
    }
}

@Composable
private fun VehiculoResumenCard(vehiculo: Vehiculo, onActualizarKm: () -> Unit) {
    ElevatedCard(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.DirectionsCar,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column {
                Text(
                    "${vehiculo.marca} ${vehiculo.modelo} ${vehiculo.anio}",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(4.dp))
                AssistChip(onClick = {}, label = { Text("Placa: ${vehiculo.placa}") })
                Spacer(Modifier.height(8.dp))
                Text(
                    "KILOMETRAJE ACTUAL",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        vehiculo.kilometraje.miles(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("km", style = MaterialTheme.typography.titleMedium)
                }
                TextButton(onClick = onActualizarKm, contentPadding = PaddingValues(0.dp)) {
                    Icon(Icons.Filled.Update, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Actualizar kilometraje")
                }
            }
        }
    }
}

@Composable
private fun ProximoMantenimientoCard(proximo: ProximoMantenimiento?, kmActual: Int) {
    val pendiente = proximo?.estado == EstadoMantenimiento.PENDIENTE
    val contenedor = if (pendiente) MaterialTheme.colorScheme.errorContainer
    else MaterialTheme.colorScheme.tertiaryContainer
    val contenido = if (pendiente) MaterialTheme.colorScheme.onErrorContainer
    else MaterialTheme.colorScheme.onTertiaryContainer

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = contenedor, contentColor = contenido)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Build, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Próximo mantenimiento", style = MaterialTheme.typography.titleMedium)
            }
            if (proximo == null) {
                Text(
                    "No tienes un próximo mantenimiento programado. Registra un mantenimiento " +
                            "con su próximo kilometraje para recibir el aviso.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                val m = proximo.mantenimiento
                Text(
                    "${m.emoji} ${m.tipo}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    if (pendiente) "🔴 Mantenimiento pendiente"
                    else "🟢 Faltan ${proximo.faltanKm.miles()} km",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "Recomendado a los ${m.proximoKilometraje.miles()} km",
                    style = MaterialTheme.typography.bodyMedium
                )
                val recorrido = (m.proximoKilometraje - m.kilometraje).coerceAtLeast(1)
                val progreso = ((kmActual - m.kilometraje).toFloat() / recorrido).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier.fillMaxWidth(),
                    color = contenido,
                    trackColor = contenido.copy(alpha = 0.25f)
                )
            }
        }
    }
}

@Composable
private fun UltimoMantenimientoCard(ultimo: Mantenimiento?, onVerHistorial: () -> Unit) {
    OutlinedCard(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.History,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Último mantenimiento", style = MaterialTheme.typography.titleMedium)
                }
                TextButton(onClick = onVerHistorial) { Text("Ver historial") }
            }
            if (ultimo == null) {
                Text(
                    "Aún no has registrado mantenimientos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "${ultimo.emoji} ${ultimo.tipo}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            ultimo.fecha,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(ultimo.costo.pesos(), style = MaterialTheme.typography.titleMedium)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Speed, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Registrado a los ${ultimo.kilometraje.miles()} km",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun TotalGastadoCard(total: Int, onClick: () -> Unit) {
    Card(onClick = onClick, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.AccountBalanceWallet, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("TOTAL GASTADO", style = MaterialTheme.typography.labelSmall)
                    Text(total.pesos(), style = MaterialTheme.typography.titleLarge)
                }
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null)
        }
    }
}