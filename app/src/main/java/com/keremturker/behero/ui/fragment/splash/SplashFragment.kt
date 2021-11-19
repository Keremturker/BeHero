package com.keremturker.behero.ui.fragment.splash

import androidx.fragment.app.viewModels
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashVM>() {

    override val viewModel: SplashVM by viewModels()

    override fun getViewBinding(): FragmentSplashBinding =
        FragmentSplashBinding.inflate(layoutInflater)

    val user = true
    override fun onFragmentCreated() {
  /*      GlobalScope.launch(Dispatchers.Main) {
            showNavigationView(false)
            delay(1500)

            if (user) {
                showNavigationFragment(SelectedNavGraph.Home)
            } else {
                viewModel.navToLogin()

            }

        }*/
    }


    override fun onReselected() {
        activity?.finish()
    }
}