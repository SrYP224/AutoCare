package com.example.autocare.ui.screens.vehiculo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.autocare.data.Vehiculo
import com.example.autocare.ui.components.AutoCareScaffold
import com.example.autocare.ui.components.AutoCareTab
import com.example.autocare.ui.components.miles
import com.example.autocare.ui.components.navigateToTab
import com.example.autocare.viewmodel.VehiculoViewModel
import java.util.Calendar
import com.example.autocare.ui.components.KilometrajeDialog

@Composable
fun MiVehiculoScreen(
    navController: NavHostController,
    onLogout: () -> Unit,
    vm: VehiculoViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()
    val error by vm.error.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            vm.limpiarError()
        }
    }

    var editando by remember { mutableStateOf(false) }
    var actualizandoKm by remember { mutableStateOf(false) }
    val vehiculo = state.vehiculo

    AutoCareScaffold(
        title = "AutoCare",
        subtitle = "Control de mantenimiento",
        currentTab = AutoCareTab.VEHICULO,
        onTabSelected = { tab -> navigateToTab(navController, tab) },
        onLogout = onLogout,
        snackbarHostState = snackbarHostState
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Mi vehículo", style = MaterialTheme.typography.titleLarge)

            when {
                state.cargando -> Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                vehiculo == null -> Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Aún no registras tu vehículo", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Registra los datos de tu vehículo para empezar a llevar el control de sus mantenimientos.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(onClick = { editando = true }) { Text("Registrar vehículo") }
                    }
                }
                else -> VehiculoDetalle(
                    vehiculo = vehiculo,
                    onEditar = { editando = true },
                    onActualizarKm = { actualizandoKm = true }
                )
            }
        }
    }

    if (editando) {
        VehiculoDialog(
            inicial = vehiculo,
            onGuardar = {
                vm.guardar(it)
                editando = false
            },
            onCerrar = { editando = false }
        )
    }

    if (actualizandoKm && vehiculo != null) {
        KilometrajeDialog(
            kmActual = vehiculo.kilometraje,
            onGuardar = {
                vm.actualizarKilometraje(it)
                actualizandoKm = false
            },
            onCerrar = { actualizandoKm = false }
        )
    }
}

@Composable
private fun VehiculoDetalle(vehiculo: Vehiculo, onEditar: () -> Unit, onActualizarKm: () -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(112.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.DirectionsCar,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "${vehiculo.marca} ${vehiculo.modelo}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    "${vehiculo.anio} • ${vehiculo.placa}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Kilometraje + botón para actualizarlo (lo primero que se ve)
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(Icons.Filled.Speed, contentDescription = null)
                        Column {
                            Text("KILOMETRAJE ACTUAL", style = MaterialTheme.typography.labelSmall)
                            Text(
                                "${vehiculo.kilometraje.miles()} km",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Button(
                        onClick = onActualizarKm,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(Icons.Filled.Update, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Actualizar kilometraje")
                    }
                }
            }

            InfoItem(Icons.Filled.DirectionsCar, "Marca", vehiculo.marca)
            InfoItem(Icons.Filled.Category, "Modelo", vehiculo.modelo)
            InfoItem(Icons.Filled.CalendarToday, "Año", vehiculo.anio.toString())
            InfoItem(Icons.Filled.Pin, "Placa", vehiculo.placa)

            OutlinedButton(onClick = onEditar, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Editar información")
            }
        }
    }
}

@Composable
private fun InfoItem(icono: ImageVector, etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
        }
        Column {
            Text(
                etiqueta.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(valor, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun VehiculoDialog(
    inicial: Vehiculo?,
    onGuardar: (Vehiculo) -> Unit,
    onCerrar: () -> Unit
) {
    var marca by remember { mutableStateOf(inicial?.marca ?: "") }
    var modelo by remember { mutableStateOf(inicial?.modelo ?: "") }
    var anio by remember { mutableStateOf(inicial?.anio?.toString() ?: "") }
    var placa by remember { mutableStateOf(inicial?.placa ?: "") }
    var km by remember { mutableStateOf(inicial?.kilometraje?.toString() ?: "") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text(if (inicial == null) "Registrar vehículo" else "Editar información") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = marca, onValueChange = { marca = it },
                    label = { Text("Marca") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = modelo, onValueChange = { modelo = it },
                    label = { Text("Modelo") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = anio,
                    onValueChange = { v -> anio = v.filter { it.isDigit() }.take(4) },
                    label = { Text("Año") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = placa,
                    onValueChange = { v -> placa = v.uppercase().filter { it.isLetterOrDigit() }.take(7) },
                    label = { Text("Placa") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = km,
                    onValueChange = { v -> km = v.filter { it.isDigit() }.take(7) },
                    label = { Text("Kilometraje") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                if (error != null) {
                    Text(
                        error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = click@{
                val anioInt = anio.toIntOrNull()
                val kmInt = km.toIntOrNull()
                val anioMax = Calendar.getInstance().get(Calendar.YEAR) + 1

                val mensaje = when {
                    marca.isBlank() -> "Indica la marca"
                    modelo.isBlank() -> "Indica el modelo"
                    anioInt == null || anioInt < 1950 || anioInt > anioMax ->
                        "Indica un año válido (1950 a $anioMax)"
                    placa.length < 5 -> "La placa debe tener entre 5 y 7 caracteres"
                    kmInt == null -> "Indica el kilometraje"
                    else -> null
                }
                if (mensaje != null) {
                    error = mensaje
                    return@click
                }
                onGuardar(
                    Vehiculo(
                        marca = marca.trim(),
                        modelo = modelo.trim(),
                        anio = anioInt!!,
                        placa = placa,
                        kilometraje = kmInt!!
                    )
                )
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onCerrar) { Text("Cancelar") } }
    )
}

