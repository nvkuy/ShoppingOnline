package com.nguyenvukhanhuygmail.shoppingonline.ultil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by toannq on 1/3/2018.
 */

public class CheckEmailType {

    // cấu trúc 1 email thông thường
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher  matcher = pattern.matcher(email);

        return matcher.matches();

    }



}
