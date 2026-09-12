package com.example.autocare.ui.screens.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.autocare.data.FakeData
import com.example.autocare.ui.components.AutoCareScaffold
import com.example.autocare.ui.components.AutoCareTab
import com.example.autocare.ui.components.navigateToTab

@Composable
fun InicioScreen(navController: NavHostController) {
    val vehiculo = FakeData.vehiculo
    val ultimo = FakeData.ultimoMantenimiento
    val proximo = FakeData.mantenimientos.first() // ajusta luego a la lógica real de "próximo"

    AutoCareScaffold(
        title = "AutoCare",
        subtitle = "Control de mantenimiento",
        currentTab = AutoCareTab.INICIO,
        onTabSelected = { tab -> navigateToTab(navController, tab) },
        showFab = true,
        onFabClick = { /* TODO: abrir bottom sheet de nuevo mantenimiento */ }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            VehiculoResumenCard(vehiculo)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ProximoMantenimientoCard(
                    modifier = Modifier.weight(1f),
                    faltanKm = proximo.proximoKilometraje - vehiculo.kilometraje,
                    recomendadoA = proximo.proximoKilometraje
                )
                UltimoMantenimientoCard(modifier = Modifier.weight(1f), mantenimiento = ultimo)
            }

            TotalGastadoCard(total = FakeData.totalGastadoAnio)
        }
    }
}

@Composable
private fun VehiculoResumenCard(vehiculo: com.example.autocare.data.Vehiculo) {
    ElevatedCard(shape = RoundedCornerShape(12.dp)) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.size(56.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.DirectionsCar, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Column {
                Text("${vehiculo.marca} ${vehiculo.modelo} ${vehiculo.anio}",
                    style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                AssistChip(onClick = {}, label = { Text("Placa: ${vehiculo.placa}") })
                Spacer(Modifier.height(8.dp))
                Text("KILOMETRAJE ACTUAL", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("${vehiculo.kilometraje}", style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(4.dp))
                    Text("km", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
private fun ProximoMantenimientoCard(modifier: Modifier = Modifier, faltanKm: Int, recomendadoA: Int) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Build, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Próximo mantenimiento", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(8.dp))
            Text("🔧 Cambio de aceite", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            AssistChip(onClick = {}, label = { Text("Faltan $faltanKm km") })
            Spacer(Modifier.height(8.dp))
            Text("Recomendado a los $recomendadoA km", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun UltimoMantenimientoCard(modifier: Modifier = Modifier, mantenimiento: com.example.autocare.data.Mantenimiento) {
    OutlinedCard(modifier = modifier, shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(4.dp))
                Text("Último mantenimiento", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(8.dp))
            Text("${mantenimiento.emoji} ${mantenimiento.tipo}", fontWeight = FontWeight.Medium)
            Text(mantenimiento.fecha, style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("$${mantenimiento.costo}", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = {}) { Text("Ver historial") }
        }
    }
}

@Composable
private fun TotalGastadoCard(total: Int) {
    Card(shape = RoundedCornerShape(12.dp)) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.AccountBalanceWallet, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("TOTAL GASTADO AÑO", style = MaterialTheme.typography.labelSmall)
                    Text("$$total", style = MaterialTheme.typography.titleLarge)
                }
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null)
        }
    }
}