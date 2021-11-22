package com.keremturker.behero.repository

import com.google.firebase.auth.FirebaseAuth
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Response.Failure
import com.keremturker.behero.model.Response.Loading
import com.keremturker.behero.utils.Constants.ERROR_MESSAGE
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {
    suspend fun firebaseSignUpWithMail(mail: String, password: String) = flow {
        try {
            emit(Loading)
            val authResult = auth.createUserWithEmailAndPassword(mail, password).await()
            if (authResult.user != null) {
                emit(Response.Success(authResult.user!!))
            } else {
                emit(Failure(ERROR_MESSAGE))
            }
        } catch (e: Exception) {
            emit(Failure(e.message ?: ERROR_MESSAGE))
        }
    }
}