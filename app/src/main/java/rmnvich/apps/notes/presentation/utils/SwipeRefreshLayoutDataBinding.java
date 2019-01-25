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

    @BindingAdapter("android:onRefreshTrash")
    public static void setSwipeRefreshLayoutOnRefreshTrashListener(
            SwipeRefreshLayout view,
            final TrashViewModel viewModel) {
        view.setOnRefreshListener(viewModel::forceUpdate);
    }

    @BindingAdapter("android:onRefreshTags")
    public static void setSwipeRefreshLayoutOnRefreshTagsListener(
            SwipeRefreshLayout view,
            final DashboardTagsViewModel viewModel) {
        view.setOnRefreshListener(viewModel::forceUpdate);
    }
}
