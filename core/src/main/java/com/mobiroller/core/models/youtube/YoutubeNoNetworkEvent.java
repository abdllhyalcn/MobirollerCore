package com.mobiroller.core.models.youtube;

import com.mobiroller.core.coreui.models.ScreenModel;

public class YoutubeNoNetworkEvent {
    public ScreenModel screenModel;
    public String screenId;

    public YoutubeNoNetworkEvent(ScreenModel screenModel, String screenId) {
        this.screenModel = screenModel;
        this.screenId = screenId;
    }
}
