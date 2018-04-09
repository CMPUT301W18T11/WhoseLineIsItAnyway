package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.view.Gravity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.UserLoginActivity;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.UserProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Intent tests for the NavigatorActivity
 *
 * @author Mark Griffith
 */
@RunWith(AndroidJUnit4.class)
public class NavigatorActivityTest
{
    String myUsername = "test";
    @Rule
    public ActivityTestRule<UserLoginActivity> activityRule = new ActivityTestRule<>(
            UserLoginActivity.class);
    private UserLoginActivity loginActivity;
    private DataSourceManager DSM;

    @Before
    public void init()
    {
        loginActivity = activityRule.getActivity();

        DSM = new DataSourceManager(loginActivity);

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
        }
        catch (NoMatchingViewException e) {}
    }

    /**
     * Tests the opening of the navigation drawer
     */
    @Test
    public void testNavigationDrawerOpen()
    {
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed
                .perform(DrawerActions.open()); // Open Drawer
    }

    /**
     * Tests the closing of the navigation drawer
     */
    @Test
    public void testNavigationDrawerClose()
    {
        // Open drawer
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        // Close drawer
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());
    }

    /**
     * Tests clicking the 'All Tasks' option in the navigation drawer
     */
    @Test
    public void testClickAllTasks()
    {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.all_tasks));
    }

    /**
     * Tests clicking the 'My Tasks' option in the navigation drawer
     */
    @Test
    public void testClickMyTasks()
    {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.my_tasks));
    }

    /**
     * Tests clicking the 'Assigned Tasks' option in the navigation drawer
     */
    @Test
    public void testClickAssignedTasks()
    {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.assigned_tasks));
    }

    /**
     * Tests clicking the Nearby Tasks option in the navigation drawer
     */
    @Test
    public void testClickNearbyTasks()
    {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nearby_tasks));
    }

    /**
     * Tests clicking the 'My Bids' option in the navigation drawer
     */
    @Test
    public void testClickMyBids()
    {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.my_bids));
    }

    /**
     * Tests clicking the 'Create Task' option in the navigation drawer
     */
    @Test
    public void testClickCreateTask()
    {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.create_task));
    }

    /**
     * Tests the clicking of the profile, search, and logout buttons
     */
    @Test
    public void testActionButtonOptions()
    {
        Intents.init();

        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        onView(withId(R.id.profile)).perform(click());
        intended(hasComponent(UserProfileActivity.class.getName()));
        mDevice.pressBack();

        onView(withId(R.id.search)).perform(click());

        onView(withId(R.id.signOut)).perform(click());
        intended(hasComponent(UserLoginActivity.class.getName()));

        Intents.release();
    }
}
