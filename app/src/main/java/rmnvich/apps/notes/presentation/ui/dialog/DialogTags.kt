package rmnvich.apps.notes.presentation.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import rmnvich.apps.notes.R
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.interactors.dialogtags.DialogTagsInteractor
import rmnvich.apps.notes.presentation.ui.adapter.tag.DialogTagsAdapter
import rmnvich.apps.notes.presentation.ui.custom.ExpandableBottomSheetDialog
import java.util.*
import java.util.concurrent.TimeUnit

class DialogTags(private val interactor: DialogTagsInteractor,
                 context: Context) : ExpandableBottomSheetDialog(context) {

    interface DialogTagsCallback {
        fun onClickTag(tag: Tag)
    }

    private var mCallback: DialogTagsCallback? = null

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private val mAdapter = DialogTagsAdapter()

    private var etSearch: EditText
    private var progressLayout: LinearLayout

    init {
        val inflater = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)

        @SuppressLint("InflateParams")
        val view = inflater.inflate(R.layout.dialog_tags, null)

        etSearch = view.findViewById(R.id.et_search)
        progressLayout = view.findViewById(R.id.progress_layout)

        mAdapter.setOnTagClickListener {
            mCallback?.onClickTag(it)
            this.dismiss()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.bottom_sheet_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = mAdapter

        this.setContentView(view)
        Objects.requireNonNull(this.window)
                .setSoftInputMode(WindowManager
                        .LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun getSearchDisposable(): Disposable {
        return RxTextView.textChanges(etSearch)
                .skipInitialValue()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { mAdapter.filter.filter(it) }
    }

    private fun getTagsDisposable(): Disposable {
        return interactor.getAllTags()
                .doOnSubscribe { progressLayout.visibility = View.VISIBLE }
                .subscribe({
                    progressLayout.visibility = View.INVISIBLE
                    mAdapter.setData(it)
                }, { this.dismiss() })
    }

    fun show(callback: DialogTagsCallback) {
        if (this.mCallback == null) {
            this.mCallback = callback
        }
        this.show()

        mCompositeDisposable.add(getSearchDisposable())
        mCompositeDisposable.add(getTagsDisposable())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mCompositeDisposable.clear()
    }
}