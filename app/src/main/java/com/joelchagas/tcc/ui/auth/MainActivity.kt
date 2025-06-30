package com.joelchagas.tcc.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.joelchagas.tcc.R
import com.joelchagas.tcc.databinding.ActivityMainBinding
import com.joelchagas.tcc.ui.bottomsheet.Bottom_Sheet_Glicemia
import com.joelchagas.tcc.ui.bottomsheet.Bottom_Sheet_Remedio
import com.joelchagas.tcc.ui.fragment.glicemia.Fragment_Glicemia
import com.joelchagas.tcc.ui.fragment.home.Fragment_Home

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, HomeToMain {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private var isExpanded = false

    private val fromBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_fab)
    }

    private val toBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_fab)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navController = findNavController(R.id.fragment_container)
        navController.addOnDestinationChangedListener { _, _, _ ->
            // zera sempre que muda de tela
            supportActionBar?.title = ""
        }
        val navigationView = findViewById<NavigationView>(R.id.navigation_drawer)
        val headerView = navigationView.getHeaderView(0)
        val textViewNome = headerView.findViewById<TextView>(R.id.txtViewNome)
        val textViewEmail = headerView.findViewById<TextView>(R.id.txtViewEmail)

        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val userNome = sharedPreferences.getString("NOME", "Usuário")
        val userEmail = sharedPreferences.getString("EMAIL", "email@exemplo.com")

        textViewNome.text = "$userNome"
        textViewEmail.text = userEmail

        setSupportActionBar(binding.toolbar)

        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        binding.fabBackground.setOnClickListener { shrinkFab() }

        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> openFragment(Fragment_Home())
                R.id.bottom_calender -> openFragment(Fragment_Glicemia())
                else -> false
            }
            true
        }

        openFragment(Fragment_Home())
        setFabVisibility(View.INVISIBLE)

        binding.fab.setOnClickListener {
            if (isExpanded) shrinkFab() else expandFab()
        }

        binding.fabGlicemia.setOnClickListener { showDialogGlicemia() }
        binding.fabInsulina.setOnClickListener { showDialogInsulina() }
        binding.fabMedicamento.setOnClickListener { showDialogRemedio() }

        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            updateToolbarNavigation(currentFragment)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    // Volta para a Home se não houver mais fragments
                    openFragment(Fragment_Home())
                }
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> openFragment(Fragment_Home())
            R.id.nav_logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("msg", "add")
                startActivity(intent)
            }
            else -> return false
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
        updateToolbarNavigation(fragment)
    }

    private fun updateToolbarNavigation(fragment: Fragment?) {
        val isHome = fragment is Fragment_Home
        supportActionBar?.setDisplayHomeAsUpEnabled(!isHome)
        binding.toolbar.setNavigationOnClickListener(null)

        if (isHome) {
            toggle = ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )
            binding.drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            binding.toolbar.setNavigationOnClickListener {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        } else {
            binding.toolbar.navigationIcon = getDrawable(R.drawable.baseline_arrow_back_24)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun expandFab() {
        binding.fab.animate().rotation(45f).setDuration(200).start()
        showFab(binding.fabGlicemia)
        showFab(binding.fabInsulina)
        showFab(binding.fabMedicamento)
        binding.fabBackground.apply {
            visibility = View.VISIBLE
            alpha = 0f
            animate().alpha(1f).setDuration(200).start()
        }
        isExpanded = true
    }

    private fun shrinkFab() {
        binding.fab.animate().rotation(0f).setDuration(200).start()
        hideFab(binding.fabGlicemia)
        hideFab(binding.fabInsulina)
        hideFab(binding.fabMedicamento)
        binding.fabBackground.animate().alpha(0f).setDuration(200).withEndAction {
            binding.fabBackground.visibility = View.GONE
        }.start()
        isExpanded = false
    }

    private fun showFab(fab: View) {
        fab.visibility = View.VISIBLE
        fab.startAnimation(fromBottomFabAnim)
    }

    private fun hideFab(fab: View) {
        fab.startAnimation(toBottomFabAnim)
        fab.postDelayed({ fab.visibility = View.INVISIBLE }, toBottomFabAnim.duration)
    }

    private fun setFabVisibility(visibility: Int) {
        binding.fabGlicemia.visibility = visibility
        binding.fabInsulina.visibility = visibility
        binding.fabMedicamento.visibility = visibility
    }

    private fun showDialogInsulina() {
        val bottomSheetFragment = com.joelchagas.tcc.ui.bottomsheet.Bottom_Sheet_Insulina()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    private fun showDialogGlicemia() {
        val bottomSheetFragment = Bottom_Sheet_Glicemia()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    private fun showDialogRemedio() {
        val bottomSheetFragment = Bottom_Sheet_Remedio()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }
}
