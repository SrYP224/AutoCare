package com.example.autocare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autocare.data.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.autocare.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

data class AuthUiState(
    val cargando: Boolean = false,
    val error: String? = null,
    val mensaje: String? = null
)

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val haySesion: Boolean get() = repo.isLoggedIn

    fun login(email: String, password: String, onExito: () -> Unit) {
        autenticar(onExito) { repo.login(email.trim(), password) }
    }

    fun registrar(email: String, password: String, onExito: () -> Unit) {
        autenticar(onExito) { repo.register(email.trim(), password) }
    }

    private fun autenticar(onExito: () -> Unit, accion: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(cargando = true)
            accion()
                .onSuccess {
                    _uiState.value = AuthUiState()
                    onExito()
                }
                .onFailure { _uiState.value = AuthUiState(error = traducirError(it)) }
        }
    }

    fun cerrarSesion() = repo.logout()

    fun limpiarError() {
        _uiState.value = _uiState.value.copy(error = null, mensaje = null)
    }

    fun loginConGoogle(context: Context, onExito: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(cargando = true)
            try {
                val opcion = GetSignInWithGoogleOption.Builder(
                    context.getString(R.string.default_web_client_id)
                ).build()
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(opcion)
                    .build()
                val resultado = CredentialManager.create(context).getCredential(context, request)
                val credencial = resultado.credential

                if (credencial is CustomCredential &&
                    credencial.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val token = GoogleIdTokenCredential.createFrom(credencial.data).idToken
                    repo.loginConGoogle(token)
                        .onSuccess {
                            _uiState.value = AuthUiState()
                            onExito()
                        }
                        .onFailure { _uiState.value = AuthUiState(error = traducirError(it)) }
                } else {
                    _uiState.value = AuthUiState(error = "No se pudo iniciar sesión con Google")
                }
            } catch (e: GetCredentialCancellationException) {
                _uiState.value = AuthUiState()   // el usuario cerró el selector de cuenta
            } catch (e: NoCredentialException) {
                _uiState.value = AuthUiState(error = "No hay una cuenta de Google en este dispositivo")
            } catch (e: GetCredentialException) {
                _uiState.value = AuthUiState(error = "No se pudo iniciar sesión con Google")
            }
        }
    }


    fun recuperarContrasena(email: String) {
        if (email.isBlank()) {
            _uiState.value = AuthUiState(error = "Escribe tu correo para recuperar la contraseña")
            return
        }
        viewModelScope.launch {
            _uiState.value = AuthUiState(cargando = true)
            repo.recuperarContrasena(email.trim())
                .onSuccess {
                    _uiState.value = AuthUiState(mensaje = "Te enviamos un correo para restablecer tu contraseña")
                }
                .onFailure { _uiState.value = AuthUiState(error = traducirError(it)) }
        }
    }

    // WeakPassword va primero porque hereda de InvalidCredentials
    private fun traducirError(e: Throwable): String = when (e) {
        is FirebaseAuthWeakPasswordException -> "La contraseña debe tener al menos 6 caracteres"
        is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con ese correo"
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> "Correo o contraseña incorrectos"
        is FirebaseNetworkException -> "Sin conexión a internet"
        else -> "Ocurrió un error. Inténtalo de nuevo"
    }
}