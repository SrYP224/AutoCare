package com.example.autocare.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MantenimientoRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    // usuarios/{uid}/mantenimientos/{id}
    private fun col() = db.collection("usuarios")
        .document(auth.currentUser?.uid ?: error("No hay sesión iniciada"))
        .collection("mantenimientos")

    // Más reciente primero (el kilometraje solo sube con el tiempo)
    fun observarMantenimientos(): Flow<List<Mantenimiento>> = callbackFlow {
        val registro = col()
            .orderBy("kilometraje", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snap?.toObjects(Mantenimiento::class.java) ?: emptyList())
            }
        awaitClose { registro.remove() }
    }

    suspend fun crear(m: Mantenimiento) {
        col().add(m).await()
    }

    suspend fun editar(m: Mantenimiento) {
        col().document(m.id).set(m).await()
    }

    suspend fun eliminar(id: String) {
        col().document(id).delete().await()
    }
}