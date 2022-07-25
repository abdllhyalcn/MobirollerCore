package com.mobiroller.core.models.youtube;

import java.io.Serializable;

/**
 * Created by ealtaca on 15.05.2017.
 */

public class VideoFeaturedHeader implements Serializable{

    private ItemDetail itemDetail;

    public VideoFeaturedHeader(ItemDetail itemDetail) {
        this.itemDetail = itemDetail;
    }

    public ItemDetail getItemDetail() {
        return itemDetail;
    }

    public void setItemDetail(ItemDetail itemDetail) {
        this.itemDetail = itemDetail;
    }
}
