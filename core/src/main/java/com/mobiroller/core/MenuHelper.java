package com.mobiroller.core;

import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.models.NavigationItemModel;
import com.mobiroller.core.util.exceptions.IntentException;
import com.mobiroller.core.util.exceptions.UserFriendlyException;

public class MenuHelper {

    public ScreenModel getScreenModel(NavigationItemModel navigationItemModel, ScreenModel screenModel) throws UserFriendlyException, IntentException {
        return SharedApplication.app.getMenuHelper().getScreenModel(navigationItemModel, screenModel);
    }

}
