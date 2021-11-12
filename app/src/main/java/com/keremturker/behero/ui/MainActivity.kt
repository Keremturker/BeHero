package com.keremturker.behero.ui

import androidx.activity.viewModels
import com.keremturker.behero.base.BaseActivity
import com.keremturker.behero.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainVM>() {

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    override val viewModel: MainVM by viewModels()

    override fun onActivityCreated() {}

    override fun observe() {}
}