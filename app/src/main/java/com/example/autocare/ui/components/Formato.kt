package com.example.autocare.ui.components

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

private val localeCo: Locale = Locale.forLanguageTag("es-CO")

/** 65200 -> "65.200" */
fun Int.miles(): String = NumberFormat.getIntegerInstance(localeCo).format(this)

/** 180000 -> "$180.000" */
fun Int.pesos(): String = "\$" + this.miles()

fun fechaHoy(): String = SimpleDateFormat("dd/MM/yyyy", localeCo).format(Date())

fun fechaValida(texto: String): Boolean {
    val fechaTexto = texto.trim()

    if (!Regex("\\d{2}/\\d{2}/\\d{4}").matches(fechaTexto)) {
        return false
    }

    return try {
        val formato = SimpleDateFormat("dd/MM/yyyy", localeCo).apply {
            isLenient = false
        }

        val fechaIngresada = formato.parse(fechaTexto) ?: return false
        val fechaActual = java.util.Calendar.getInstance().apply {
            set(
                java.util.Calendar.HOUR_OF_DAY, 0
            )
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }.time

        !fechaIngresada.after(fechaActual)
    } catch (e: Exception) {
        false
    }
}

fun emojiPara(tipo: String): String {
    val t = tipo.lowercase()
    return when {
        "freno" in t || "llanta" in t -> "🛞"
        "bater" in t -> "🔋"
        else -> "🔧"
    }
}