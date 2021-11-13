package com.keremturker.behero.ui.fragment.my_donations

import androidx.fragment.app.viewModels
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentMyDonationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyDonationsFragment : BaseFragment<FragmentMyDonationsBinding, MyDonationsVM>() {
    override val viewModel: MyDonationsVM by viewModels()

    override fun getViewBinding() = FragmentMyDonationsBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {}
}