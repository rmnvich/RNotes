package rmnvich.apps.notes.presentation.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import rmnvich.apps.notes.R
import rmnvich.apps.notes.presentation.ui.fragment.notesdashboard.DashboardNotesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DashboardNotesFragment.newInstance())
                .commitNow()
        }
    }

}
