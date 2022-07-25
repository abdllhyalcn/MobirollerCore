package com.mobiroller.core.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mobiroller.core.R2;
import com.mobiroller.core.adapters.bottomsheet.BottomSheetAdapter;
import com.mobiroller.core.interfaces.bottomsheet.ActionListener;
import com.mobiroller.core.models.bottomsheet.ActionModel;
import com.mobiroller.core.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ActionPickerBottomSheet extends BottomSheetDialogFragment {

    String title;
    List<ActionModel> actionList;
    ActionListener actionListener;
    Unbinder unbinder;

    @BindView(R2.id.recycler_view)
    RecyclerView recyclerView;

    BottomSheetAdapter adapter;

    CoordinatorLayout.Behavior behavior;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_actions_view, null);
        unbinder = ButterKnife.bind(this, contentView);

        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
            ((BottomSheetBehavior) behavior).setHideable(false);
        }

        adapter = new BottomSheetAdapter(actionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if(actionListener != null) {
                    actionListener.actionSelected(position, actionList.get(position));
                }
            }
        });


    }

    public static class Builder {

        private String title;
        private List<ActionModel> actionList;
        private ActionListener actionListener;

        public Builder() {
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setActions(List<ActionModel> actionList) {
            this.actionList = actionList;
            return this;
        }

        public Builder setActionListener(ActionListener actionListener) {
            this.actionListener = actionListener;
            return this;
        }

        public ActionPickerBottomSheet build() {
            ActionPickerBottomSheet sheet = new ActionPickerBottomSheet();
            sheet.title = title;
            sheet.actionList = actionList;
            sheet.actionListener = actionListener;
            return sheet;
        }

        public ActionPickerBottomSheet show(FragmentManager fragmentManager, String tag) {
            ActionPickerBottomSheet actionPickerBottomSheet = build();
            actionPickerBottomSheet.show(fragmentManager, tag);
            return actionPickerBottomSheet;
        }
    }


}
