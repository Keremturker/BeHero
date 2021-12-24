package com.keremturker.behero.repository

import android.util.Log
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

    suspend fun updateDonationInFirestore(donation: Donations) = flow {
        try {
            emit(Response.Loading)
            auth.currentUser?.apply {
                donationsRef.document(donation.documentId).set(donation).await().also {
                    emit(Response.Success(it))
                }
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
            auth.signOut()
        }
    }


    suspend fun getDonationsFromFirestore(uuid: String? = auth.uid) = flow {
        try {
            emit(Response.Loading)
            auth.currentUser?.apply {
                val donations = donationsRef.whereEqualTo("uuid", uuid).get().await()
                    .toObjects(Donations::class.java)
                emit(Response.Success(donations))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
        }
    }

    suspend fun getDonationCount(uuid: String? = auth.uid) = flow {
        try {
            emit(Response.Loading)
            auth.currentUser?.apply {
                val donations =
                    donationsRef.whereEqualTo("uuid", uuid).whereEqualTo("enable", true).get()
                        .await()
                emit(Response.Success(donations.documents.size))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
        }
    }


    suspend fun getDonationsFromFirestore(
        bloodGroup: String,
        address: String,
        limit: Long? = null
    ) =
        flow {
            try {
                emit(Response.Loading)
                auth.currentUser?.apply {


                    var query = donationsRef.whereNotEqualTo("uuid", this.uid)
                    query = query.whereEqualTo("enable", true)

                    if (bloodGroup.isNotEmpty()) {
                        query = query.whereEqualTo("bloodGroup", bloodGroup)
                    }

                    limit?.let {
                        query = query.limit(it)
                    }


                    val donations = query.get().await().toObjects(Donations::class.java)
                    if (address.isNotEmpty()) {
                        val newList = arrayListOf<Donations>()
                        donations.forEach {
                            if (it.address.description?.lowercase()
                                    ?.contains(address.lowercase()) == true
                            ) {
                                newList.add(it)
                            }
                        }
                        emit(Response.Success(newList))

                    } else {
                        emit(Response.Success(donations))
                    }

                }
            } catch (e: Exception) {
                Log.d("test123", e.localizedMessage);
                emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
            }
        }

}