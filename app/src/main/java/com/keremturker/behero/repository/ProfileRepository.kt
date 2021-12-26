package com.keremturker.behero.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Users
import com.keremturker.behero.utils.Constants.ERROR_MESSAGE
import com.keremturker.behero.utils.Constants.USERS_REF
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val auth: FirebaseAuth,
    @Named(USERS_REF) private val usersRef: CollectionReference
) {

    fun getUserFromFirestore() = flow {
        try {
            emit(Response.Loading)
            auth.currentUser?.apply {
                val user = usersRef.document(uid).get().await().toObject(Users::class.java)
                user?.let {
                    emit(Response.Success(user))
                } ?: emit(Response.Failure(ERROR_MESSAGE))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: ERROR_MESSAGE))
        }
    }

    suspend fun createOrUpdateUserInFirestore(user: Users) = flow {
        try {
            emit(Response.Loading)
            auth.currentUser?.apply {
                usersRef.document(uid).set(user).await().also {
                    emit(Response.Success(it))
                }
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: ERROR_MESSAGE))
            auth.signOut()
        }
    }
}