package rmnvich.apps.notes

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import rmnvich.apps.notes.data.common.SchedulersProvider

class TestSchedulersProvider : SchedulersProvider() {

    private val testScheduler = TestScheduler()

    override fun ui(): Scheduler {
        return testScheduler
    }

    override fun computation(): Scheduler {
        return testScheduler
    }

    override fun io(): Scheduler {
        return testScheduler
    }

    override fun newThread(): Scheduler {
        return testScheduler
    }

    override fun trampoline(): Scheduler {
        return testScheduler
    }

    fun testScheduler(): TestScheduler {
        return testScheduler
    }

}