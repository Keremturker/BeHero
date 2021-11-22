package com.keremturker.behero.base

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.keremturker.behero.ui.activity.MainScreenActivity
import com.keremturker.behero.ui.activity.MainScreenActivity.OnReselectedDelegate
import com.keremturker.behero.utils.SelectedNavGraph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


abstract class BaseFragment<BindingType : ViewBinding, ViewModelType : BaseViewModel> :
    Fragment(), OnReselectedDelegate {

    private val baseActivity by lazy { activity as BaseActivity<*, *>? }


    lateinit var binding: BindingType
    protected abstract val viewModel: ViewModelType
    abstract fun getViewBinding(): BindingType
    abstract fun onFragmentCreated()
    open fun observe() {}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        showNavigationView()
        onFragmentCreated()
        observe()
        observeActions()

        return binding.root
    }

    private fun observeActions() {
        viewModel.navigateFragmentDetection.observeThis {
            baseActivity?.navigateFragment(it)
        }
        viewModel.loadingDetection.observeThis {
            baseActivity?.showHideProgress(it)
        }
    }


    fun updateOnBackPressed(onBackPress: (() -> Unit)? = null) {
        baseActivity?.onNewBackPress = onBackPress
    }

    fun requestPermission(requestCode: Int, vararg permission: String?) {
        val arr = arrayOf(*permission)
        activity?.let {
            if (Build.VERSION.SDK_INT > 23) {
                requestPermissions(arr, requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //granted
            onPermissionGranted(permissions)
        } else {
            //not granted
            onPermissionDenied(permissions)
        }
    }

    open fun onPermissionGranted(permissions: Array<String>) {}

    open fun onPermissionDenied(permissions: Array<String>) {}


    fun showNavigationFragment(selectedNavGraph: SelectedNavGraph) {
        (activity as MainScreenActivity?)?.showNavigationFragment(selectedNavGraph)
    }

    fun showNavigationView() {
        GlobalScope.launch(Dispatchers.Main) {
            (activity as MainScreenActivity?)?.setNavigationView(
                baseActivity?.onNavigationViewShow ?: true
            )
        }
    }


    fun <T> LiveData<T>.observeThis(function: (T) -> Unit) {
        observe(viewLifecycleOwner) {
            it?.let {
                function(it)
            }
        }
    }

    override fun onReselected() {}

}