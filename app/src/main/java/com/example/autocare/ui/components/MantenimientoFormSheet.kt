package com.example.autocare.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.autocare.data.Mantenimiento

private val TIPOS_SUGERIDOS = listOf(
    "Cambio de aceite", "Frenos", "Filtros", "Batería", "Llantas", "Revisión general"
)
private const val OTRO = "__otro__"

private data class Entrada(val clave: String, val nombre: String)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MantenimientoFormSheet(
    inicial: Mantenimiento?,
    kmActual: Int,
    onGuardar: (List<Mantenimiento>) -> Unit,
    onCerrar: () -> Unit
) {
    val editando = inicial != null
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var seleccion by remember {
        mutableStateOf(
            if (inicial != null && inicial.tipo in TIPOS_SUGERIDOS) setOf(inicial.tipo) else emptySet()
        )
    }
    var otro by remember {
        mutableStateOf(
            if (inicial != null && inicial.tipo !in TIPOS_SUGERIDOS) inicial.tipo else ""
        )
    }
    var fecha by remember { mutableStateOf(inicial?.fecha ?: fechaHoy()) }
    var km by remember {
        mutableStateOf((inicial?.kilometraje ?: kmActual).takeIf { it > 0 }?.toString() ?: "")
    }
    var descripcion by remember { mutableStateOf(inicial?.descripcion ?: "") }
    var error by remember { mutableStateOf<String?>(null) }

    // Costo y próximo km de cada tipo, indexados por la "clave" del tipo
    val costos = remember {
        mutableStateMapOf<String, String>().apply {
            if (inicial != null) {
                put(if (inicial.tipo in TIPOS_SUGERIDOS) inicial.tipo else OTRO, inicial.costo.toString())
            }
        }
    }
    val proximos = remember {
        mutableStateMapOf<String, String>().apply {
            if (inicial != null && inicial.proximoKilometraje > 0) {
                put(
                    if (inicial.tipo in TIPOS_SUGERIDOS) inicial.tipo else OTRO,
                    inicial.proximoKilometraje.toString()
                )
            }
        }
    }

    val entradas = buildList<Entrada> {
        TIPOS_SUGERIDOS.filter { it in seleccion }.forEach { add(Entrada(it, it)) }
        if (otro.isNotBlank()) add(Entrada(OTRO, otro.trim()))
    }

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
                if (editando) "Editar mantenimiento" else "Nuevo mantenimiento",
                style = MaterialTheme.typography.titleLarge
            )

            if (!editando) {
                Text(
                    "Si en la misma visita hiciste varios servicios (por ejemplo frenos y el " +
                            "cambio de aceite que tenías pendiente), márcalos todos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text("Tipo de mantenimiento", style = MaterialTheme.typography.labelLarge)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TIPOS_SUGERIDOS.forEach { tipo ->
                    val marcado = tipo in seleccion
                    FilterChip(
                        selected = marcado,
                        onClick = {
                            seleccion = when {
                                editando -> {
                                    otro = ""
                                    setOf(tipo)
                                }
                                marcado -> seleccion - tipo
                                else -> seleccion + tipo
                            }
                        },
                        label = { Text(tipo) }
                    )
                }
            }

            OutlinedTextField(
                value = otro,
                onValueChange = {
                    otro = it
                    if (editando && it.isNotBlank()) seleccion = emptySet()
                },
                label = { Text("Otro tipo (opcional)") },
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

            // Un bloque de costo y próximo km por cada tipo marcado
            entradas.forEach { e ->
                Text(
                    e.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = costos[e.clave].orEmpty(),
                    onValueChange = { v -> costos[e.clave] = v.filter { it.isDigit() }.take(9) },
                    label = { Text("Costo (COP)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = proximos[e.clave].orEmpty(),
                    onValueChange = { v -> proximos[e.clave] = v.filter { it.isDigit() }.take(7) },
                    label = { Text("Próximo kilometraje recomendado") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

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

                        if (entradas.isEmpty()) {
                            error = "Elige al menos un tipo de mantenimiento"
                            return@click
                        }
                        if (!fechaValida(fecha)) {
                            error = "Fecha no válida: usa el formato dd/mm/aaaa y que no sea futura"
                            return@click
                        }
                        if (kmInt == null || kmInt <= 0) {
                            error = "Indica el kilometraje del servicio"
                            return@click
                        }
                        val nombres = entradas.map { it.nombre.lowercase() }
                        if (nombres.toSet().size != nombres.size) {
                            error = "Hay un tipo de mantenimiento repetido"
                            return@click
                        }

                        val resultado = mutableListOf<Mantenimiento>()
                        for (e in entradas) {
                            val costoInt = costos[e.clave]?.toIntOrNull()
                            if (costoInt == null) {
                                error = "Indica el costo de ${e.nombre}"
                                return@click
                            }
                            val proximoTexto = proximos[e.clave].orEmpty()
                            val proximoInt = proximoTexto.toIntOrNull() ?: 0
                            if (proximoTexto.isNotBlank() && proximoInt <= kmInt) {
                                error = "El próximo kilometraje de ${e.nombre} debe ser mayor al del servicio"
                                return@click
                            }
                            resultado += (inicial ?: Mantenimiento()).copy(
                                tipo = e.nombre,
                                emoji = emojiPara(e.nombre),
                                fecha = fecha.trim(),
                                kilometraje = kmInt,
                                costo = costoInt,
                                proximoKilometraje = proximoInt,
                                descripcion = descripcion.trim()
                            )
                        }
                        error = null
                        onGuardar(resultado)
                    }
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}