package com.keremturker.behero.ui.fragment.donor.search

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Users
import com.keremturker.behero.repository.UsersRepository
import com.keremturker.behero.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class SearchDonorVM @Inject constructor(
    app: Application,
    private val usersRepository: UsersRepository
) : BaseViewModel(app) {


    private val _users = SingleLiveEvent<Response<List<Users>>>()
    val users: LiveData<Response<List<Users>>> = _users


    fun goToDetailDonor(users: Users) {
        Bundle().apply {
            putSerializable("users", users as Serializable)
            navigateFragment(
                navAction = R.id.nav_action_detailDonorFragment_global,
                bundle = this
            )
        }
    }

    fun getDonor(gender: String, bloodGroup: String, address: String, limit: Long? = null) {
        viewModelScope.launch {
            usersRepository.getDonorsFromFirestore(
                gender = gender,
                bloodGroup = bloodGroup,
                address = address,
                limit = limit
            ).collect {
                _users.postValue(it)
            }
        }
    }
}