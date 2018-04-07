package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.TaskDetailActivity;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.UserLoginActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

/**
 * Intent tests for the TaskDetailActivity
 *
 * @author Mark Griffith
 */
@RunWith(AndroidJUnit4.class)
public class TaskDetailActivityTest
{
    String myUsername = "test";
    String otherUsername = "nottest";
    String myTitle = "My Intent Task";
    String otherTitle = "Other Intent Task";
    String myDescription = "This task is for intent testing";
    String otherDescription = "This task is also for intent testing";
    Task myTask = new Task(myUsername, myTitle, myDescription);
    Task otherTask = new Task(otherUsername , otherTitle, otherDescription);
    DataSourceManager DSM;
    @Rule
    public ActivityTestRule<UserLoginActivity> activityRule = new ActivityTestRule<>(
            UserLoginActivity.class);
    private UserLoginActivity loginActivity;

    @Before
    public void init()
    {
        loginActivity = activityRule.getActivity();

        DSM = new DataSourceManager(loginActivity);
        myTask = new Task(myUsername, myTitle, myDescription);
        otherTask = new Task(otherUsername , otherTitle, otherDescription);
        if (DSM.getTask(myUsername, myTitle) != null)
        {
            DSM.removeTask(myTask);
        }
        if (DSM.getTask(otherUsername, otherTitle) != null)
        {
            DSM.removeTask(otherTask);
        }
//
//        DSM.addTask(myTask);
//        DSM.addTask(otherTask);

        User user = DSM.getUser(myUsername);
        if (user == null)
        {
            user = new User(myUsername, new EmailAddress("test", "intent.com"),
                    new PhoneNumber(0, 123, 456, 7890));
            DSM.addUser(user);
        }
        Intents.init();
    }

    @After
    public void release()
    {
        DSM = new DataSourceManager(loginActivity);
        if (DSM.getTask(myUsername, myTitle) != null) { DSM.removeTask(myTask); }
        if (DSM.getTask(otherUsername, otherTitle) != null) { DSM.removeTask(otherTask); }
        Intents.release();
    }

    /**
     * Tests proper rendering of user's requested task
     */
    @Test
    public void testMyTaskRequested()
    {
        if (DSM.getTask(myUsername, myTitle) == null)
        {
//            DSM.removeTask(myTask);
            DSM.addTask(myTask);
        }

        // Login and navigate to 'my tasks'
        onView(withId(R.id.etxt_Username))
                .perform(typeText(myUsername), closeSoftKeyboard());
        onView(withId(R.id.btn_Login))
                .perform(click());
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.my_tasks));

        // Click on my task
        onData(anything()).inAdapterView(withId(R.id.detail_LV)).atPosition(0).perform(click());
        intended(hasComponent(TaskDetailActivity.class.getName()));

        // My Requested tasks should have edit and delete buttons
        onView(withText(R.string.button_edit_task)).check(matches(isDisplayed()));
        onView(withText(R.string.button_delete_task)).check(matches(isDisplayed()));

        DSM.removeTask(myTask);
    }

    /**
     * Tests proper rendering of user's requested task
     */
    @Test
    public void testMyBiddedTask()
    {
        if (DSM.getTask(myUsername, myTitle) == null)
        {
//            DSM.removeTask(myTask);
            DSM.addTask(myTask);
        }
        // Bid on my task
        myTask.setElasticId("12345");
        myTask = myTask.submitBid(
                new Bid(otherUsername,
                        myTask.getElasticId(),
                        new BigDecimal(999.99)
                )
        );
        DSM.addTask(myTask);

        // Login and navigate to 'my tasks'
        onView(withId(R.id.etxt_Username))
                .perform(typeText(myUsername), closeSoftKeyboard());
        onView(withId(R.id.btn_Login))
                .perform(click());
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.my_tasks));

        // Click on my task
        onData(anything()).inAdapterView(withId(R.id.detail_LV)).atPosition(0).perform(click());
        intended(hasComponent(TaskDetailActivity.class.getName()));

        // My bidded tasks should have edit, delete, and accept buttons
        onView(withText(R.string.button_edit_task)).check(matches(isDisplayed()));
        onView(withText(R.string.button_delete_task)).check(matches(isDisplayed()));
        onView(withText(R.string.button_accept_bid)).check(matches(isDisplayed()));

        DSM.removeTask(myTask);
    }

    /**
     * Tests proper rendering of user's assigned task
     */
    @Test
    public void testMyAssignedTask()
    {
        if (DSM.getTask(myUsername, myTitle) == null)
        {
//            DSM.removeTask(myTask);
            DSM.addTask(myTask);
        }

        // Bid on my task
        myTask.setElasticId("12345");
        myTask = myTask.submitBid(
                new Bid(otherUsername,
                        myTask.getElasticId(),
                        new BigDecimal(999.99)
                )
        );
        myTask = myTask.assignProvider(otherUsername);
        DSM.addTask(myTask);

        // Login and navigate to 'my tasks'
        onView(withId(R.id.etxt_Username))
                .perform(typeText(myUsername), closeSoftKeyboard());
        onView(withId(R.id.btn_Login))
                .perform(click());
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.my_tasks));

        // Click on my task
        onData(anything()).inAdapterView(withId(R.id.detail_LV)).atPosition(0).perform(click());
        intended(hasComponent(TaskDetailActivity.class.getName()));

        // My assigned tasks should have complete and unassign buttons
        onView(withText(R.string.button_complete_task)).check(matches(isDisplayed()));
        onView(withText(R.string.button_unassign_task)).check(matches(isDisplayed()));

        DSM.removeTask(myTask);
    }

    /**
     * Tests proper rendering of user's done task
     */
    @Test
    public void testMyDoneTask()
    {
        if (DSM.getTask(myUsername, myTitle) == null)
        {
//            DSM.removeTask(myTask);
            DSM.addTask(myTask);
        }

        // Bid on my task
        myTask.setElasticId("12345");
        myTask = myTask.submitBid(
                new Bid(otherUsername,
                        myTask.getElasticId(),
                        new BigDecimal(999.99)
                )
        );
        myTask = myTask.assignProvider(otherUsername);
        myTask = myTask.markDone();
        DSM.addTask(myTask);

        // Login and navigate to 'my tasks'
        onView(withId(R.id.etxt_Username))
                .perform(typeText(myUsername), closeSoftKeyboard());
        onView(withId(R.id.btn_Login))
                .perform(click());
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.my_tasks));

        // Click on my task
        onData(anything()).inAdapterView(withId(R.id.detail_LV)).atPosition(0).perform(click());
        intended(hasComponent(TaskDetailActivity.class.getName()));

        // My done done task should have
        onView(withText(R.string.button_delete_task)).check(matches(isDisplayed()));

        DSM.removeTask(myTask);
    }
}
