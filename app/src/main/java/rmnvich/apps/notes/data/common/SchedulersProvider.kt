package rmnvich.apps.notes.data.common

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulersProvider {

    fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    fun computation(): Scheduler {
        return Schedulers.computation()
    }

    fun io(): Scheduler {
        return Schedulers.io()
    }

    fun newThread(): Scheduler {
        return Schedulers.newThread()
    }

    fun trampoline(): Scheduler {
        return Schedulers.trampoline()
    }
}