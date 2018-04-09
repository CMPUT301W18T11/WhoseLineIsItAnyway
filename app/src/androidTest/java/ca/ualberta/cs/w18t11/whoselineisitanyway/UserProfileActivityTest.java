package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.DetailedListActivity;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.UserLoginActivity;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.UserProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Intent tests for UserProfileActivity
 *
 * @author Mark Griffith
 */
@RunWith(AndroidJUnit4.class)
public class UserProfileActivityTest
{
    String myUsername = "test";
    User user;
    DataSourceManager DSM;
    @Rule
    public ActivityTestRule<UserLoginActivity> activityRule = new ActivityTestRule<>(
            UserLoginActivity.class);
    private UserLoginActivity loginActivity;

    @Before
    public void init()
    {
        Intents.init();

        loginActivity = activityRule.getActivity();

        DSM = new DataSourceManager(loginActivity);

        user = DSM.getUser(myUsername);
        if (user == null)
        {
            user = new User(myUsername, new EmailAddress("test", "intent.com"),
                    new PhoneNumber(1, 111, 111, 1111));
            DSM.addUser(user);
        }

        try
        {
            onView(withId(R.id.signOut)).perform(click());
        }
        catch (NoMatchingViewException e) {}

        try
        {
            onView(withId(R.id.etxt_Username))
                    .perform(typeText(myUsername), closeSoftKeyboard());
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
            onView(withId(R.id.btn_Login))
                    .perform(click());
        }
        catch (NoMatchingViewException e) {}
    }
    @After
    public void cleanup()
    {
        Intents.release();
    }

    /**
     * Tests proper rendering of the user profile with location
     */
    @Test
    public void testUserProfile()
    {
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        onView(withId(R.id.profile)).perform(click());
        intended(hasComponent(UserProfileActivity.class.getName()));

        onView(withId(R.id.txt_userName))
                .check(matches(withText(myUsername)));
        onView(withId(R.id.txt_reviewHeader))
                .check(matches(withText("SUMMARY (0 Reviews - Unrated User)")));

    }

    /**
     * Tests closing the user profile
     */
    @Test
    public void testCloseUserProfile()
    {
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        onView(withId(R.id.profile)).perform(click());
        intended(hasComponent(UserProfileActivity.class.getName()));

        onView(withId(R.id.btn_close)).perform(click());
        intended(hasComponent(DetailedListActivity.class.getName()));
    }
}
