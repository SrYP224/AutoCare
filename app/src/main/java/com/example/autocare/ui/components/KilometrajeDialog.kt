package com.example.autocare.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun KilometrajeDialog(
    kmActual: Int,
    onGuardar: (Int) -> Unit,
    onCerrar: () -> Unit
) {
    var km by remember { mutableStateOf(kmActual.toString()) }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Actualizar kilometraje") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = km,
                    onValueChange = { v -> km = v.filter { it.isDigit() }.take(7) },
                    label = { Text("Kilometraje actual") },
                    singleLine = true,
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
                val kmInt = km.toIntOrNull()
                if (kmInt == null) {
                    error = "Indica el kilometraje"
                    return@click
                }
                if (kmInt < kmActual) {
                    error = "No puede ser menor al kilometraje actual ($kmActual km)"
                    return@click
                }
                onGuardar(kmInt)
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onCerrar) { Text("Cancelar") } }
    )
}