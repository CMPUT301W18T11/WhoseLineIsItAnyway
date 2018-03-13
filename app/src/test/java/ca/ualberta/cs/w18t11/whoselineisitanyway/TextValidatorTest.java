package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
/**
 * Created by lucas on 2018-03-09.
 */

public final class TextValidatorTest {
    final TextValidator validator = new TextValidator();

    @Test
    public void phoneNum_PartialIncompleteCorrectFormat() {
        String phone1 = "+1 (780) 4";
        String phone2 = "";
        String phone3 = "+1 ";
        
        assertEquals(phone1, 1, validator.validatePhoneNumber(phone1, true));
        assertEquals(phone2, 1, validator.validatePhoneNumber(phone2, true));
        assertEquals(phone3, 1, validator.validatePhoneNumber(phone3, true));
        
    }
    @Test
    public void phoneNum_PartialWrongEnds() {
        String phone1 = ".1 (780) 111-1111";
        String phone2 = "+1 (909) 111-111.";

        assertEquals(phone1, -1, validator.validatePhoneNumber(phone1, true));
        assertEquals(phone2, -1, validator.validatePhoneNumber(phone2, true));
    }
    @Test
    public void phoneNum_PartialIncompleteWrongFormat() {
        String phone1 = "+1 (780. 111-111";
        String phone2 = "780 111-111";
        String phone3 = "+1 (780)111-111";

        assertEquals(phone1, -1, validator.validatePhoneNumber(phone1, true));
        assertEquals(phone2, -1, validator.validatePhoneNumber(phone2, true));
        assertEquals(phone3, -1, validator.validatePhoneNumber(phone3, true));
    }
    @Test
    public void phoneNum_PartialCorrectFormat() {
        String phone1 = "+22 (627) 111-2222";
        String phone2 = "+1 (780) 432-3399";

        assertEquals(phone1, 1, validator.validatePhoneNumber(phone1, true));
        assertEquals(phone2, 1, validator.validatePhoneNumber(phone2, true));
    }
    @Test
    public void phoneNum_NoPartialIncompleteCorrectFormat() {
        String phone1 = "+1 (780) 4";
        String phone2 = "";
        String phone3 = "+1 ";

        assertEquals(phone1, -1, validator.validatePhoneNumber(phone1, false));
        assertEquals(phone2, -1, validator.validatePhoneNumber(phone2, false));
        assertEquals(phone3, -1, validator.validatePhoneNumber(phone1, false));
    }
    @Test
    public void phoneNum_NoPartialIncompleteWrongFormat() {
        String phone1 = "+1 .780";
        String phone2 = "+l";
        String phone3 = "+1 )780) 111-1.11";

        assertEquals(phone1, -1, validator.validatePhoneNumber(phone1, false));
        assertEquals(phone2, -1, validator.validatePhoneNumber(phone2, false));
        assertEquals(phone3, -1, validator.validatePhoneNumber(phone1, false));
        
    }
    @Test
    public void phoneNum_NoPartialCompleteCorrectFormat() {
        String phone1 = "+1 (780) 432-9999";
        String phone2 = "+22 (909) 861-2245";

        assertEquals(phone1, 1, validator.validatePhoneNumber(phone1, false));
        assertEquals(phone2, 1, validator.validatePhoneNumber(phone2, false));
    }

    @Test
    public void email_PartialIncompleteWrongFormat() {
        String email1 = "mt@hurn@live.c";
        String email2 = "att.ne@@t";

        assertEquals(email1, -1 , validator.validateEmail(email1, true));
        assertEquals(email2, -1 , validator.validateEmail(email2, true));
    }
    @Test
    public void email_PartialCompleteCorrectFormat() {
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

        for (int i = 0; i < emailList.size(); i ++) {
            assertEquals(emailList.get(i), 1, validator.validateEmail(emailList.get(i), true));
        }
    }
    @Test
    public void email_PartialIncompleteCorrectFormat() {
        ArrayList<String> emailList = new ArrayList<String>();
        // Note: Randomly Generated, with a few stoppers thrown in
        emailList.add("bjoern@");
        emailList.add("attwood@aol.");
        emailList.add("m.r.dv.t@msn.com");
        emailList.add("sfos123kett@yaho12233oa");


        for (int i = 0; i < emailList.size(); i ++) {
            assertEquals(emailList.get(i), 1, validator.validateEmail(emailList.get(i), true));
        }
    }
}