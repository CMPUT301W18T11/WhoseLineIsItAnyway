package ca.ualberta.cs.w18t11.whoselineisitanyway;

import java.util.ArrayList;

/**
 * Created by lucas on 2018-03-13.
 */

public final class TextValidatorResult {
    private int ERRORCODE;
    private ArrayList<String> MATCHGROUPS = new ArrayList<String>();
    private String ERRMSG;

    TextValidatorResult(final int err, final ArrayList<String> matches, final String errMSG) {
        ERRORCODE = err;
        MATCHGROUPS = matches;
        ERRMSG = errMSG;
    }
    public boolean isError() {
        return !(ERRORCODE == 1);
    }
    public int getErrorCode() {
        return ERRORCODE;
    }
    public String getErrorMSG() {
        return ERRMSG;
    }

    public ArrayList<String> getComponents() {
        return MATCHGROUPS;
    }
}
