package rmnvich.apps.notes.presentation.utils;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes.DashboardNotesViewModel;

public class SwipeRefreshLayoutDataBinding {

    @BindingAdapter("android:onRefresh")
    public static void setSwipeRefreshLayoutOnRefreshListener(SwipeRefreshLayout view,
                                                              final DashboardNotesViewModel viewModel) {
        view.setOnRefreshListener(viewModel::forceUpdate);
    }
}
