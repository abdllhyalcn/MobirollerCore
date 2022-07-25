package com.mobiroller.core.containers;

import com.mobiroller.core.views.PagedDragDropGrid;


public interface OnPageChangedListener {

    /**
     * called when the grid is scrolled to another page
     * @param sender grid
     * @param newPageNumber 0 based
     */
    void onPageChanged(PagedDragDropGrid sender, int newPageNumber);
}
