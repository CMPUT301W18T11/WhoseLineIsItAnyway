package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.UserLoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Intent tests for the UserLoginActivity
 *
 * @author Mark Griffith
 */
@RunWith(AndroidJUnit4.class)
public class UserLoginActivityTest
{
    @Rule
    public ActivityTestRule<UserLoginActivity> activityRule = new ActivityTestRule<>(
            UserLoginActivity.class);
    private UserLoginActivity loginActivity;
    private String testUsername;
    private DataSourceManager DSM;
    User user;

    @Before
    public void init()
    {
        testUsername = "test";
        loginActivity = activityRule.getActivity();
        DSM = new DataSourceManager(loginActivity);
        if ((user = DSM.getUser(testUsername)) != null)
        {
            DSM.removeUser(user);
            DSM.unsetCurrentUser();
        }
        DSM.unsetCurrentUser();
    }

    @After
    public void cleanup()
    {
        if ((user = DSM.getUser(testUsername)) != null)
            DSM.removeUser(user);
        DSM.unsetCurrentUser();
    }

    /**
     * Tests typing in the username text input
     */
    @Test
    public void testUsernameInput()
    {
        try
        {
            onView(withId(R.id.signOut)).perform(click());
        }
        catch (NoMatchingViewException e) {}

        // Input text
        onView(withId(R.id.etxt_Username))
                .perform(typeText(testUsername), closeSoftKeyboard());

        // Check the typed text is correct
        onView(withId(R.id.etxt_Username))
                .check(matches(withText(testUsername)));
    }

    /**
     * Tests clicking sign in with no username text
     */
    @Test
    public void testBlankLogin()
    {
        try
        {
            onView(withId(R.id.signOut)).perform(click());
        }
        catch (NoMatchingViewException e) {}

        // Click the sign in button
        onView(withId(R.id.btn_Login))
                .perform(click());

        onView(withId(R.id.etxt_Username))
                .check(matches(hasErrorText("This field is required")));
    }

    /**
     * Tests registering a new user
     */
    @Test
    public void testRegisterUser()
    {
        try
        {
            onView(withId(R.id.signOut)).perform(click());
        }
        catch (NoMatchingViewException e) {}

        // Input text
        onView(withId(R.id.etxt_Username))
                .perform(typeText(testUsername), closeSoftKeyboard());

        onView(withId(R.id.btn_Login))
                .perform(click());

        onView(withId(R.id.etxtPhoneNum))
                .perform(typeText("+1 (111) 111-1111"));
        try
        {
            Thread.sleep(700);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        onView(withId(R.id.etxtEmail))
                .perform(typeText("intent@test.com"));
        try
        {
            Thread.sleep(700);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        onView(withId(R.id.btn_OK))
                .perform(click());

        onView(withId(R.id.signOut)).perform(click());
    }

    /**
     * Tests a blank input in the registration dialog
     */
    @Test
    public void testBlankRegisterUser()
    {
        try
        {
            onView(withId(R.id.signOut)).perform(click());
        }
        catch (NoMatchingViewException e) {}

        // Input text
        onView(withId(R.id.etxt_Username))
                .perform(typeText(testUsername), closeSoftKeyboard());

        onView(withId(R.id.btn_Login))
                .perform(click());

        onView(withId(R.id.btn_OK))
                .perform(click());

        onView(withId(R.id.etxtPhoneNum))
                .check(matches(hasErrorText("Invalid format. Example: +1 (123) 123-1234")));

        onView(withId(R.id.etxtEmail))
                .check(matches(hasErrorText("invalid format. Example: exemplaremail@example.com")));
    }

    /**
     * Tests a blank email in the registration dialog
     */
    @Test
    public void testBlankEmailRegisterUser()
    {
        try
        {
            onView(withId(R.id.signOut)).perform(click());
        }
        catch (NoMatchingViewException e) {}

        // Input text
        onView(withId(R.id.etxt_Username))
                .perform(typeText(testUsername), closeSoftKeyboard());

        onView(withId(R.id.btn_Login))
                .perform(click());

        onView(withId(R.id.etxtPhoneNum))
                .perform(typeText("+1 (111) 111-1111"));
        try
        {
            Thread.sleep(700);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        onView(withId(R.id.btn_OK))
                .perform(click());

        onView(withId(R.id.etxtEmail))
                .check(matches(hasErrorText("invalid format. Example: exemplaremail@example.com")));
    }

    /**
     * Tests cancelling a user registration
     */
    @Test
    public void testRegisterUserCancel()
    {
        try
        {
            onView(withId(R.id.signOut)).perform(click());
        }
        catch (NoMatchingViewException e) {}

        // Input text
        onView(withId(R.id.etxt_Username))
                .perform(typeText(testUsername), closeSoftKeyboard());

        onView(withId(R.id.btn_Login))
                .perform(click());

        onView(withId(R.id.btn_cancel))
                .perform(click());
    }
}
