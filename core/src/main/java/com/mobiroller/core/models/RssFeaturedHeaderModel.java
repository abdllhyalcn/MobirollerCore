package com.mobiroller.core.models;

/**
 * Created by ealtaca on 12.04.2017.
 */

public class RssFeaturedHeaderModel {

    private RssModel featuredHeader;

    public RssFeaturedHeaderModel(RssModel featuredHeader) {
        this.featuredHeader = featuredHeader;
    }

    public RssModel getFeaturedHeader() {
        return featuredHeader;
    }

    public void setFeaturedHeader(RssModel featuredHeader) {
        this.featuredHeader = featuredHeader;
    }
}
