package com.keremturker.behero.ui.fragment.splash

import androidx.fragment.app.viewModels
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentSplashBinding
import com.keremturker.behero.ui.activity.SelectedNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashVM>() {

    override val viewModel: SplashVM by viewModels()

    override fun getViewBinding(): FragmentSplashBinding =
        FragmentSplashBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        GlobalScope.launch(Dispatchers.Main) {
            showNavigationView(false)
            delay(1500)
            showNavigationFragment(SelectedNavGraph.Home)

        }
    }
}