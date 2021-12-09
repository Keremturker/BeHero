package com.keremturker.behero.ui.fragment.user.profile_edit

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Users
import com.keremturker.behero.repository.ProfileRepository
import com.keremturker.behero.utils.SharedHelper
import com.keremturker.behero.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditVM @Inject constructor(
    val app: Application,
    private val profileRepository: ProfileRepository,
    private val sharedHelper: SharedHelper
) : BaseViewModel(app) {

    private val _updateUser = SingleLiveEvent<Response<Void>>()
    val updateUser: LiveData<Response<Void>> = _updateUser


    fun goToMaps() {
        navigateFragment(R.id.nav_action_mapsFragmentSecond_global)
    }

    fun updateUser(user: Users) {
        if (signUpValidation(
                name = user.name,
                birthDay = user.birthDay
            )
        ) {

            viewModelScope.launch {
                profileRepository.createUserInFirestore(user).collect {
                    if (it is Response.Success) {
                        sharedHelper.syncUsers = user
                    }
                    _updateUser.postValue(it)
                }
            }
        } else {
            _updateUser.postValue(Response.Failure(app.getString(R.string.required_filed_text)))
        }
    }

    private fun signUpValidation(
        name: String,
        birthDay: String
    ): Boolean {
        return !(name.isEmpty() || birthDay == app.getString(
            R.string.birthday_hint_text
        ))
    }
}