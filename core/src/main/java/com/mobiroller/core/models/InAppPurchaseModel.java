package com.mobiroller.core.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InAppPurchaseModel implements Serializable {

    @SerializedName("ia")
    public boolean isActive;

    @SerializedName("alk")
    public String androidLicenseKey;

    @SerializedName("p")
    public List<InAppPurchaseProduct> products = new ArrayList<>();

    @SerializedName("updateDate")
    public String updateDate;
}
