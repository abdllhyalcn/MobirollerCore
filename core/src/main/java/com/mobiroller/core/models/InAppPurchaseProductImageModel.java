package com.mobiroller.core.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InAppPurchaseProductImageModel implements Serializable,Comparable<InAppPurchaseProductImageModel>{
    @SerializedName("oi")
    public int orderIndex;
    @SerializedName("iu")
    public String imageUrl;
    @SerializedName("updateDate")
    public String updateDate;

    public InAppPurchaseProductImageModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int compareTo(@NonNull InAppPurchaseProductImageModel inAppPurchaseProductImageModel) {
        if(orderIndex>inAppPurchaseProductImageModel.orderIndex)
            return 1;
        else
            return -1;
    }
}
