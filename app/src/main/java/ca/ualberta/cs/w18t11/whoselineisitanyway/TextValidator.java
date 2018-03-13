package ca.ualberta.cs.w18t11.whoselineisitanyway;

/**
 * Created by lucas on 2018-03-08.
 */

import java.util.regex.*;

public class TextValidator {

    /**
     * Validate a text field for a phone number of format +[#, ##, ###] (###) ###-####
     * @param input The string of a phone number to be validated
     * @param allowPartialMatching False for matching whole strong, true for changing-text validation
     * @return Integer, error code:
     * 1 = All clear; proceed
     * -1 = Format error (Prompt user to enter correct format with example)
     */
    public int validatePhoneNumber(final String input, final boolean allowPartialMatching) {

        Pattern phoneNum = Pattern.compile("^\\+(\\d)+ \\((\\d{3})\\) (\\d{3})\\-(\\d{4})");
        Matcher matches = phoneNum.matcher(input);
        boolean res = matches.matches();

        if (res) { return 1; } // if res is true, then it's a whole number and it works
        else {
            if (matches.hitEnd() && allowPartialMatching) { // failure was due to partial match ending
                return 1;
            } else {
                return -1; // failure was due to no valid match or partial match hitting invalid text
            }
        }
    }

    /**
     * Validate an email address field to ensure that very basic compliance exists
     * @param input The input string to be evaluated
     * @param allowPartialMatching Turn on for partial (in progress) matching, off for whole-match only
     * @return Integer errorcode:
     * 1 = All clear
     * -1 = Format error (prompt user to enter an email of the correct format)
     * -2 = @ Error, for multiple @ in an email
     */
    // Note: real email is horribly complicated, but this will validate that basic components exist.
    public int validateEmail(final String input, final boolean allowPartialMatching) {
        Pattern eMail = Pattern.compile("^([^@]+)@([^@]+\\.[^@]+)");
        Matcher matches = eMail.matcher(input);
        boolean res = matches.matches();

        if (res) { return 1; }
        else {
            if (matches.hitEnd() && allowPartialMatching) {
                return 1;
            } else {
                int arrobaCount = 0;
                for (int i = 0; i < input.length(); i ++) {
                    if (input.charAt(i) == '@') { arrobaCount ++; }
                }
                if (arrobaCount >= 2) { return -2; } else { return -1; }
            }
        }
    }

    /**
     * Validate currency string before commiting or casting it to other types
     * @param input input string to validate
     * @param allowPartialMatching True for in-progress eval, false for whole-phrase only.
     * @return Intger errorcode:
     * 1 = All clear
     * -1 = Format issue (invalid chars)
     * -2 = Decimal error (a decimal exists, but no input after; prompt for decimal
     */
    public int validateCurrency(final String input, final boolean allowPartialMatching) {
        Pattern currency = Pattern.compile("^\\$?\\d+(?:\\.\\d{2})?$");
        Matcher matches = currency.matcher(input);
        boolean res = matches.matches();

        if (res) {
           if (input.contains(".")) {
               currency = Pattern.compile("^\\$?\\d+(?:\\.\\d{2}){1}$");
               matches = currency.matcher(input);
               res = matches.matches();
               if (res) { return 1; } else { return -2; }
           }
        }
        else {
            if (matches.hitEnd() && allowPartialMatching) {
                return 1;
            } else {
                return -1;
            }
        }
        return -1;
    }
}
