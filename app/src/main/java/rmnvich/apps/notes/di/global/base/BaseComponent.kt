package rmnvich.apps.notes.di.global.base

interface BaseComponent<V> {

    fun inject(view: V)
}