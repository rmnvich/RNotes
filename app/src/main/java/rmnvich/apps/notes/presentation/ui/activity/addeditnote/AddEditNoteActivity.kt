package rmnvich.apps.notes.presentation.ui.activity.addeditnote

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants.EXTRA_NOTE_ID
import rmnvich.apps.notes.databinding.AddEditNoteActivityBinding
import rmnvich.apps.notes.di.addeditnote.AddEditNoteModule
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import javax.inject.Inject

class AddEditNoteActivity : AppCompatActivity() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mAddEditNoteViewModel: AddEditNoteViewModel
    private lateinit var mAddEditNoteBinding: AddEditNoteActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAddEditNoteBinding = DataBindingUtil.setContentView(this, R.layout.add_edit_note_activity)
        App.getApp(applicationContext).componentsHolder
                .getComponent(javaClass, AddEditNoteModule(application))
                ?.inject(this)
    }

    @Inject
    fun init() {
        mAddEditNoteViewModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(AddEditNoteViewModel::class.java)
        mAddEditNoteBinding.viewmodel = mAddEditNoteViewModel

        mAddEditNoteBinding.swipeRefreshLayout.isEnabled = false
        mAddEditNoteBinding.swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary, R.color.colorAccent
        )

        setSupportActionBar(mAddEditNoteBinding.toolbar)
        mAddEditNoteBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val noteId = intent.getIntExtra(EXTRA_NOTE_ID, -1)
        if (noteId != -1)
            mAddEditNoteViewModel.loadNote(noteId)

        observeFab()
        observeSnackbar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_edit_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun observeFab() {
        mAddEditNoteViewModel.getInsertNoteEvent().observe(this,
                Observer { onBackPressed() })
    }

    private fun observeSnackbar() {
        mAddEditNoteViewModel.getSnackbar().observe(this,
                Observer {
                    Snackbar.make(
                            mAddEditNoteBinding.root, getString(it!!),
                            Snackbar.LENGTH_LONG
                    ).show()
                })
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        App.getApp(applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }

}
