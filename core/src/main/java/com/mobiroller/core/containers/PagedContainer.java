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

package com.mobiroller.core.containers;

public interface PagedContainer {

	/**
	 * Scrolls directly to specified page
	 * 
	 * @param page index
	 */
    void scrollToPage(int page);

	
	/**
	 * Scrolls to the page at left of current page
	 */
    void scrollLeft();

	/**
	 * Scrolls to the page at right of current page
	 */
    void scrollRight();
	
	/**
	 * Returns the current displayed page
	 * 
	 * @return page index
	 */
    int currentPage();

	/**
	 * Enable scrolling
	 */
    void enableScroll();

	/**
	 * Disable scrolling
	 */
    void disableScroll();

	/**
	 * Returns true if there is a page at right of current page
	 * 
	 * @return
	 */
    boolean canScrollToNextPage();

	/**
	 * Returns true if there is a page at left of current page
	 * 
	 * @return 
	 */
    boolean canScrollToPreviousPage();
}
