package ca.ualberta.cs.w18t11.whoselineisitanyway;

/**
 * Created by lucas on 2018-03-08.
 */

import java.util.regex.*;

public class TextValidator {
    /**
     * Hard/soft match of Date (as string)
     * This variant of Date matching will either allow partial matching or enforce full matching based on LenientMatch
     * it will return false.
     *
     * @param input           string containing a date to be evaluated
     * @param partialMatching Boolean indicating whether full matching is enforced
     * @return True (matches a date) or False (fails regex or is too short)
     */
    public boolean matchCalendarDate(final String input, final boolean partialMatching) {
        String regPatt = "^(0[1-9]|1[0-2])\\/(0[1-9]|1\\d|2\\d|3[0-1])\\/(\\d{4})"; // 0 = month, 1 = day, 2 = year
        Pattern datePattern = Pattern.compile(regPatt);

        Matcher matcher = datePattern.matcher(input);
        boolean res = matcher.matches();
        if (res) {
            int month = Integer.getInteger(matcher.group(0));
            int day = Integer.getInteger(matcher.group(1));
            int year = Integer.getInteger(matcher.group(2));
            if ((month == 4 || month == 6 || month == 9 || month == 1) && day > 30) { return false; }
            if (month == 2 && day == 29 && !(year % 4 == 0)) { return false; }
        }



        return matcher.hitEnd();
    }
}
