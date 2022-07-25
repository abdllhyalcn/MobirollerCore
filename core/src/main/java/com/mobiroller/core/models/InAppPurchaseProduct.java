package com.mobiroller.core.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class InAppPurchaseProduct implements Serializable,Comparable<InAppPurchaseProduct>{

    @SerializedName("pi")
    public int productId;
    @SerializedName("t")
    public String title;
    @SerializedName("d")
    public String description;
    @SerializedName("vu")
    public String videoUrl;
    @SerializedName("aoti")
    public String oneTimeProductId;
    @SerializedName("asil")
    public String[] subscriptionProductId;
    @SerializedName("ty")
    public int type;
    @SerializedName("oi")
    public int orderIndex;
    @SerializedName("bat")
    public String buyActionText;
    @SerializedName("dat")
    public String detailActionText;
    @SerializedName("dau")
    public String detailActionUrl;
    @SerializedName("pil")
    public List<InAppPurchaseProductImageModel> productImages = new ArrayList<>();
    @SerializedName("sl")
    public List<String> screenList = new ArrayList<>();
    @SerializedName("ia")
    public boolean isActive;
    @SerializedName("updateDate")
    public String updateDate;

    @Override
    public int compareTo(@NonNull InAppPurchaseProduct inAppPurchaseProduct) {
        if(orderIndex>inAppPurchaseProduct.orderIndex)
            return 1;
        else
            return -1;
    }
}
