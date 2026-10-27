package com.example.autocare.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class VehiculoRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    // usuarios/{uid}/vehiculo/actual
    private fun doc() = db.collection("usuarios")
        .document(auth.currentUser?.uid ?: error("No hay sesión iniciada"))
        .collection("vehiculo")
        .document("actual")

    fun observarVehiculo(): Flow<Vehiculo?> = callbackFlow {
        val registro = doc().addSnapshotListener { snap, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snap?.toObject(Vehiculo::class.java))
        }
        awaitClose { registro.remove() }
    }

    suspend fun guardarVehiculo(vehiculo: Vehiculo) {
        doc().set(vehiculo).await()
    }

    suspend fun actualizarKilometraje(km: Int) {
        doc().set(mapOf("kilometraje" to km), SetOptions.merge()).await()
    }
}