package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Test;

import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.TextValidator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Lucas Thalen
 *         Tests for the TextValidator model class
 */

public final class TextValidatorTest
{
    final TextValidator validator = new TextValidator();

    @Test
    // This checks that a complete phone number can be deconstructed
    public void phoneNum_CompleteReturnsMatches()
    {

        String phone1 = "+1 (780) 432-9926";
        ArrayList<String> res = validator.validatePhoneNumber(phone1, false).getComponents();
        assertTrue(res.size() == 5);
        assertEquals(res.get(1), "1");
        assertEquals(res.get(2), "780");
        assertEquals(res.get(3), "432");
        assertEquals(res.get(4), "9926");
    }

    @Test
    // This checks that partial phone numbers allowed partialmatching will not fail
    public void phoneNum_PartialIncompleteCorrectFormat()
    {
        String phone1 = "+1 (780) 4";
        String phone2 = "";
        String phone3 = "+1 ";

        assertEquals(phone1, 1, validator.validatePhoneNumber(phone1, true).getErrorCode());
        assertEquals(phone2, 1, validator.validatePhoneNumber(phone2, true).getErrorCode());
        assertEquals(phone3, 1, validator.validatePhoneNumber(phone3, true).getErrorCode());

    }

    @Test
    // This tests front and back of a string phone number
    public void phoneNum_PartialWrongEnds()
    {
        String phone1 = ".1 (780) 111-1111";
        String phone2 = "+1 (909) 111-111.";

        assertEquals(phone1, -1, validator.validatePhoneNumber(phone1, true).getErrorCode());
        assertEquals(phone2, -1, validator.validatePhoneNumber(phone2, true).getErrorCode());
    }

    @Test
    // This checks that partial matching still fails incorrect results
    public void phoneNum_PartialIncompleteWrongFormat()
    {
        String phone1 = "+1 (780. 111-111";
        String phone2 = "780 111-111";
        String phone3 = "+1 (780)111-111";

        assertEquals(phone1, -1, validator.validatePhoneNumber(phone1, true).getErrorCode());
        assertEquals(phone2, -1, validator.validatePhoneNumber(phone2, true).getErrorCode());
        assertEquals(phone3, -1, validator.validatePhoneNumber(phone3, true).getErrorCode());
    }

    @Test
    // This checks that partial matching returns on correct format for complete strings
    public void phoneNum_PartialCorrectFormat()
    {
        String phone1 = "+22 (627) 111-2222";
        String phone2 = "+1 (780) 432-3399";

        assertEquals(phone1, 1, validator.validatePhoneNumber(phone1, true).getErrorCode());
        assertEquals(phone2, 1, validator.validatePhoneNumber(phone2, true).getErrorCode());
    }

    @Test
    // This means partial is off, tests for incomplete strings but correct format. Should throw an error.
    public void phoneNum_NoPartialIncompleteCorrectFormat()
    {
        String phone1 = "+1 (780) 4";
        String phone2 = "";
        String phone3 = "+1 ";

        assertEquals(phone1, -1, validator.validatePhoneNumber(phone1, false).getErrorCode());
        assertEquals(phone2, -1, validator.validatePhoneNumber(phone2, false).getErrorCode());
        assertEquals(phone3, -1, validator.validatePhoneNumber(phone1, false).getErrorCode());
    }

    @Test
    // Turn off partial and supply it with an incorrect, incomplete format; should throw error
    public void phoneNum_NoPartialIncompleteWrongFormat()
    {
        String phone1 = "+1 .780";
        String phone2 = "+l";
        String phone3 = "+1 )780) 111-1.11";

        assertEquals(phone1, -1, validator.validatePhoneNumber(phone1, false).getErrorCode());
        assertEquals(phone2, -1, validator.validatePhoneNumber(phone2, false).getErrorCode());
        assertEquals(phone3, -1, validator.validatePhoneNumber(phone1, false).getErrorCode());

    }

    @Test
    // No partial matching, test complete and correct formats
    public void phoneNum_NoPartialCompleteCorrectFormat()
    {
        String phone1 = "+1 (780) 432-9999";
        String phone2 = "+22 (909) 861-2245";

        assertEquals(phone1, 1, validator.validatePhoneNumber(phone1, false).getErrorCode());
        assertEquals(phone2, 1, validator.validatePhoneNumber(phone2, false).getErrorCode());
    }

    @Test
    public void email_PartialIncompleteWrongFormat()
    {
        String email1 = "mt@hurn@live.c";
        String email2 = "att.ne@@t";

        assertEquals(email1, -2, validator.validateEmail(email1, true).getErrorCode());
        assertEquals(email2, -2, validator.validateEmail(email2, true).getErrorCode());
    }

    @Test
    // Test a variety of email types to see that it works
    public void email_PartialCompleteCorrectFormat()
    {
        ArrayList<String> emailList = new ArrayList<String>();
        // Note: Randomly Generated, with a few stoppers thrown in
        emailList.add("bjoern@me.com");
        emailList.add("attwood@aol.com");
        emailList.add("mrdvt@msn.com");
        emailList.add("sfoskett@yahoo.ca");
        emailList.add("netsfr@live.com");
        emailList.add("tokuhirom@optonline.net");
        emailList.add("osaru@verizon.net");
        emailList.add("neonatus@optonline.net");
        emailList.add("bri-ckbat@hotmail.com");
        emailList.add("yangyan@hotmail.com");
        emailList.add("rhavyn@yahoo.com");
        emailList.add("mwitte@att.net");
        emailList.add("pkilab@mac.com");
        emailList.add("wbarker@sbcglobal.net");
        emailList.add("gr+dschl@yahoo.com");
        emailList.add("damian@1234g.com");
        emailList.add("do.ne.v@yahoo.ca");
        emailList.add("zeller@hotmail.com");
        emailList.add("isaacson@msn.com");
        emailList.add("cosimo@comcast.net");
        emailList.add("miltchev@msn.com");
        emailList.add("jonas@me.com");
        emailList.add("sriha@gmail.com");
        emailList.add("hakim@hotmail.com");
        emailList.add("fudrucker@aol.com");
        emailList.add("naupa@hotmail.com");
        emailList.add("djupedal@mac.126");
        emailList.add("kawasaki@msn.com");
        emailList.add("tellis@aol.com");
        emailList.add("bjoern@msn.com");

        for (int i = 0; i < emailList.size(); i++)
        {
            assertEquals(emailList.get(i), 1,
                    validator.validateEmail(emailList.get(i), true).getErrorCode());
        }
    }

    @Test
    public void email_PartialIncompleteCorrectFormat()
    {
        ArrayList<String> emailList = new ArrayList<String>();
        // Note: Randomly Generated, with a few stoppers thrown in
        emailList.add("bjoern@");
        emailList.add("attwood@aol.");
        emailList.add("m.r.dv.t@msn.com");
        emailList.add("sfos123kett@yaho12233oa");


        for (int i = 0; i < emailList.size(); i++)
        {
            assertEquals(emailList.get(i), 1,
                    validator.validateEmail(emailList.get(i), true).getErrorCode());
        }
    }

    @Test
    public void email_CompleteReturnsMatches()
    {

        String email1 = "testing@supreme.net";
        ArrayList<String> res = validator.validateEmail(email1, false).getComponents();
        assertTrue(res.size() == 3);
        assertEquals(res.get(1), "testing");
        assertEquals(res.get(2), "supreme.net");
    }

    @Test
    public void currency_PartialCompleteCorrectFormat()
    {
        String curr1 = "$0.99";
        String curr2 = "0.9";
        String curr3 = "$236";
        String curr4 = "3012.66";

        assertEquals(curr1, 1, validator.validateCurrency(curr1, true).getErrorCode());
        assertEquals(curr2, 1, validator.validateCurrency(curr2, true).getErrorCode());
        assertEquals(curr3, 1, validator.validateCurrency(curr3, true).getErrorCode());
        assertEquals(curr4, 1, validator.validateCurrency(curr4, true).getErrorCode());
    }

    @Test
    public void currency_PartialIncompleteCorrectFormat()
    {
        String curr1 = "$0.";
        String curr2 = "0.9";
        String curr3 = "$";
        String curr4 = "";

        assertEquals(curr1, 1, validator.validateCurrency(curr1, true).getErrorCode());
        assertEquals(curr2, 1, validator.validateCurrency(curr2, true).getErrorCode());
        assertEquals(curr3, 1, validator.validateCurrency(curr3, true).getErrorCode());
        assertEquals(curr4, 1, validator.validateCurrency(curr4, true).getErrorCode());
    }

    @Test
    // Ensure currency returns a deconstructed string which can then be cast to a double by receiver
    public void currency_CompleteReturnsMatches()
    {
        String curr1 = "$0.99";
        ArrayList<String> res = validator.validateCurrency(curr1, false).getComponents();
        assertTrue(res.size() == 2);
        assertEquals(res.get(1), "0.99");

    }

    @Test
    // make sure that an error distinct from wrong-format throws when a decimal is used without supplied decimalized values
    public void currency_CompleteDecimalFail()
    {
        String curr1 = "996.";
        assertEquals(curr1, -2, validator.validateCurrency(curr1, false).getErrorCode());

    }
}