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
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.joelchagas.tcc.R
import com.joelchagas.tcc.databinding.ActivityMainBinding
import com.joelchagas.tcc.ui.bottomsheet.Bottom_Sheet_Glicemia
import com.joelchagas.tcc.ui.bottomsheet.Bottom_Sheet_Remedio
import com.joelchagas.tcc.ui.fragment.glicemia.Fragment_Glicemia
import com.joelchagas.tcc.ui.fragment.home.Fragment_Home

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    HomeToMain {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarCfg: AppBarConfiguration
    private var isExpanded = false

    /* ---------- animações ---------- */
    private val fromBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_fab)
    }
    private val toBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_fab)
    }

    /* ====================================================================== */
    /* onCreate                                                               */
    /* ====================================================================== */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navController = findNavController(R.id.nav_home)

        // Destinos de topo (não mostram Up‑Button)
        appBarCfg = AppBarConfiguration(
            setOf(
                R.id.fragment_Home,
                R.id.fragment_Glicemia,
                R.id.fragment_Insulina,
                R.id.fragment_Remedios,
                R.id.fragment_Relatorios
            ),
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarCfg)

        // BottomNavigation + Drawer ligados ao NavController
        binding.bottomNavigation.apply {
            background = null          // deixa transparência p/ FAB
            setupWithNavController(navController)
        }
        binding.navigationDrawer.setupWithNavController(navController)

        // Mantém o item Sair no Drawer fora da navegação automática
        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        /* ---------- Drawer Toggle (hambúrguer) ---------- */
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        /* ---------- Dados do cabeçalho do Drawer ---------- */
        val header = binding.navigationDrawer.getHeaderView(0)
        header.findViewById<TextView>(R.id.txtViewNome).text =
            getSharedPreferences("AppPrefs", MODE_PRIVATE).getString("NOME", "Usuário")
        header.findViewById<TextView>(R.id.txtViewEmail).text =
            getSharedPreferences("AppPrefs", MODE_PRIVATE).getString("EMAIL", "email@exemplo.com")

        /* ---------- FAB e extensões ---------- */
        binding.fab.setOnClickListener { if (isExpanded) shrinkFab() else expandFab() }
        binding.fabBackground.setOnClickListener { shrinkFab() }
        binding.fabGlicemia   .setOnClickListener { Bottom_Sheet_Glicemia().show(supportFragmentManager, null) }
        binding.fabInsulina   .setOnClickListener { showDialogInsulina() }
        binding.fabMedicamento.setOnClickListener { Bottom_Sheet_Remedio().show(supportFragmentManager, null) }
        setFabVisibility(View.INVISIBLE)                  // começa recolhido

        /* ---------- Back‑Press: fecha Drawer ou delega ao NavController ---------- */
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    binding.drawerLayout.isDrawerOpen(GravityCompat.START) ->
                        binding.drawerLayout.closeDrawer(GravityCompat.START)

                    !navController.navigateUp(appBarCfg) ->     // pilha vazia?
                        finish()

                    else -> Unit
                }
            }
        })

        /* ---------- Zera título ao trocar de destino ---------- */
        navController.addOnDestinationChangedListener { _, _, _ ->
            supportActionBar?.title = ""
        }
    }

    /* ====================================================================== */
    /* Navegação do Drawer (apenas item "Sair")                               */
    /* ====================================================================== */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_logout -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> false
        }.also {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    /* ====================================================================== */
    /* Interface HomeToMain (mapeia Fragment -> destino do nav_graph)         */
    /* ====================================================================== */
    override fun openFragment(fragment: Fragment) {
        when (fragment) {
            is Fragment_Home     -> navController.navigate(R.id.fragment_Home)
            is Fragment_Glicemia -> navController.navigate(R.id.fragment_Glicemia)
            else -> { /* adicione outros mapeamentos se ainda precisar */ }
        }
    }

    /* ====================================================================== */
    /* FAB helpers                                                            */
    /* ====================================================================== */
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

    /* ====================================================================== */
    /* BottomSheets extras (só muda Insulina, já que outros foram simplificados) */
    /* ====================================================================== */
    private fun showDialogInsulina() {
        com.joelchagas.tcc.ui.bottomsheet.Bottom_Sheet_Insulina()
            .show(supportFragmentManager, null)
    }

    /* ====================================================================== */
    /* Support Navigate Up (Toolbar)                                          */
    /* ====================================================================== */
    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarCfg) || super.onSupportNavigateUp()
}
