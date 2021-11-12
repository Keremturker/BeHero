package com.keremturker.behero.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainVM
@Inject constructor(
    private val auth: FirebaseAuth,
    app: Application
) : AndroidViewModel(app) {

}