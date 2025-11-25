package com.example.todolist.Ui.activity.Lich;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.todolist.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CategoryBottomSheet extends BottomSheetDialogFragment {

    private CategorySelectedListener listener;

    public interface CategorySelectedListener {
        void onCategorySelected(String category);
    }

    public void setCategorySelectedListener(CategorySelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_category_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bắt sự kiện click cho từng mục text
        setupClick(view, R.id.chipWork, "Công việc");
        setupClick(view, R.id.chipPersonal, "Cá nhân");
        setupClick(view, R.id.chipStudy, "Học tập");
        setupClick(view, R.id.chipHealth, "Sức khỏe");
        setupClick(view, R.id.chipOther, "Khác");
    }

    private void setupClick(View parent, int viewId, String categoryName) {
        View item = parent.findViewById(viewId);
        if (item != null) {
            item.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategorySelected(categoryName);
                }
                dismiss();
            });
        }
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }
}