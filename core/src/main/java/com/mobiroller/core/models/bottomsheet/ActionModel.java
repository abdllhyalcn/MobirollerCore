package com.mobiroller.core.models.bottomsheet;

public class ActionModel {

    public int id;
    public int iconRes;
    public int titleRes;
    public boolean colorize;

    public ActionModel(int id, int iconRes, int titleRes, boolean colorize) {
        this.id = id;
        this.iconRes = iconRes;
        this.titleRes = titleRes;
        this.colorize = colorize;
    }
}
