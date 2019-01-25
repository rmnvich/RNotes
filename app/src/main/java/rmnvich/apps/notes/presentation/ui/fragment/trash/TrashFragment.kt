package rmnvich.apps.notes.presentation.ui.fragment.trash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.main_activity.*
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.TrashFragmentBinding
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.activity.main.MainActivity
import rmnvich.apps.notes.presentation.ui.adapter.trash.TrashAdapter
import javax.inject.Inject

class TrashFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    @Inject
    lateinit var mAdapter: TrashAdapter

    private lateinit var mTrashViewModel: TrashViewModel
    private lateinit var mTrashBinding: TrashFragmentBinding

    companion object {
        fun newInstance() = TrashFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mTrashBinding = DataBindingUtil.inflate(inflater,
                R.layout.trash_fragment, container, false)
        mTrashBinding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)

        initRecyclerView()
        initToolbar()

        return mTrashBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.trash_menu, menu)
    }

    private fun initToolbar() {
        (activity as MainActivity).setSupportActionBar(mTrashBinding.trashToolbar)
        mTrashBinding.trashToolbar.setNavigationOnClickListener {
            (activity as MainActivity).drawer_layout.openDrawer(Gravity.START)
        }
        setHasOptionsMenu(true)
    }

    private fun initRecyclerView() {
        val gridLayoutManager = StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)
        gridLayoutManager.gapStrategy = StaggeredGridLayoutManager
                .GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        mTrashBinding.recyclerTrashNotes.layoutManager = gridLayoutManager

        mAdapter.setOnItemClickListener { mTrashViewModel.onClickNote(it) }
        mTrashBinding.recyclerTrashNotes.adapter = mAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mTrashViewModel = ViewModelProviders.of(activity!!, mViewModelFactory)
                .get(TrashViewModel::class.java)
        mTrashBinding.viewmodel = mTrashViewModel

        mTrashViewModel.getNotes(false)?.observe(this,
                Observer<List<Note>> { handleResponse(it!!) })
        observeSnackbar()
    }

    private fun handleResponse(response: List<Note>) {
        mAdapter.setData(response)
    }

    private fun observeSnackbar() {
        mTrashViewModel.getSnackbar().observe(this, Observer {
            Snackbar.make(
                    mTrashBinding.root, getString(it!!),
                    Snackbar.LENGTH_LONG
            ).show()
        })
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}