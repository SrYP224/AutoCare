package com.example.autocare.data

import com.google.firebase.firestore.DocumentId

data class Vehiculo(
    val marca: String = "",
    val modelo: String = "",
    val anio: Int = 0,
    val placa: String = "",
    val kilometraje: Int = 0
)

data class Mantenimiento(
    @DocumentId val id: String = "",
    val tipo: String = "",
    val emoji: String = "🔧",
    val fecha: String = "",
    val kilometraje: Int = 0,
    val costo: Int = 0,
    val proximoKilometraje: Int = 0,
    val descripcion: String = ""
)