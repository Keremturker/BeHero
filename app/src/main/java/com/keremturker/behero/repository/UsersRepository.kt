package com.keremturker.behero.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Users
import com.keremturker.behero.utils.Constants
import com.keremturker.behero.utils.Constants.USERS_REF
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val auth: FirebaseAuth,
    @Named(USERS_REF) private val usersRef: CollectionReference
) {

    suspend fun getDonationsFromFirestore() = flow {
        try {
            emit(Response.Loading)
            auth.currentUser?.apply {
                val donations = usersRef/*.whereNotEqualTo("uuid", this.uid)*/.get().await()
                    .toObjects(Users::class.java)
                emit(Response.Success(donations))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
        }
    }
}