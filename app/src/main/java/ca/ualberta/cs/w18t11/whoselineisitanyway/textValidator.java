package ca.ualberta.cs.w18t11.whoselineisitanyway;

/**
 * Created by lucas on 2018-03-08.
 */

import java.util.regex.*;

public class textValidator {
    /**
     * Hard match of Date (as string)
     * This variant of Date matching will only test a full date and return true. If it is less or not a date
     * it will return false.
     * @param input string containing a date to be evaluated
     * @return True (matches a date) or False (fails regex or is too short)
     */
    public boolean matchCalendarDate(final String input, final boolean lenientMatch) {
        String regPatt = "^(0\\d|1[0-2])\\/(0\\d|1\\d|2\\d|(?<!02\\/)(?:3(?:0|(?<!(\\/04\\/|\\/06\\/|\\/09\\/|\\/11\\/)))))\\/(\\d{4})"; // 0 = month, 1 = day, 2 = year
        Pattern datePattern = Pattern.compile(regPatt);

        Matcher matcher = datePattern.matcher(input);
        boolean res = matcher.matches();
        if (lenientMatch == false) {
            // Check leap-year condition
            if (matcher.hitEnd() && res) {
                int month = Integer.getInteger(m.group(0));
                int year = Integer.getInteger(m.group(2));
                int day = Integer.getInteger(m.group(1));

                if (month == 2 && year % 4 != 0 && day == 29) {
                    return false; // Given Feb 29th without leap year therefore invalid date
                }

            }
            return res; // After checking leap year, date is valid or invalid per normal regex
        } else {
            if (! res) {
                if (! matcher.hitEnd()) {
                    return true;
                }
                else {
                    // Check leap-year condition
                    if (matcher.hitEnd() && res) {
                        int month = Integer.getInteger(matcher.group(0));
                        int year = Integer.getInteger(matcher.group(2));
                        int day = Integer.getInteger(matcher.group(1));

                        if (month == 2 && year % 4 != 0 && day == 29) {
                            return false; // Given Feb 29th without leap year therefore invalid date
                        }

                    }
                    return res; // After checking leap year, date is valid or invalid per normal regex
                }
            }
            else {
                return res;
            }
        }
    }

    /**
     * Soft match of Date (as String)
     * This variant of the function allows partial matching - it will only return false when a
     * calendar expression is broken or regex is not resolved
     * @param input this is the Date as a string to be tested
     * @return boolean True/False based on if the regex matches a Calendar pattern
     */
    public boolean softMatchCalendarDate(final String input) {
        String regPatt = "^(0\\d|1[0-2])\\/(0\\d|1\\d|2\\d|(?<!02\\/)(?:3(?:0|(?<!(\\/04\\/|\\/06\\/|\\/09\\/|\\/11\\/)))))\\/(\\d{4})"; // 0 = month, 1 = day, 2 = year
        Pattern datePattern = Pattern.compile(regPatt);

        Matcher matcher = datePattern.matcher(input);
        boolean res = matcher.matches();


    }
}
