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
import com.keremturker.behero.utils.Constants.emptyText
import com.keremturker.behero.utils.ToolbarType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


abstract class BaseFragment<BindingType : ViewBinding, ViewModelType : BaseViewModel> :
    Fragment() {

    private val baseActivity by lazy { activity as BaseActivity<*, *>? }


    lateinit var binding: BindingType
    protected abstract val viewModel: ViewModelType
    abstract fun getViewBinding(): BindingType
    abstract fun onFragmentCreated()
    open fun observe() {}
    open var onNavigationViewShow = false
    open var toolbarType = ToolbarType.Empty


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        onFragmentCreated()
        showNavigationView(onNavigationViewShow)
        infoToolbarType(toolbarType)
        observe()
        observeActions()
        viewModel.loadingDetection.postValue(false)
        return binding.root
    }


    private fun infoToolbarType(type: ToolbarType) {
        GlobalScope.launch(Dispatchers.Main) {
            (activity as MainScreenActivity?)?.infoToolbarType(type)
        }
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

    open fun reloadActivity() {
        baseActivity?.reloadActivity()
    }

    open fun onPermissionGranted(permissions: Array<String>) {}

    open fun onPermissionDenied(permissions: Array<String>) {}

    fun setNormalToolbar(
        isBackIcon: Boolean = false,
        title: String = emptyText(),
        rightIcon: Int = 0,
        rightIconFunction: (() -> Unit)? = null
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            (activity as MainScreenActivity?)?.setNormalToolbar(
                isBackIcon,
                title,
                rightIcon,
                rightIconFunction
            )
        }
    }

    private fun <T> LiveData<T>.observeThis(function: (T) -> Unit) {
        observe(viewLifecycleOwner) {
            it?.let {
                function(it)
            }
        }
    }

    private fun showNavigationView(isShow: Boolean) {
        GlobalScope.launch(Dispatchers.Main) {
            (activity as MainScreenActivity?)?.setNavigationView(isShow)
        }
    }

    fun onBackPress() {
        baseActivity?.performBackPressed()
    }
}