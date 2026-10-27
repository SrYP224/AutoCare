package com.example.autocare.ui.screens.mantenimientos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.autocare.data.Mantenimiento
import com.example.autocare.ui.components.AutoCareScaffold
import com.example.autocare.ui.components.AutoCareTab
import com.example.autocare.ui.components.MantenimientoFormSheet
import com.example.autocare.ui.components.miles
import com.example.autocare.ui.components.navigateToTab
import com.example.autocare.ui.components.pesos
import com.example.autocare.viewmodel.MantenimientosViewModel
import com.example.autocare.viewmodel.VehiculoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MantenimientosScreen(
    navController: NavHostController,
    onLogout: () -> Unit,
    mantVm: MantenimientosViewModel = viewModel(),
    vehiculoVm: VehiculoViewModel = viewModel()
) {
    val mantState by mantVm.uiState.collectAsState()
    val vehiculoState by vehiculoVm.uiState.collectAsState()
    val errorMant by mantVm.error.collectAsState()
    val errorVehiculo by vehiculoVm.error.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(errorMant) {
        errorMant?.let {
            snackbarHostState.showSnackbar(it)
            mantVm.limpiarError()
        }
    }
    LaunchedEffect(errorVehiculo) {
        errorVehiculo?.let {
            snackbarHostState.showSnackbar(it)
            vehiculoVm.limpiarError()
        }
    }

    var mostrarForm by remember { mutableStateOf(false) }
    var editando by remember { mutableStateOf<Mantenimiento?>(null) }
    var detalle by remember { mutableStateOf<Mantenimiento?>(null) }
    var aEliminar by remember { mutableStateOf<Mantenimiento?>(null) }

    val vehiculo = vehiculoState.vehiculo
    val kmActual = vehiculo?.kilometraje ?: 0

    AutoCareScaffold(
        title = "AutoCare",
        subtitle = "Control de mantenimiento",
        currentTab = AutoCareTab.MANTENIMIENTOS,
        onTabSelected = { tab -> navigateToTab(navController, tab) },
        showFab = true,
        onFabClick = {
            if (vehiculo == null) navigateToTab(navController, AutoCareTab.VEHICULO)
            else {
                editando = null
                mostrarForm = true
            }
        },
        onLogout = onLogout,
        snackbarHostState = snackbarHostState
    ) { padding ->
        when {
            mantState.cargando || vehiculoState.cargando -> Box(
                Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            mantState.lista.isEmpty() -> Box(
                Modifier.padding(padding).fillMaxSize().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Aún no hay mantenimientos registrados",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        if (vehiculo == null) "Primero registra tu vehículo en la pestaña Mi vehículo."
                        else "Toca «Añadir» para registrar el primero.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        "Mantenimientos registrados",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(mantState.lista, key = { it.id }) { m ->
                    MantenimientoCard(m, onClick = { detalle = m })
                }
            }
        }
    }

    // Consultar: detalle
    detalle?.let { m ->
        ModalBottomSheet(
            onDismissRequest = { detalle = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("${m.emoji} ${m.tipo}", style = MaterialTheme.typography.titleLarge)
                DetalleFila("Fecha", m.fecha)
                DetalleFila("Kilometraje", "${m.kilometraje.miles()} km")
                DetalleFila("Costo", m.costo.pesos())
                DetalleFila(
                    "Próximo kilometraje",
                    if (m.proximoKilometraje > 0) "${m.proximoKilometraje.miles()} km" else "No definido"
                )
                if (m.descripcion.isNotBlank()) DetalleFila("Descripción", m.descripcion)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            aEliminar = m
                            detalle = null
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Eliminar")
                    }
                    Button(
                        onClick = {
                            editando = m
                            detalle = null
                            mostrarForm = true
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Editar")
                    }
                }
            }
        }
    }

    // Crear / Editar
    if (mostrarForm) {
        MantenimientoFormSheet(
            inicial = editando,
            kmActual = kmActual,
            onGuardar = { m ->
                if (editando == null) mantVm.crear(m) else mantVm.editar(m)
                if (vehiculo != null && m.kilometraje > kmActual) {
                    vehiculoVm.actualizarKilometraje(m.kilometraje)
                }
                mostrarForm = false
                editando = null
            },
            onCerrar = {
                mostrarForm = false
                editando = null
            }
        )
    }

    // Eliminar (con confirmación)
    aEliminar?.let { m ->
        AlertDialog(
            onDismissRequest = { aEliminar = null },
            title = { Text("¿Eliminar mantenimiento?") },
            text = { Text("Se eliminará «${m.tipo}» del ${m.fecha}. Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    mantVm.eliminar(m.id)
                    aEliminar = null
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { aEliminar = null }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun MantenimientoCard(m: Mantenimiento, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Build,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "${m.emoji} ${m.tipo}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        m.fecha,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    m.costo.pesos(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "${m.kilometraje.miles()} km",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DetalleFila(etiqueta: String, valor: String) {
    Column {
        Text(
            etiqueta.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(valor, style = MaterialTheme.typography.bodyLarge)
    }
}