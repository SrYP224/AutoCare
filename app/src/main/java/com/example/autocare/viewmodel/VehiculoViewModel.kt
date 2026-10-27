package com.example.autocare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autocare.data.Vehiculo
import com.example.autocare.data.VehiculoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class VehiculoUiState(
    val cargando: Boolean = true,
    val vehiculo: Vehiculo? = null   // null y cargando=false → el usuario aún no registró su vehículo
)

class VehiculoViewModel(
    private val repo: VehiculoRepository = VehiculoRepository()
) : ViewModel() {

    val uiState: StateFlow<VehiculoUiState> = repo.observarVehiculo()
        .map { VehiculoUiState(cargando = false, vehiculo = it) }
        .catch { emit(VehiculoUiState(cargando = false)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), VehiculoUiState())

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun guardar(vehiculo: Vehiculo) = ejecutar { repo.guardarVehiculo(vehiculo) }

    fun actualizarKilometraje(km: Int) = ejecutar { repo.actualizarKilometraje(km) }

    fun limpiarError() { _error.value = null }

    private fun ejecutar(bloque: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                bloque()
            } catch (e: Exception) {
                _error.value = "No se pudo guardar. Revisa tu conexión."
            }
        }
    }
}