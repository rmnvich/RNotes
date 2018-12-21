package rmnvich.apps.notes.presentation.base

interface BaseView {

    fun showProgress()

    fun hideProgress()

    fun showMessage(text: String)
}