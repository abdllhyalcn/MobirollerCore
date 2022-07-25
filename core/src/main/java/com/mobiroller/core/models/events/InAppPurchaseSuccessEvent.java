package com.mobiroller.core.models.events;

public class InAppPurchaseSuccessEvent {

    public String screenId;
    public String screenType;
    public boolean isFragment;

    public InAppPurchaseSuccessEvent(String screenId, String screenType, boolean isFragment) {
        this.screenId = screenId;
        this.screenType = screenType;
        this.isFragment = isFragment;
    }
}
