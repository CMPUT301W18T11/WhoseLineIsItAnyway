package ca.ualberta.cs.w18t11.whoselineisitanyway;

/**
 * Created by lucas on 2018-03-08.
 */

import java.util.ArrayList;
import java.util.regex.*;

public final class TextValidator {

    /**
     * Validate a text field for a phone number of format +[#, ##, ###] (###) ###-####
     * @param input The string of a phone number to be validated
     * @param allowPartialMatching False for matching whole strong, true for changing-text validation
     * @return Integer, error code:
     * 1 = All clear; proceed
     * -1 = Format error (Prompt user to enter correct format with example)
     */
    public TextValidatorResult validatePhoneNumber(final String input, final boolean allowPartialMatching) {

        Pattern phoneNum = Pattern.compile("^\\+(\\d)+ \\((\\d{3})\\) (\\d{3})\\-(\\d{4})");
        Matcher matches = phoneNum.matcher(input);
        boolean res = matches.matches();
        ArrayList<String> results = new ArrayList<String>();

        if (res) {
            for (int i = 0; i < matches.groupCount(); i ++) { results.add(matches.group(i)); }
            return new TextValidatorResult(1, results, "");
        } // if res is true, then it's a whole number and it works
        else {
            if (matches.hitEnd() && allowPartialMatching) { // failure was due to partial match ending
                return new TextValidatorResult(1, results, "");
            } else {
                return new TextValidatorResult(-1, results, "Invalid format. Example: +1 (123) 123-1234"); // failure was due to no valid match or partial match hitting invalid text
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
    public TextValidatorResult validateEmail(final String input, final boolean allowPartialMatching) {
        Pattern eMail = Pattern.compile("^([^@]+)@([^@]+\\.[^@]+)");
        Matcher matches = eMail.matcher(input);
        boolean res = matches.matches();
        ArrayList<String> results = new ArrayList<String>();

        if (res) {
            for (int i = 0; i < matches.groupCount(); i++) {
                results.add(matches.group(i));
            }
            return new TextValidatorResult(1, results, "");
        } else {
            if (matches.hitEnd() && allowPartialMatching) {
                return new TextValidatorResult(1, results, "");
            } else {
                int arrobaCount = 0;
                for (int i = 0; i < input.length(); i++) {
                    if (input.charAt(i) == '@') {
                        arrobaCount++;
                    }
                }
                if (arrobaCount >= 2) {
                    return new TextValidatorResult(-2, results, "invalid format. Use \'@\' only once.\nExample: exemplaremail@example.com");
                } else {
                    return new TextValidatorResult(-1, results, "invalid format. Example: exemplaremail@example.com");
                }
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
    public TextValidatorResult validateCurrency(final String input, final boolean allowPartialMatching) {
        Pattern currency = Pattern.compile("^\\$?\\d+(?:\\.\\d{2})?$");
        Matcher matches = currency.matcher(input);
        boolean res = matches.matches();
        ArrayList<String> results = new ArrayList<String>();

        if (res) {

           if (input.contains(".")) {
               currency = Pattern.compile("^\\$?\\d+(?:\\.\\d{2})$");
               matches = currency.matcher(input);
               res = matches.matches();
               if (res) {
                   return new TextValidatorResult(1, results, "");
               } else {
                   return new TextValidatorResult(-2, results, "Invalid format. If using decimal, enter all values afterwards.\nExample: 4 or 4.10");
               }
           }  else {
               return new TextValidatorResult(1, results, "");
           }
        }
        else {
            if (matches.hitEnd() && allowPartialMatching) {
                return new TextValidatorResult(1, results, "");
            } else {
                return new TextValidatorResult(-1, results, "invalid format. Example, either: 4 OR 4.10\n(Include all values after decimal, if a point is used.");
            }
        }
    }
}
