package com.example.autocare.data

enum class EstadoMantenimiento { AL_DIA, PENDIENTE }   // 🟢 / 🔴

data class ProximoMantenimiento(
    val mantenimiento: Mantenimiento,
    val faltanKm: Int
) {
    val estado: EstadoMantenimiento
        get() = if (faltanKm > 0) EstadoMantenimiento.AL_DIA else EstadoMantenimiento.PENDIENTE
}

/**
 * De cada tipo de mantenimiento cuenta solo el registro más reciente (un cambio de aceite
 * nuevo reemplaza el "próximo km" del anterior). Entre esos, devuelve el que vence primero.
 */
fun calcularProximo(lista: List<Mantenimiento>, kmActual: Int): ProximoMantenimiento? =
    lista.groupBy { it.tipo }
        .map { (_, registros) -> registros.maxBy { it.kilometraje } }
        .filter { it.proximoKilometraje > 0 }
        .minByOrNull { it.proximoKilometraje }
        ?.let { ProximoMantenimiento(it, it.proximoKilometraje - kmActual) }