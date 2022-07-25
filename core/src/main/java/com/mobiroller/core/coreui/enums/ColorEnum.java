package com.mobiroller.core.coreui.enums;

import com.mobiroller.core.coreui.Theme;

public enum ColorEnum {

    Primary(Theme.primaryColor, 0),
    Secondary(Theme.secondaryColor, 1),
    Dark(Theme.darkColor, 2),
    Header(Theme.headerColor, 3),
    Text(Theme.textColor, 4),
    Paragraph(Theme.paragraphColor, 5),
    ButtonPrimary(Theme.buttonPrimaryColor, 6),
    TextPrimary(Theme.textPrimaryColor, 7),
    ImageTintPrimary(Theme.imagePrimaryColor, 8),
    TextSecondary(Theme.textSecondaryColor, 9);

    private int resId;
    private int resOrder;

    ColorEnum(int resId, int resOrder) {
        this.resId = resId;
        this.resOrder = resOrder;
    }

    public int getResId() {
        return resId;
    }

    public int getResOrder() {
        return resOrder;
    }

    public static int getResIdByResOrder(int order){
        for(ColorEnum e : ColorEnum.values()){
            if(order == e.resOrder) return e.getResId();
        }
        return 2;
    }

}
