package rmnvich.apps.notes.presentation.ui.activity.addeditnote

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.like.LikeButton
import com.like.OnLikeListener
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants.EXTRA_NOTE_ID
import rmnvich.apps.notes.databinding.AddEditNoteActivityBinding
import rmnvich.apps.notes.di.addeditnote.AddEditNoteModule
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import javax.inject.Inject

class AddEditNoteActivity : AppCompatActivity(), ColorPickerDialogListener, OnLikeListener {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mAddEditNoteViewModel: AddEditNoteViewModel
    private lateinit var mAddEditNoteBinding: AddEditNoteActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAddEditNoteBinding = DataBindingUtil.setContentView(this,
                R.layout.add_edit_note_activity)
        App.getApp(applicationContext).componentsHolder
                .getComponent(javaClass)?.inject(this)
    }

    @Inject
    fun init() {
        mAddEditNoteViewModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(AddEditNoteViewModel::class.java)
        mAddEditNoteBinding.viewmodel = mAddEditNoteViewModel

        mAddEditNoteBinding.starButton.setOnLikeListener(this)

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

        mAddEditNoteViewModel.getDeleteTagEvent().observe(this,
                Observer { mAddEditNoteViewModel.noteTag.set(null) })

        observeFab()
        observeSnackbar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_edit_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_color -> {
                showColorPickerDialog()
                true
            }
            R.id.menu_tag -> {
                val tag = Tag()
                tag.name = "Test tag"
                mAddEditNoteViewModel.noteTag.set(tag)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showColorPickerDialog() {
        ColorPickerDialog.newBuilder()
                .setAllowCustom(false)
                .setShowAlphaSlider(false)
                .setAllowPresets(false)
                .setShowColorShades(false)
                .show(this)
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
                            Snackbar.LENGTH_SHORT
                    ).show()
                })
    }

    override fun liked(button: LikeButton?) =
            mAddEditNoteViewModel.noteFavorite.set(true)

    override fun unLiked(button: LikeButton?) =
            mAddEditNoteViewModel.noteFavorite.set(false)

    override fun onColorSelected(dialogId: Int, color: Int) =
            mAddEditNoteViewModel.noteColor.set(color)

    override fun onDialogDismissed(dialogId: Int) = Unit

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
