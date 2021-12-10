package com.keremturker.behero.ui.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseActivity
import com.keremturker.behero.databinding.ActivityMainScreenBinding
import com.keremturker.behero.model.NavigateFragmentParams
import com.keremturker.behero.utils.ToolbarType
import com.keremturker.behero.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainScreenActivity : BaseActivity<ActivityMainScreenBinding, MainScreenVM>() {

    private var currentNavController: NavController? = null

    override fun getViewBinding() = ActivityMainScreenBinding.inflate(layoutInflater)
    override val viewModel: MainScreenVM by viewModels()

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onActivityCreated() {
        setSupportActionBar(binding.actionBarNormal.root)
        setupBottomNavBar()
        checkUser()
    }

    private fun checkUser() {
        val user = auth.currentUser != null
        if (user) {
            val params = NavigateFragmentParams(R.id.nav_action_mainFragment_global_removeAll)
            navigateFragment(params)
        } else {
            val params = NavigateFragmentParams(R.id.nav_action_RemoveLoginFragment_global)
            navigateFragment(params)
        }
    }

    override fun observe() {}

    private fun setupBottomNavBar() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.sectionMain)
        currentNavController = (navHostFragment as NavHostFragment).navController
        binding.navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val params = NavigateFragmentParams(R.id.nav_action_mainFragment_global)
                    navigateFragment(params)
                }
                R.id.navigation_search -> {
                    val params =
                        NavigateFragmentParams(R.id.nav_action_searchDonationsFragment_global_removeAll)
                    navigateFragment(params)
                }
                R.id.navigation_donation -> {
                    val params =
                        NavigateFragmentParams(R.id.nav_action_mineDonationsFragment_global_removeAll)
                    navigateFragment(params)
                }
                R.id.navigation_user -> {
                    val params =
                        NavigateFragmentParams(R.id.nav_action_userFragment_global_removeAll)
                    navigateFragment(params)
                }
                else -> {
                    currentNavController = null
                }
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

    fun setNavigationView(isShow: Boolean = false) {
        binding.navigationView.visibleIf(isShow)
    }

    fun setNormalToolbar(
        isBackIcon: Boolean = false,
        title: String = "",
        rightIcon: Int = 0,
        rightIconFunction: (() -> Unit)? = null
    ) {
        binding.actionBarNormal.apply {
            txtToolbarTitle.text = title
            binding.viewLine.visibleIf(true)

            if (isBackIcon) {
                imgToolbarIcon.setVisible()
                val icon =
                    this@MainScreenActivity.getBitmapFromVectorDrawable(R.drawable.ic_left_arrow)
                icon?.let {
                    imgToolbarIcon.setImage(it)
                }

                imgToolbarIcon.setOnClickListener {
                    onBackPressed()
                }
            } else {
                imgToolbarIcon.setInvisible()
            }
            if (rightIcon == 0) {
                imgRightIcon.setInvisible()
            } else {
                imgRightIcon.setVisible()
                val icon = this@MainScreenActivity.getBitmapFromVectorDrawable(rightIcon)
                icon?.let {
                    imgRightIcon.setImage(it)
                    imgRightIcon.setOnClickListener {
                        rightIconFunction?.invoke()
                    }
                }
            }
        }
    }

    override fun performBackPressed() {
        onBackPressed()
    }

    override fun infoToolbarType(type: ToolbarType) {
        when (type) {
            ToolbarType.Empty -> {
                showHideActionBarNormal(false)
            }
            ToolbarType.Normal -> {
                showHideActionBarNormal(true)
            }
        }
    }

    private fun showHideActionBarNormal(isShow: Boolean) {
        binding.actionBarNormal.root.visibleIf(isShow)
        binding.viewLine.visibleIf(isShow)
    }
}