package com.example.mynotepad.Utility;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewItemDecorator extends RecyclerView.ItemDecoration {
    private final int verticalSpaceHeight;

    public RecyclerViewItemDecorator(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    /**
     * Retrieve any offsets for the given item. Each field of ourRect specifies the number of pixels
     * that the itemView should be inset by.
     *
     * @param outRect Rect to receive the output.
     * @param view the Child view to decorate.
     * @param parent RecyclerView this itemDecoration is decorating
     * @param state The current state of RecyclerView.
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
    }
}
