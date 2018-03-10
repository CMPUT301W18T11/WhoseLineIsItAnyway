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
    public void partialDate_LeapYear() {


        String leapyear = "02/29";
        String leapyear2 = "02/29/2";

        assertEquals(leapyear, true, validator.matchCalendarDate(leapyear, true));
        assertEquals(leapyear2, true, validator.matchCalendarDate(leapyear2, true));

    }

    @Test
    public void partialDate_WrongFormatIncomplete() {
        ArrayList<String> tests = new ArrayList<String>();
        tests.add("02/25/.");
        tests.add("0.");
        tests.add("01/.");
        tests.add("0/1/");
        tests.add("01/01/2.0");

        for (int i = 0; i < tests.size(); i ++) {
            assertEquals(tests.get(i), false, validator.matchCalendarDate(tests.get(i), true));
        }
    }

    @Test
    public void partialDate_WrongFormatComplete() {
        ArrayList<String> tests = new ArrayList<String>();
        tests.add("01/.2/2018");
        tests.add("0p/01/1970");
        tests.add("0123456789/");

        for (int i = 0; i < tests.size(); i ++) {
            assertEquals(tests.get(i), false, validator.matchCalendarDate(tests.get(i), true));
        }
    }

    @Test
    public void partialDate_CorrectFormatIncomplete() {
        ArrayList<String> tests = new ArrayList<String>();
        tests.add("02/25/");
        tests.add("01");
        tests.add("01/");
        tests.add("01/31/20");

        for (int i = 0; i < tests.size(); i ++) {
            assertEquals(tests.get(i), true, validator.matchCalendarDate(tests.get(i), true));
        }
    }

    @Test
    public void partialDate_WrongFormatFront() {
        ArrayList<String> tests = new ArrayList<String>();
        tests.add("/2/25/");
        tests.add(".1");
        tests.add("/01/");
        tests.add("0-/31/20");

        for (int i = 0; i < tests.size(); i ++) {
            assertEquals(tests.get(i), false, validator.matchCalendarDate(tests.get(i), true));
        }
    }
    @Test
    public void partialDate_WrongFormatEnd() {
        ArrayList<String> tests = new ArrayList<String>();
        tests.add("02/25/....");
        tests.add("01/03/201.");
        tests.add("01/.......");

        for (int i = 0; i < tests.size(); i ++) {
            assertEquals(tests.get(i), false, validator.matchCalendarDate(tests.get(i), true));
        }
    }

    @Test
    public void partialDate_BoundsWrong() {
        ArrayList<String> tests = new ArrayList<String>();
        tests.add("02/32/20");
        tests.add("00/10/20");
        tests.add("13/10/20");
        tests.add("04/31/");
        tests.add("06/31/2");
        tests.add("11/31/2");
        tests.add("09/31/201");

        for (int i = 0; i < tests.size(); i ++) {
            assertEquals(tests.get(i), false, validator.matchCalendarDate(tests.get(i), true));
        }
    }
}