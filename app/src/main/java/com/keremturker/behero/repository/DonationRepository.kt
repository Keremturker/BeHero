package com.keremturker.behero.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.keremturker.behero.model.Donations
import com.keremturker.behero.model.Response
import com.keremturker.behero.utils.Constants
import com.keremturker.behero.utils.Constants.DONATIONS_REF
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class DonationRepository @Inject constructor(
    private val auth: FirebaseAuth,
    @Named(DONATIONS_REF) private val donationsRef: CollectionReference
) {
    suspend fun createDonationInFirestore(donation: Donations) = flow {
        try {
            emit(Response.Loading)
            auth.currentUser?.apply {
                donationsRef.document().set(donation).await().also {
                    emit(Response.Success(it))
                }
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
            auth.signOut()
        }
    }
}