package rmnvich.apps.notes.presentation.ui.fragment.dashboardreminders

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import rmnvich.apps.notes.domain.interactors.dashboardreminders.DashboardRemindersInteractor

class DashboardRemindersViewModel(
        private val mDashboardRemindersInteractor: DashboardRemindersInteractor,
        private val isDoneReminders: Boolean) : ViewModel() {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)

    val bRemindersIsEmpty: ObservableBoolean = ObservableBoolean(false)



    fun addReminder() {

    }
}