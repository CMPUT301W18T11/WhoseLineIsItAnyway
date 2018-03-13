package ca.ualberta.cs.w18t11.whoselineisitanyway;

/**
 * Created by lucas on 2018-03-08.
 */

import java.util.regex.*;

public class TextValidator {

    public int validatePhoneNumber(final String input, final boolean allowPartialMatching) {

        Pattern phoneNum = Pattern.compile("^\\+(\\d)+ \\((\\d{3})\\) (\\d{3})\\-(\\d{4})");
        Matcher matches = phoneNum.matcher(input);
        boolean res = matches.matches();

        if (res) { return 1; }
        else {
            if (matches.hitEnd() && allowPartialMatching) {
                return 1;
            } else {
                return -1;
            }
        }
    }

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
                return -1;
            }
        }
    }

    public int validateCurrency(final String input, final boolean allowPartialMatching) {
        Pattern currency = Pattern.compile("^\\$?\\d+(?:\\.\\d{2})?$");
        Matcher matches = currency.matcher(input);
        boolean res = matches.matches();

        if (res) { return 1; }
        else {
            if (matches.hitEnd() && allowPartialMatching) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
