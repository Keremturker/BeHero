package com.keremturker.behero.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<BindingType : ViewBinding, ViewModelType : BaseViewModel> :
    AppCompatActivity() {

    lateinit var binding: BindingType
    abstract fun onActivityCreated()
    abstract fun observe()
    abstract fun getViewBinding(): BindingType
    protected abstract val viewModel: ViewModelType

    var onNewBackPress: (() -> Unit)? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        onActivityCreated()
        observe()
    }

    override fun onBackPressed() {
        if (onNewBackPress != null) {
            onNewBackPress!!()
        } else {
            super.onBackPressed()
        }
    }

}