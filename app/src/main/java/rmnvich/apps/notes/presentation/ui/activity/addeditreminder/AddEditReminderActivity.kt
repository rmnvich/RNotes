package rmnvich.apps.notes.presentation.ui.activity.addeditreminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants
import rmnvich.apps.notes.databinding.AddEditReminderActivityBinding
import rmnvich.apps.notes.di.addeditreminder.AddEditReminderModule
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.dialog.DialogRepeat
import rmnvich.apps.notes.presentation.utils.DateHelper
import java.util.*
import javax.inject.Inject

class AddEditReminderActivity : AppCompatActivity() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mAddEditReminderViewModel: AddEditReminderViewModel
    private lateinit var mAddEditReminderBinding: AddEditReminderActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAddEditReminderBinding = DataBindingUtil.setContentView(
            this,
            R.layout.add_edit_reminder_activity
        )
        App.getApp(applicationContext).componentsHolder
            .getComponent(javaClass, AddEditReminderModule(application))
            ?.inject(this)
    }

    @Inject
    fun init() {
        mAddEditReminderViewModel = ViewModelProviders.of(this, mViewModelFactory)
            .get(AddEditReminderViewModel::class.java)
        mAddEditReminderBinding.viewmodel = mAddEditReminderViewModel

        mAddEditReminderBinding.swipeRefreshLayout.isEnabled = false
        mAddEditReminderBinding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)

        setSupportActionBar(mAddEditReminderBinding.toolbar)
        mAddEditReminderBinding.toolbar.setNavigationOnClickListener { onBackPressed() }

        val reminderId = intent.getIntExtra(Constants.EXTRA_REMINDER_ID, -1)
        if (reminderId != -1) {
            mAddEditReminderViewModel.getReminder(reminderId)
            mAddEditReminderBinding.root.isFocusableInTouchMode = true
        }

        mAddEditReminderViewModel.getCreateClickEvent().observe(this,
            Observer { onBackPressed() })
        mAddEditReminderViewModel.getClickDateEvent().observe(this,
            Observer { showDatePickerDialog(it!!) })
        mAddEditReminderViewModel.getClickTimeEvent().observe(this,
            Observer { showTimePickerDialog(it!!) })
        mAddEditReminderViewModel.getClickRepeatEvent().observe(this,
            Observer { showRepeatTypeDialog(it!!) })

        observeSnackbar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_edit_reminder_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_create_reminder -> {
                mAddEditReminderViewModel.insertOrUpdateReminder()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeSnackbar() {
        mAddEditReminderViewModel.getSnackbar().observe(this,
            Observer {
                Snackbar.make(
                    mAddEditReminderBinding.root, getString(it!!),
                    Snackbar.LENGTH_SHORT
                ).show()
            })
    }

    private fun showDatePickerDialog(date: Long) {
        dismissKeyboard()
        val pickerDialog = DatePickerDialog(
            this, { _, year, month, day ->
                mAddEditReminderViewModel.onDatePickerDialogClicked(year, month, day)
            }, DateHelper.getField(date, Calendar.YEAR),
            DateHelper.getField(date, Calendar.MONTH),
            DateHelper.getField(date, Calendar.DAY_OF_MONTH)
        )
        pickerDialog.datePicker.minDate = DateHelper.getCurrentTimeInMills()
        pickerDialog.show()
    }

    private fun showTimePickerDialog(time: Long) {
        dismissKeyboard()
        TimePickerDialog(
            this, { _, hour, minute ->
                mAddEditReminderViewModel.onTimePickerDialogClicked(hour, minute)
            }, DateHelper.getField(time, Calendar.HOUR_OF_DAY),
            DateHelper.getField(time, Calendar.MINUTE),
            true
        ).show()
    }

    private fun showRepeatTypeDialog(repeatType: Int) {
        dismissKeyboard()
        DialogRepeat(this).show(repeatType) {
            mAddEditReminderViewModel.onRepeatDialogClicked(it)
        }
    }

    private fun dismissKeyboard() {
        val inputMethodManager = getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            mAddEditReminderBinding.etReminderText.windowToken, 0
        )
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
