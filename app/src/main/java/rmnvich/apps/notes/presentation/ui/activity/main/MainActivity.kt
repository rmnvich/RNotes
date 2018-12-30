package rmnvich.apps.notes.presentation.ui.activity.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.main_activity.*
import rmnvich.apps.notes.R
import rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes.DashboardNotesFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        init()
    }

    private fun init() {
        val view = layoutInflater.inflate(R.layout.nav_header,
                nav_view, false)
        nav_view.addHeaderView(view.rootView)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        showFragment(DashboardNotesFragment.newInstance())

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(R.id.nav_notes)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout?.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {
                when (item.itemId) {
                    R.id.nav_notes -> showFragment(DashboardNotesFragment.newInstance())
                    R.id.nav_favorites -> showFragment(DashboardNotesFragment.newInstance())
                    R.id.nav_tags -> showFragment(DashboardNotesFragment.newInstance())
                    R.id.nav_trash -> showFragment(DashboardNotesFragment.newInstance())
                }
                drawer_layout?.removeDrawerListener(this)
            }
        })
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.content, fragment)
                .commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }
}
