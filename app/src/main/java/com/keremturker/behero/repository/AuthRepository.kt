package com.keremturker.behero.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Response.Failure
import com.keremturker.behero.model.Response.Loading
import com.keremturker.behero.utils.Constants.ERROR_MESSAGE
import com.keremturker.behero.utils.Constants.USERS_REF
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    @Named(USERS_REF) private val usersRef: CollectionReference
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

    suspend fun firebaseSignInWithMail(mail: String, password: String) = flow {

        try {
            emit(Loading)
            val authResult = auth.signInWithEmailAndPassword(mail, password).await()
            if (authResult.user != null) {
                emit(Response.Success(authResult.user!!))
            } else {
                emit(Failure(ERROR_MESSAGE))
            }

        } catch (e: Exception) {
            emit(Failure(e.message ?: ERROR_MESSAGE))
        }
    }

    fun firebaseSignOut() = auth.signOut()


    @ExperimentalCoroutinesApi
    fun getFirebaseAuthState() = callbackFlow  {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

}