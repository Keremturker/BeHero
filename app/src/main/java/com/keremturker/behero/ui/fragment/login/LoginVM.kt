package com.keremturker.behero.ui.fragment.login

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Users
import com.keremturker.behero.repository.AuthRepository
import com.keremturker.behero.repository.ProfileRepository
import com.keremturker.behero.utils.SingleLiveEvent
import com.keremturker.behero.utils.extension.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val auth: FirebaseAuth,
    val app: Application
) : BaseViewModel(app) {

    private val _signInUser = SingleLiveEvent<Response<FirebaseUser>>()
    val signInUser: LiveData<Response<FirebaseUser>> = _signInUser

    private val _getUser = SingleLiveEvent<Response<Users>>()
    val getUser: LiveData<Response<Users>> = _getUser


    fun goToRegister() {
        navigateFragment(R.id.nav_action_registerFragment_global)
    }

    fun goToMainScreen() {
        navigateFragment(R.id.nav_action_mainFragment_global)
    }

    fun signInWithMail(mail: String, passWord: String) {
        if (mail.isValidEmail() && passWord.isNotEmpty()
        ) {
            viewModelScope.launch {
                authRepository.firebaseSignInWithMail(mail, passWord).collect {
                    _signInUser.postValue(it)
                }
            }
        } else {
            _signInUser.postValue(Response.Failure(app.getString(R.string.required_filed_text)))
        }
    }

    fun getUser() {
        viewModelScope.launch {
            profileRepository.getUserFromFirestore().collect {
                _getUser.postValue(it)
            }
        }
    }

    fun createOrUpdateUserInFirestore(user: Users) {
        viewModelScope.launch {
            profileRepository.createOrUpdateUserInFirestore(user).collect { }
        }
    }

    fun sendMailVerified(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    app.baseContext,
                    app.getText(R.string.sent_mail_text),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(app.baseContext, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun sendResetPassword(mail: String, function: (() -> Unit)) {
        auth.sendPasswordResetEmail(mail).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    app.baseContext,
                    app.getText(R.string.sent_password_text),
                    Toast.LENGTH_SHORT
                ).show()
                function.invoke()
            } else {
                Toast.makeText(app.baseContext, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun singOut() {
        authRepository.firebaseSignOut()
    }
}