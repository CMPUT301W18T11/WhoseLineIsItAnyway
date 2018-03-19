package ca.ualberta.cs.w18t11.whoselineisitanyway.model.validator;

import java.util.ArrayList;

/**
 * <h1>ValidatorResult</h1>
 * Since textvalidator covers a few useful pieces of data in its work and is designed as a single point
 * of failure for text formats to ensure consistency and easy fixing; no need to repeat work in the calling
 * class. Keep data already created during execution and package it up to allow a single return value
 *
 * @author Lucas Thalen
 * @see Validator
 */

public final class ValidatorResult
{
    private int ERRORCODE;
    private ArrayList<String> MATCHGROUPS = new ArrayList<String>();
    private String ERRMSG;

    /**
     * Constructor; takes injected values from Validator and allows user access to data that is useful
     * but was created during the validation process
     *
     * @param err     The errorcode, should one want to parse it differently than default options
     * @param matches These are the components found in regex groups, such as user and domain in an email
     * @param errMSG  This is an error message that can be used and tacked onto things like edtxtviews
     */
    ValidatorResult(final int err, final ArrayList<String> matches, final String errMSG)
    {
        ERRORCODE = err;
        MATCHGROUPS = matches;
        ERRMSG = errMSG;
    }

    public boolean isError()
    {
        return !(ERRORCODE == 1);
    }

    public int getErrorCode()
    {
        return ERRORCODE;
    }

    public String getErrorMSG()
    {
        return ERRMSG;
    }

    public ArrayList<String> getComponents()
    {
        return MATCHGROUPS;
    }
}
