package com.keremturker.behero.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseActivity
import com.keremturker.behero.databinding.ActivityMainScreenBinding
import com.keremturker.behero.model.NavigateFragmentParams
import com.keremturker.behero.utils.SelectedNavGraph
import com.keremturker.behero.utils.extension.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainScreenActivity : BaseActivity<ActivityMainScreenBinding, MainScreenVM>() {

    private var currentNavController: NavController? = null
    private var isFirstCallOfFirstTab = true

    override fun getViewBinding() = ActivityMainScreenBinding.inflate(layoutInflater)
    override val viewModel: MainScreenVM by viewModels()

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onActivityCreated() {
        checkUser()
        setupBottomNavBar()

      /*  if (viewModel.isUserLogin()){
            Log.d("test123","login")
            showNavigationFragment(SelectedNavGraph.Home)

        }else{
            Log.d("test123","login değil")
            showNavigationFragment(SelectedNavGraph.Splash)

        }*/

    }

    override fun observe() {}


    private fun setupBottomNavBar() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.sectionSplash)
        currentNavController = (navHostFragment as NavHostFragment).navController
        binding.navigationView.setOnItemSelectedListener { item ->
            val previousNavController = currentNavController
            val selectedNavGraph: SelectedNavGraph?
            when (item.itemId) {
                R.id.navigation_home -> {
                    currentNavController = findNavController(R.id.sectionMain)
                    selectedNavGraph = SelectedNavGraph.Home
                }
                R.id.navigation_search -> {
                    currentNavController = findNavController(R.id.sectionDonationSearch)
                    selectedNavGraph = SelectedNavGraph.Search
                }
                R.id.navigation_donation -> {
                    currentNavController = findNavController(R.id.sectionMyDonation)
                    selectedNavGraph = SelectedNavGraph.Donation
                }
                R.id.navigation_user -> {
                    currentNavController = findNavController(R.id.sectionUser)
                    selectedNavGraph = SelectedNavGraph.User
                }
                else -> {
                    currentNavController = null
                    selectedNavGraph = null
                }
            }
            showNavigationFragment(selectedNavGraph)
            onReselected(item.itemId, previousNavController)
            currentNavController?.addOnDestinationChangedListener(::onFragmentChanged)
            if (isFirstCallOfFirstTab && item.itemId == R.id.navigation_home) {
                reSelectOfferTab()
                isFirstCallOfFirstTab = false
            }
            when (currentNavController) {
                null -> false
                else -> true
            }

        }
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        currentNavController?.navigateUp()
    }

    override fun onBackPressed() {
        if (currentNavController == null || currentNavController?.popBackStack()?.not() == true) {
            finish()
        }
    }

    private fun onFragmentChanged(
        navController: NavController,
        navDestination: NavDestination,
        bundle: Bundle?
    ) {
        val isAOneOfMainFragments = when (navDestination.id) {
            R.id.mainFragment,
            R.id.donationSearchFragment,
            R.id.myDonationsFragment,
            R.id.userFragment -> false
            else -> true
        }
        showOnBackButton(isAOneOfMainFragments)
    }

    private fun showOnBackButton(isAOneOfMainFragments: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isAOneOfMainFragments)
    }

    fun showNavigationFragment(graph: SelectedNavGraph?) {
        binding.navigationView.visibleIf(graph != SelectedNavGraph.Splash)
        // binding.actionBar.root.visibleIf(graph != SelectedNavGraph.Splash)
        binding.sectionWrapperSplash.visibleIf(graph == SelectedNavGraph.Splash)
        binding.sectionWrapperMain.visibleIf(graph == SelectedNavGraph.Home)
        binding.sectionWrapperDonationSearch.visibleIf(graph == SelectedNavGraph.Search)
        binding.sectionWrapperMyDonation.visibleIf(graph == SelectedNavGraph.Donation)
        binding.sectionWrapperUser.visibleIf(graph == SelectedNavGraph.User)

        when (graph) {
            SelectedNavGraph.Home -> {
                currentNavController = findNavController(R.id.sectionMain)
                // binding.actionBar.txtToolbarTitle.text = NOTIFICATION_GROUP_TITLE_FRAGMENT
            }
            SelectedNavGraph.Search -> {
                // binding.actionBar.txtToolbarTitle.text = SETTING_FRAGMENT
            }
            SelectedNavGraph.Donation -> {
                // binding.actionBar.txtToolbarTitle.text = NOTIFICATION_APP_FRAGMENT
            }
            SelectedNavGraph.User -> {
                // binding.actionBar.txtToolbarTitle.text = NOTIFICATION_APP_FRAGMENT
            }
            SelectedNavGraph.Splash -> {
                // binding.actionBar.txtToolbarTitle.text = ""
            }
        }
    }


    private fun onReselected(itemId: Int, previousNavController: NavController?) {
        val fragmentResId: Int = when (itemId) {
            R.id.navigation_home -> R.id.sectionMain
            R.id.navigation_search -> R.id.sectionDonationSearch
            R.id.navigation_donation -> R.id.sectionMyDonation
            else -> R.id.sectionUser
        }
        if (previousNavController == currentNavController && currentNavController != null && fragmentResId == R.id.sectionSplash) {
            onClickSameBottomNavigationButton(fragmentResId)
            return
        }
        val navHostFragment = supportFragmentManager.findFragmentById(fragmentResId)
        val reSelectedFragment = navHostFragment?.childFragmentManager?.fragments?.lastOrNull {
            it is OnReselectedDelegate
        } as OnReselectedDelegate?
        reSelectedFragment?.onReselected()
    }


    private fun onClickSameBottomNavigationButton(fragmentResId: Int) {
        currentNavController?.popBackStack(fragmentResId, true)
        currentNavController?.graph?.startDestination?.let { currentNavController?.navigate(it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun reSelectOfferTab() {
        lifecycleScope.launch {
            delay(250)
            onReselected(R.id.navigation_home, null)

        }
    }

    fun setNavigationView(isShow: Boolean = false) {
        binding.navigationView.visibleIf(isShow)
    }

    interface OnReselectedDelegate {
        fun onReselected()
    }

    override fun navigateFragment(params: NavigateFragmentParams) {
        currentNavController?.navigate(
            params.navAction,
            params.bundle,
            params.navOptions,
            params.extras
        )

    }

    override fun showHideProgress(isShow: Boolean) {
        binding.pbLoading.visibleIf(isShow)
    }

    private fun checkUser() {
        val user = auth.currentUser != null
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