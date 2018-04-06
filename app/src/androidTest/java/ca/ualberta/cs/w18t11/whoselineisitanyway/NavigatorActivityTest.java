package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.NavigatorActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Intent tests for the NavigatorActivity
 *
 * @author Mark Griffith
 */
@RunWith(AndroidJUnit4.class)
public class NavigatorActivityTest
{
    @Rule
    public ActivityTestRule<NavigatorActivity> activityRule = new ActivityTestRule<>(
            NavigatorActivity.class);
    private NavigatorActivity navActivity;
    private DataSourceManager DSM;
    private User testUser;

    @Before
    public void init()
    {
        navActivity = activityRule.getActivity();

        DSM = new DataSourceManager(navActivity);
        testUser = new User("bob", new EmailAddress("bob", "gmail.com"),
                new PhoneNumber(0, 123, 456, 7890));
        DSM.removeUser(testUser);
        DSM.addUser(testUser);
        DSM.setCurrentUser(testUser);
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
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Profile")).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Search")).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Logout")).perform(click());
    }
}
