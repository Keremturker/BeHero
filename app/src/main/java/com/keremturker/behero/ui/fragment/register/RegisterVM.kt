package com.keremturker.behero.ui.fragment.register

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Users
import com.keremturker.behero.repository.AuthRepository
import com.keremturker.behero.utils.extension.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterVM @Inject constructor(
    private val repository: AuthRepository,
    val app: Application
) : BaseViewModel(app) {

    private val _signUpUser = MutableLiveData<Response<FirebaseUser>>()
    val signUpUser: LiveData<Response<FirebaseUser>> = _signUpUser

    private val _createUser = MutableLiveData<Response<Void>>()
    val createUser: LiveData<Response<Void>> = _createUser

    fun goToLogin() {
        navigateFragment(R.id.nav_action_RemoveRegisterFragment_global)
    }

    fun goToMaps() {
        navigateFragment(R.id.nav_action_mapsFragment_global)
    }

    fun signUpWithMail(
        name: String,
        mail: String,
        passWord: String,
        birthDay: String,
        address: String
    ) {
        if (signUpValidation(
                name = name,
                mail = mail,
                passWord = passWord,
                birthDay = birthDay,
                address = address
            )
        ) {
            viewModelScope.launch {
                repository.firebaseSignUpWithMail(mail, passWord).collect {
                    _signUpUser.postValue(it)
                }
            }
        } else if (name.isEmpty()) {
            _signUpUser.postValue(Response.Failure("isim boş"))
        } else if (!mail.isValidEmail()) {
            _signUpUser.postValue(Response.Failure("Mail doğru değil"))

        } else if (passWord.isEmpty()) {
            _signUpUser.postValue(Response.Failure("Şifre Boş"))
        } else if (address == app.getString(R.string.address_hint_text)) {
            _signUpUser.postValue(Response.Failure("Adres Boş"))
        } else if (birthDay == app.getString(R.string.birthday_hint_text)) {
            _signUpUser.postValue(Response.Failure("Doğum günü seç"))
        }
    }


    fun createUserInFirestore(user: Users) {
        viewModelScope.launch {
            repository.createUserInFirestore(user).collect {
                _createUser.postValue(it)
            }
        }
    }

    private fun signUpValidation(
        name: String,
        mail: String,
        passWord: String,
        birthDay: String,
        address: String
    ): Boolean {
        return !(name.isEmpty() || !mail.isValidEmail() || passWord.isEmpty() || birthDay == app.getString(
            R.string.birthday_hint_text
        ) || address == app.getString(R.string.address_hint_text))
    }
}