/**
 * Copyright 2012 
 * 
 * Nicolas Desjardins  
 * https://github.com/mrKlar
 * 
 * Facilite solutions
 * http://www.facilitesolutions.com/
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mobiroller.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.crashlytics.android.Crashlytics;
import com.mobiroller.core.adapters.PagedDragDropGridAdapter;
import com.mobiroller.core.containers.OnPageChangedListener;
import com.mobiroller.core.containers.PagedContainer;

public class PagedDragDropGrid  extends HorizontalScrollView implements PagedContainer, OnGestureListener {

    private static final int FLING_VELOCITY = 500;
    private int activePage = 0;
    private boolean activePageRestored = false;
    
	private DragDropGrid grid;
	private PagedDragDropGridAdapter adapter;
    private GestureDetector gestureScanner;

    private OnPageChangedListener pageChangedListener;

    public PagedDragDropGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        initPagedScroll();
        initGrid();
    }
 
    public PagedDragDropGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        initPagedScroll();
        initGrid();
    }
 
    public PagedDragDropGrid(Context context) {
        super(context);

        
        initPagedScroll();
        initGrid();
    }
 
	private void initGrid() {
		grid = new DragDropGrid(getContext());
    	addView(grid);
	}
	
    public void initPagedScroll(){
    	setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
    	
    	if (!isInEditMode()) {
    	    gestureScanner = new GestureDetector(getContext(), this);
    	}

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean specialEventUsed = gestureScanner.onTouchEvent(event);
                if(!specialEventUsed && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    int scrollX = getScrollX();                    
                    int onePageWidth = v.getMeasuredWidth();
                    int page = ((scrollX + (onePageWidth/2))/onePageWidth);
                    scrollToPage(page);
                    return true;
                } else {                    
                    return specialEventUsed;
                }
            }
        });
    }
    
    public void setOnPageChangedListener(OnPageChangedListener listener) {
        this.pageChangedListener = listener;
    }
    
    public void setAdapter(PagedDragDropGridAdapter adapter) {
    	this.adapter = adapter;
		grid.setAdapter(adapter);
		grid.setContainer(this);
	}

    public boolean onLongClick(View v) {
        return grid.onLongClick(v);
    }

    public void notifyDataSetChanged() {
        removeAllViews();
        initGrid();
        grid.setAdapter(adapter);
        grid.setContainer(this);
    }

	@Override
	public void scrollToPage(int page) {
        try {
            activePage = page;
            int onePageWidth = getMeasuredWidth();
            int scrollTo = page * onePageWidth;
            smoothScrollTo(scrollTo, 0);

            pageChangedListener.onPageChanged(this, page);
        }catch (Exception e)
        {
            e.printStackTrace();
            Crashlytics.log("PagedDragDropGrid scrollToPage crash!");
        }
	}

	@Override
	public void scrollLeft() {		
		adapter.moveItemToPreviousPage(activePage, 0);
		int newPage = activePage-1;
		if (canScrollToPreviousPage()) {
			scrollToPage(newPage);
		}
	}

	@Override
	public void scrollRight() {
        try {
            adapter.moveItemToNextPage(activePage, 0);
            int newPage = activePage + 1;
            if (canScrollToNextPage()) {
                scrollToPage(newPage);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Crashlytics.log("PagedDragDropGrid scrollRight crash!");
        }
	}

	@Override
	public int currentPage() {
		return activePage;
	}

	@Override
	public void enableScroll() {
		requestDisallowInterceptTouchEvent(false);
	}

	@Override
	public void disableScroll() {
		requestDisallowInterceptTouchEvent(true);
	}

	@Override
	public boolean canScrollToNextPage() {
		int newPage = activePage+1;
		return (newPage < adapter.pageCount());
	}

	@Override
	public boolean canScrollToPreviousPage() {
		int newPage = activePage-1;
		return (newPage >= 0);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
	    super.onLayout(changed, l, t, r, b);	 
	    
	    if (activePageRestored) {
	        activePageRestored = false;
	        scrollToRestoredPage();
	    }
	}

    private void scrollToRestoredPage() {        
        scrollToPage(activePage);
    }
	
    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent evt1, MotionEvent evt2, float velocityX, float velocityY) {   
        if (velocityX < -FLING_VELOCITY) {
            scrollRight();
            return true;
        } else if (velocityX > FLING_VELOCITY) {
            scrollLeft();
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        return false;
    }	
}
