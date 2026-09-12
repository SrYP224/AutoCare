package com.example.autocare.data

object FakeData {
    val vehiculo = Vehiculo("Chevrolet", "Onix", 2022, "ABC123", 65200)

    val mantenimientos = listOf(
        Mantenimiento("1", "Cambio de aceite", "🔧", "12/08/2026", 65000, 180000, 70000),
        Mantenimiento("2", "Cambio de frenos", "🛞", "20/06/2026", 62500, 350000, 82500),
        Mantenimiento("3", "Cambio de batería", "🔋", "15/03/2026", 58000, 120000, 98000),
    )

    val totalGastadoAnio = mantenimientos.sumOf { it.costo }
    val ultimoMantenimiento get() = mantenimientos.first()
}