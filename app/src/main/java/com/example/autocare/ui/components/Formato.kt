package com.example.autocare.ui.components

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val localeCo: Locale = Locale.forLanguageTag("es-CO")

/** 65200 -> "65.200" */
fun Int.miles(): String = NumberFormat.getIntegerInstance(localeCo).format(this)

/** 180000 -> "$180.000" */
fun Int.pesos(): String = "\$" + this.miles()

fun fechaHoy(): String = SimpleDateFormat("dd/MM/yyyy", localeCo).format(Date())

fun fechaValida(texto: String): Boolean {
    if (!Regex("\\d{2}/\\d{2}/\\d{4}").matches(texto.trim())) return false
    return try {
        SimpleDateFormat("dd/MM/yyyy", localeCo).apply { isLenient = false }
            .parse(texto.trim()) != null
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