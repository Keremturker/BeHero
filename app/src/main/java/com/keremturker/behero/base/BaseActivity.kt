package com.keremturker.behero.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.keremturker.behero.R
import com.keremturker.behero.model.NavigateFragmentParams
import com.keremturker.behero.ui.activity.MainScreenActivity
import com.keremturker.behero.ui.activity.SelectedNavGraph

abstract class BaseActivity<BindingType : ViewBinding, ViewModelType : BaseViewModel> :
    AppCompatActivity() {

    lateinit var binding: BindingType
    abstract fun onActivityCreated()
    abstract fun observe()
    abstract fun navigateFragment(params: NavigateFragmentParams)
    abstract fun getViewBinding(): BindingType
    protected abstract val viewModel: ViewModelType
    open var onNavigationViewShow = false

    var onNewBackPress: (() -> Unit)? = null

    val user = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_BeHero)
        binding = getViewBinding()
        setContentView(binding.root)
        onActivityCreated()
        observe()
        checkUser()
    }

    override fun onBackPressed() {
        if (onNewBackPress != null) {
            onNewBackPress!!()
        } else {
            super.onBackPressed()
        }
    }

    private fun checkUser() {
        if (user) {
            onNavigationViewShow = true
            (this as MainScreenActivity?)?.showNavigationFragment(SelectedNavGraph.Home)
            (this as MainScreenActivity?)?.setNavigationView(true)
        } else {
            val params = NavigateFragmentParams(R.id.nav_action_RemoveLoginFragment_global)
            navigateFragment(params)
        }
    }

}