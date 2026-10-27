package com.example.autocare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autocare.data.Mantenimiento
import com.example.autocare.data.MantenimientoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MantenimientosUiState(
    val cargando: Boolean = true,
    val lista: List<Mantenimiento> = emptyList()
) {
    val totalGastado: Int get() = lista.sumOf { it.costo }
    val ultimo: Mantenimiento? get() = lista.firstOrNull()   // la lista llega con el más reciente primero
}

class MantenimientosViewModel(
    private val repo: MantenimientoRepository = MantenimientoRepository()
) : ViewModel() {

    val uiState: StateFlow<MantenimientosUiState> = repo.observarMantenimientos()
        .map { MantenimientosUiState(cargando = false, lista = it) }
        .catch { emit(MantenimientosUiState(cargando = false)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MantenimientosUiState())

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun crear(m: Mantenimiento) = ejecutar { repo.crear(m) }
    fun editar(m: Mantenimiento) = ejecutar { repo.editar(m) }
    fun eliminar(id: String) = ejecutar { repo.eliminar(id) }

    fun limpiarError() { _error.value = null }

    private fun ejecutar(bloque: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                bloque()
            } catch (e: Exception) {
                _error.value = "No se pudo completar la acción. Revisa tu conexión."
            }
        }
    }
}