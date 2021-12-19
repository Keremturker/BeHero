package com.keremturker.behero.ui.fragment.donor

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Users
import com.keremturker.behero.repository.UsersRepository
import com.keremturker.behero.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchDonorVM @Inject constructor(
    app: Application,
    private val usersRepository: UsersRepository
) : BaseViewModel(app) {


    private val _users = SingleLiveEvent<Response<List<Users>>>()
    val users: LiveData<Response<List<Users>>> = _users


    fun getDonor(gender: String, bloodGroup: String, address: String) {
        viewModelScope.launch {
            usersRepository.getDonationsFromFirestore(
                gender = gender,
                bloodGroup = bloodGroup,
                address = address
            ).collect {
                _users.postValue(it)
            }
        }
    }
}