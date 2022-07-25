package com.mobiroller.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ealtaca on 22.03.2017.
 */

public class ValidationUtil {

    // validating email address
    public static boolean isValidEmail(String email) {
        if(email==null)
            return false;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    public static boolean isValidPassword(String pass) {
        return pass != null && pass.length() >= 6;
    }
}
