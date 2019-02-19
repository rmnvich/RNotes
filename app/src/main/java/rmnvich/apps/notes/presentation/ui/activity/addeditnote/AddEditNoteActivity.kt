package rmnvich.apps.notes.presentation.ui.activity.addeditnote

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Lazy
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants.*
import rmnvich.apps.notes.databinding.AddEditNoteActivityBinding
import rmnvich.apps.notes.di.addeditnote.AddEditNoteModule
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.activity.viewimage.ViewImageActivity
import rmnvich.apps.notes.presentation.ui.custom.ColorPickerDialog
import rmnvich.apps.notes.presentation.ui.dialog.DialogMoreActions
import rmnvich.apps.notes.presentation.ui.dialog.DialogTags
import java.util.*
import javax.inject.Inject
import javax.inject.Provider


class AddEditNoteActivity : AppCompatActivity(), ColorPickerDialogListener {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    @Inject
    lateinit var mColorPickerDialog: Provider<ColorPickerDialog.Builder>

    @Inject
    lateinit var mDialogTags: Provider<DialogTags>

    @Inject
    lateinit var mDialogMore: Lazy<DialogMoreActions>

    private val mPermissionDisposable: CompositeDisposable = CompositeDisposable()
    private val mRxPermissions = RxPermissions(this)

    private lateinit var mAddEditNoteViewModel: AddEditNoteViewModel
    private lateinit var mAddEditNoteBinding: AddEditNoteActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAddEditNoteBinding = DataBindingUtil.setContentView(
                this,
                R.layout.add_edit_note_activity
        )
        App.getApp(applicationContext).componentsHolder
                .getComponent(javaClass, AddEditNoteModule(application, this))
                ?.inject(this)
    }

    @Inject
    fun init() {
        mAddEditNoteViewModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(AddEditNoteViewModel::class.java)
        mAddEditNoteBinding.viewmodel = mAddEditNoteViewModel

        setSupportActionBar(mAddEditNoteBinding.toolbar)
        mAddEditNoteBinding.toolbar.setNavigationOnClickListener {
            mAddEditNoteViewModel.insertOrUpdateNote()
        }

        val noteId = intent.getIntExtra(EXTRA_NOTE_ID, -1)
        if (noteId != -1) {
            mAddEditNoteViewModel.getNote(noteId)
            mAddEditNoteBinding.root.isFocusableInTouchMode = true
        }

        mAddEditNoteViewModel.noteIsFavorite =
                intent.getBooleanExtra(EXTRA_FAVORITE_NOTES, false)
        mAddEditNoteViewModel.noteIsLocked =
                intent.getBooleanExtra(EXTRA_LOCKED_NOTE, false)

        observeClickActionMoreEvent()
        observeDeleteTagEvent()
        observeShareNoteEvent()
        observeClickImageEvent()
        observeOnPickImageEvent()
        observeOnBackPressedEvent()
        observeSnackbar()
    }

    private fun observeClickActionMoreEvent() {
        mAddEditNoteViewModel.getActionMoreEvent().observe(this, Observer {
            mDialogMore.get().show(object : DialogMoreActions.DialogMoreCallback {
                override fun onClickPickImage() = requestImagePermissions()

                override fun onClickColor() = showColorPickerDialog()

                override fun onClickDate() = showDatePickerDialog()

                override fun onClickLabel() = showTagsDialog()

                override fun onClickShare() = handleShareNote()
            })
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_edit_note_menu, menu)
        menu?.findItem(R.id.menu_lock_note)?.setIcon(getLockNoteIcon())
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_lock_note -> {
                mAddEditNoteViewModel.noteIsLocked =
                        !mAddEditNoteViewModel.noteIsLocked
                item.setIcon(getLockNoteIcon())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getLockNoteIcon(): Int {
        return if (mAddEditNoteViewModel.noteIsLocked)
            R.drawable.ic_action_pin_inverted
        else R.drawable.ic_action_unpin_inverted
    }

    private fun handleClickImageEvent(imagePath: String) {
        startActivity(
                Intent(this, ViewImageActivity::class.java)
                        .putExtra(EXTRA_IMAGE_PATH, imagePath)
        )
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun handleShareNote() {
        try {
            val drawable = mAddEditNoteBinding.ivNoteImage.drawable as BitmapDrawable
            val bitmap = drawable.bitmap

            mAddEditNoteViewModel.onShareClicked(bitmap)
        } catch (e: TypeCastException) {
            mAddEditNoteViewModel.onShareClicked(null)
        }
    }

    private fun observeDeleteTagEvent() {
        mAddEditNoteViewModel.getDeleteTagEvent().observe(this,
                Observer {
                    mAddEditNoteViewModel.noteTag.set("")
                    mAddEditNoteViewModel.noteTagId = null
                })
    }

    private fun observeShareNoteEvent() {
        mAddEditNoteViewModel.getShareNoteEvent().observe(this,
                Observer {
                    startActivityForResult(
                            Intent.createChooser(it, getString(R.string.send_via)),
                            REQUEST_CODE_SHARE
                    )
                })
    }

    private fun observeClickImageEvent() {
        mAddEditNoteViewModel.getClickImageEvent().observe(this,
                Observer { handleClickImageEvent(it!!) })
    }

    private fun observeOnPickImageEvent() {
        mAddEditNoteViewModel.getPickImageEvent().observe(this,
                Observer { showImageDialog() })
    }

    private fun observeOnBackPressedEvent() {
        mAddEditNoteViewModel.getBackPressedEvent().observe(this,
                Observer {
                    finish()
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                })
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

    private fun requestImagePermissions() {
        mPermissionDisposable.add(
                mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        ?.subscribe { permission ->
                            if (permission) {
                                mAddEditNoteViewModel.showImagePickerDialog()
                            }
                        }!!
        )
    }

    private fun showColorPickerDialog() {
        dismissKeyboard()
        mColorPickerDialog.get().show(this)
    }

    private fun showDatePickerDialog() {
        dismissKeyboard()
        DatePickerDialog(
                this, { _, year, month, day ->
            mAddEditNoteViewModel.onDatePickerDialogClicked(year, month, day)
        }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showImageDialog() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK).setType("image/*")
        startActivityForResult(
                Intent.createChooser(
                        photoPickerIntent, getString(R.string.select_a_file)
                ), REQUEST_CODE_IMAGE
        )
    }

    private fun showTagsDialog() {
        mDialogTags.get().show(object : DialogTags.DialogTagsCallback {
            override fun onClickTag(tag: Tag) {
                mAddEditNoteViewModel.noteTag.set(tag.name)
                mAddEditNoteViewModel.noteTagId = tag.id
            }

            override fun tagsIsEmpty() {
                mAddEditNoteViewModel.tagsIsEmpty()
            }
        })
    }

    private fun dismissKeyboard() {
        val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
                mAddEditNoteBinding.etText.windowToken, 0
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            mAddEditNoteViewModel.onActivityResult(data, requestCode)
        } else if (requestCode == REQUEST_CODE_SHARE) {
            mAddEditNoteViewModel.onActivityResult(null, requestCode)
        }
    }

    override fun onColorSelected(dialogId: Int, color: Int) =
            mAddEditNoteViewModel.noteColor.set(color)

    override fun onDialogDismissed(dialogId: Int) = Unit

    override fun onBackPressed() {
        mAddEditNoteViewModel.insertOrUpdateNote()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPermissionDisposable.clear()
        App.getApp(applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }

}
