package com.example.autocare.data

data class Vehiculo(
    val marca: String,
    val modelo: String,
    val anio: Int,
    val placa: String,
    val kilometraje: Int
)

data class Mantenimiento(
    val id: String,
    val tipo: String,
    val emoji: String,
    val fecha: String,
    val kilometraje: Int,
    val costo: Int,
    val proximoKilometraje: Int
)