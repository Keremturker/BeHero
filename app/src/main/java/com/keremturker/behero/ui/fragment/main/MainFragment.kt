package com.keremturker.behero.ui.fragment.main

import androidx.fragment.app.viewModels
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainMV>() {
    override val viewModel: MainMV by viewModels()

    override fun getViewBinding() = FragmentMainBinding.inflate(layoutInflater)
    override var onNavigationViewShow = true

    override fun onFragmentCreated() {}

    override fun onReselected() {
        super.onReselected()
    }

}