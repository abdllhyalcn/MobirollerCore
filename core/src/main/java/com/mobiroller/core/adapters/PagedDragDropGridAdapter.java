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

package com.mobiroller.core.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PagedDragDropGridAdapter  extends BaseAdapter{

    // Automatic child distribution
	public final static int AUTOMATIC = 3; 
	
	// Delete drop zone location TOP
	public final static int TOP = 1;
	
	// Delete drop zone location BOTTOM
	public final static int BOTTOM = 2;
	
	/**
	 * Used to create the paging
	 * 
	 * @return the page count
	 */
	public int pageCount() {
		return 0;
	}

	/**
	 * Returns the count of item in a page
	 * 
	 * @param page index
	 * @return item count for page
	 */
	public int itemCountInPage(int page) {
		return 0;
	}
	
	/**
	 * Returns the view for the item in the page
	 * 
	 * @param page index
	 * @return the view
	 */
	public View view(int page, int index) {
		return null;
	}
	
	/**
	 * The fixed row count (AUTOMATIC for automatic computing)
	 * 
	 * @return row count or AUTOMATIC
	 */
	public int rowCount() {
		return 0;
	}
	
	/**
	 * The fixed column count (AUTOMATIC for automatic computing)
	 * 
	 * @return column count or AUTOMATIC
	 */
	public int columnCount() {
		return 0;
	}

	/**
	 * Prints the layout in Log.d();
	 */
	public void printLayout() {
	}

	/**
	 * Swaps two items in the item list in a page
	 * 
	 * @param pageIndex
	 * @param itemIndexA
	 * @param itemIndexB
	 */
	public void swapItems(int pageIndex, int itemIndexA, int itemIndexB) {
	}

	/**
	 * Moves an item in the page on the left of provided the page
	 * 
	 * @param pageIndex
	 * @param itemIndex
	 */
	public void moveItemToPreviousPage(int pageIndex, int itemIndex) {
	}

	/**
	 * Moves an item in the page on the right of provided the page
	 * 
	 * @param pageIndex
	 * @param itemIndex
	 */
	public void moveItemToNextPage(int pageIndex, int itemIndex) {
	}

	
	/**
	 * deletes the item in page and at position
	 * 
	 * @param pageIndex
	 * @param itemIndex
	 */
	public void deleteItem(int pageIndex, int itemIndex) {
	}

	/** 
	 * Returns the delete drop zone location.  
	 * 
	 * @return TOP or BOTTOM. 
	 */
    public int deleteDropZoneLocation() {
		return 0;
	}

    /**
     * Tells the grid to show or not the remove drop zone when moving an item
     */
    public boolean showRemoveDropZone() {
		return false;
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
}