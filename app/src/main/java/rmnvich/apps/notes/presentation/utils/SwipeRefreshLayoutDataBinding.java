package rmnvich.apps.notes.presentation.utils;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;

import rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes.DashboardNotesViewModel;
import rmnvich.apps.notes.presentation.ui.fragment.dashboardtags.DashboardTagsViewModel;
import rmnvich.apps.notes.presentation.ui.fragment.trash.TrashViewModel;

public class SwipeRefreshLayoutDataBinding {

    @BindingAdapter("android:onRefreshNotes")
    public static void setSwipeRefreshLayoutOnRefreshNotesListener(
            SwipeRefreshLayout view,
            final DashboardNotesViewModel viewModel) {
        view.setOnRefreshListener(viewModel::forceUpdate);
    }
}
