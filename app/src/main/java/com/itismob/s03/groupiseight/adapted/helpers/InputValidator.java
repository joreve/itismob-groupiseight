package com.itismob.s03.groupiseight.adapted.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/** Form validation rules for the Registration and Login screens. Contains no Android code. */
public final class InputValidator {

    public static final int MIN_PASSWORD_LENGTH = 8;
    /** Birthdays are stored as text in this format, e.g. 2001-03-25. */
    public static final String BIRTHDAY_FORMAT = "yyyy-MM-dd";

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$");

    private InputValidator() {
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= MIN_PASSWORD_LENGTH;
    }

    /** True if the text is a real date in yyyy-MM-dd format that is not in the future. */
    public static boolean isValidBirthday(String birthday) {
        if (birthday == null) {
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat(BIRTHDAY_FORMAT, Locale.US);
        format.setLenient(false);
        try {
            Date date = format.parse(birthday);
            return date != null && !date.after(new Date());
        } catch (ParseException e) {
            return false;
        }
    }
}
