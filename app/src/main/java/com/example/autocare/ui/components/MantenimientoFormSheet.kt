package com.example.autocare.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.autocare.data.Mantenimiento

private val TIPOS_SUGERIDOS = listOf(
    "Cambio de aceite", "Frenos", "Filtros", "Batería", "Llantas", "Revisión general"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MantenimientoFormSheet(
    inicial: Mantenimiento?,
    kmActual: Int,
    onGuardar: (Mantenimiento) -> Unit,
    onCerrar: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var tipo by remember { mutableStateOf(inicial?.tipo ?: "") }
    var fecha by remember { mutableStateOf(inicial?.fecha ?: fechaHoy()) }
    var km by remember {
        mutableStateOf((inicial?.kilometraje ?: kmActual).takeIf { it > 0 }?.toString() ?: "")
    }
    var costo by remember { mutableStateOf(inicial?.costo?.toString() ?: "") }
    var proximoKm by remember {
        mutableStateOf(inicial?.proximoKilometraje?.takeIf { it > 0 }?.toString() ?: "")
    }
    var descripcion by remember { mutableStateOf(inicial?.descripcion ?: "") }
    var error by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(onDismissRequest = onCerrar, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                if (inicial == null) "Nuevo mantenimiento" else "Editar mantenimiento",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TIPOS_SUGERIDOS.forEach { sugerido ->
                    FilterChip(
                        selected = tipo == sugerido,
                        onClick = { tipo = sugerido },
                        label = { Text(sugerido) }
                    )
                }
            }

            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo de mantenimiento") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it.take(10) },
                label = { Text("Fecha (dd/mm/aaaa)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = km,
                onValueChange = { v -> km = v.filter { it.isDigit() }.take(7) },
                label = { Text("Kilometraje del servicio") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = costo,
                onValueChange = { v -> costo = v.filter { it.isDigit() }.take(9) },
                label = { Text("Costo (COP)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = proximoKm,
                onValueChange = { v -> proximoKm = v.filter { it.isDigit() }.take(7) },
                label = { Text("Próximo kilometraje recomendado") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (opcional)") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            if (error != null) {
                Text(
                    error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = onCerrar, modifier = Modifier.weight(1f)) {
                    Text("Cancelar")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = click@{
                        val kmInt = km.toIntOrNull()
                        val costoInt = costo.toIntOrNull()
                        val proximoInt = proximoKm.toIntOrNull() ?: 0

                        val mensaje = when {
                            tipo.isBlank() -> "Indica el tipo de mantenimiento"
                            !fechaValida(fecha) -> "La fecha debe tener el formato dd/mm/aaaa"
                            kmInt == null || kmInt <= 0 -> "Indica el kilometraje del servicio"
                            costoInt == null -> "Indica el costo"
                            proximoKm.isNotBlank() && proximoInt <= kmInt ->
                                "El próximo kilometraje debe ser mayor al del servicio"
                            else -> null
                        }
                        if (mensaje != null) {
                            error = mensaje
                            return@click
                        }
                        error = null
                        onGuardar(
                            (inicial ?: Mantenimiento()).copy(
                                tipo = tipo.trim(),
                                emoji = emojiPara(tipo),
                                fecha = fecha.trim(),
                                kilometraje = kmInt!!,
                                costo = costoInt!!,
                                proximoKilometraje = proximoInt,
                                descripcion = descripcion.trim()
                            )
                        )
                    }
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}