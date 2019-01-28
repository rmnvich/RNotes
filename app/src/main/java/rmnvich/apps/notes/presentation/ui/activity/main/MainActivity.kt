package rmnvich.apps.notes.presentation.ui.activity.main

import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.main_activity.*
import rmnvich.apps.notes.R
import rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes.DashboardNotesFragment
import rmnvich.apps.notes.presentation.ui.fragment.dashboardtags.DashboardTagsFragment
import rmnvich.apps.notes.presentation.ui.fragment.trash.TrashFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val mFragmentNotes = DashboardNotesFragment.newInstance(false)
    private val mFragmentFavoritesNotes = DashboardNotesFragment.newInstance(true)
    private val mFragmentTags = DashboardTagsFragment.newInstance()
    private val mFragmentTrash = TrashFragment.newInstance()

    private var mFragmentActive: Fragment = Fragment()
    private var mCurrentFragmentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        init()
    }

    private fun init() {
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        showFragment(mFragmentNotes, 0)
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
                    R.id.nav_notes -> {
                        showFragment(mFragmentNotes, 0)
                    }
                    R.id.nav_favorites -> {
                        showFragment(mFragmentFavoritesNotes, 1)
                    }
                    R.id.nav_trash -> {
                        showFragment(mFragmentTrash, 2)
                    }
                    R.id.nav_tags -> {
                        showFragment(mFragmentTags, 3)
                    }
                }
                dismissKeyboard()
                drawer_layout?.removeDrawerListener(this)
            }
        })
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showFragment(fragment: Fragment, fragmentPosition: Int) {
        if (mFragmentActive != fragment) {
            var enterAnim = R.anim.slide_in_up
            var exitAnim = R.anim.slide_out_up
            if (fragmentPosition < mCurrentFragmentPosition) {
                enterAnim = R.anim.slide_in_down
                exitAnim = R.anim.slide_out_down
            }

            supportFragmentManager.beginTransaction()
                .setCustomAnimations(enterAnim, exitAnim)
                .replace(R.id.content, fragment)
                .commit()
            mFragmentActive = fragment
            mCurrentFragmentPosition = fragmentPosition
        }
    }

    private fun dismissKeyboard() {
        val inputMethodManager = getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }
}
