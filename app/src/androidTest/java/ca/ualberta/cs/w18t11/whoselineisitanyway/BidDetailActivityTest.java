package ca.ualberta.cs.w18t11.whoselineisitanyway;


import android.support.test.espresso.NoMatchingViewException;
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
import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.BidDetailActivity;
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
import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.hasToString;

/**
 * Intent tests for the TaskDetailActivity
 *
 * @author Mark Griffith
 */
@RunWith(AndroidJUnit4.class)
public class BidDetailActivityTest
{
    @Rule
    public ActivityTestRule<UserLoginActivity> activityRule = new ActivityTestRule<>(
            UserLoginActivity.class);

    String myUsername = "test";

    String otherUsername = "nottest";

    String otherUsername2 = "fish";

    String myTitle = "My Intent Task";

    String otherTitle = "Other Intent Task";

    String myDescription = "This task is for intent testing";

    String otherDescription = "This task is also for intent testing";

    Task myTask = new Task(myUsername, myTitle, myDescription);

    Task otherTask = new Task(otherUsername, otherTitle, otherDescription);

    User user;

    User otherUser;

    DataSourceManager DSM;

    private UserLoginActivity loginActivity;

    @Before
    public void init()
    {
        Intents.init();
        loginActivity = activityRule.getActivity();

        DSM = new DataSourceManager(loginActivity);

        Task[] tasks = DSM.getTasks();
        if (tasks != null)
        {
            for (Task task : tasks)
            {
                if (task.getRequesterUsername().equals(myUsername))
                {
                    DSM.removeTask(task);
                }
                if (task.getRequesterUsername().equals(otherUsername))
                {
                    DSM.removeTask(task);
                }
            }
        }
        Bid[] bids = DSM.getBids();
        if (bids != null)
        {
            for (Bid bid : bids)
            {
                if (bid.getProviderUsername().equals(myUsername))
                {
                    DSM.removeBid(bid);
                }
                if (bid.getProviderUsername().equals(otherUsername))
                {
                    DSM.removeBid(bid);
                }
                if (bid.getProviderUsername().equals(otherUsername2))
                {
                    DSM.removeBid(bid);
                }
            }
        }

        user = DSM.getUser(myUsername);
        if (user == null)
        {
            user = new User(myUsername, new EmailAddress("test", "intent.com"),
                    new PhoneNumber(0, 123, 456, 7890));
            DSM.addUser(user);
        }
        otherUser = DSM.getUser(myUsername);
        if (otherUser == null)
        {
            otherUser = new User(otherUsername, new EmailAddress("nottest", "intent.com"),
                    new PhoneNumber(2, 222, 222, 2222));
            DSM.addUser(otherUser);
        }

        try
        {
            onView(withId(R.id.signOut)).perform(click());
        }
        catch (NoMatchingViewException e)
        {
        }

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
        catch (NoMatchingViewException e)
        {
        }
    }

    @After
    public void release()
    {
        DSM = new DataSourceManager(loginActivity);
        ArrayList<Task> myTasks;
        Task[] tasks = DSM.getTasks();
        if (tasks != null)
        {
            for (Task task : tasks)
            {
                if (task.getRequesterUsername().equals(myUsername))
                {
                    DSM.removeTask(task);
                }
                if (task.getRequesterUsername().equals(otherUsername))
                {
                    DSM.removeTask(task);
                }
            }
        }
        Bid[] bids = DSM.getBids();
        if (bids != null)
        {
            for (Bid bid : bids)
            {
                if (bid.getProviderUsername().equals(myUsername))
                {
                    DSM.removeBid(bid);
                }
                if (bid.getProviderUsername().equals(otherUsername))
                {
                    DSM.removeBid(bid);
                }
                if (bid.getProviderUsername().equals(otherUsername2))
                {
                    DSM.removeBid(bid);
                }
            }
        }
        Intents.release();
    }

    /**
     * Tests the rendering of a user's bid
     */
    @Test
    public void testClickOnMyBid()
    {
        // Make task to bid on
        otherTask = new Task(otherUsername, "Bid on Me", otherDescription);
        DSM.removeTask(otherTask);
        DSM.addTask(otherTask);
        otherTask = otherTask.submitBid(
                new Bid(myUsername,
                        otherTask.getElasticId(),
                        new BigDecimal(1)
                ),
                DSM
        );
        DSM.addTask(otherTask);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.my_bids));

        onData(anything())
                .inAdapterView(withId(R.id.detail_LV))
                .atPosition(0)
                .perform(click());
        intended(hasComponent(BidDetailActivity.class.getName()));

        // My bids should have no buttons
        try
        {
            onView(withText(R.string.button_accept_bid)).check(matches(isDisplayed()));
            onView(withText(R.string.button_decline_bid)).check(matches(isDisplayed()));
            fail("No Buttons Should Be Present");
        }
        catch (NoMatchingViewException e)
        {

        }
    }

    /**
     * Tests the rendering of another user's bid on another user's task
     */
    @Test
    public void testClickOnOtherBid()
    {
        otherTask = new Task(otherUsername, "U didn't bid on me", otherDescription);
        DSM.removeTask(otherTask);
        DSM.addTask(otherTask);
        otherTask = otherTask.submitBid(
                new Bid("fish",
                        otherTask.getElasticId(),
                        new BigDecimal(1)
                ),
                DSM
        );
        DSM.addTask(otherTask);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.all_tasks));

        onData(hasToString(startsWith("nottest: U")))
                .inAdapterView(withId(R.id.detail_LV))
                .atPosition(0)
                .perform(click());
        intended(hasComponent(TaskDetailActivity.class.getName()));

        onView(withText(R.string.button_all_bids_task)).perform(click());

        onData(hasToString(startsWith("fish")))
                .inAdapterView(withId(R.id.detail_LV))
                .atPosition(0)
                .perform(click());
        intended(hasComponent(BidDetailActivity.class.getName()));

        // Other bids should have no buttons
        try
        {
            onView(withText(R.string.button_accept_bid)).check(matches(isDisplayed()));
            onView(withText(R.string.button_decline_bid)).check(matches(isDisplayed()));
            fail("No Buttons Should Be Present");
        }
        catch (NoMatchingViewException e)
        {

        }

        DSM.removeTask(otherTask);
    }

    /**
     * Tests the rendering of another user's bid on the user's task
     */
    @Test
    public void testClickABidOnMyTask()
    {
        myTask = new Task(myUsername, "Your Task", myDescription);
        DSM.removeTask(myTask);
        DSM.addTask(myTask);
        myTask = myTask.submitBid(
                new Bid(otherUsername,
                        myTask.getElasticId(),
                        new BigDecimal(1)
                ),
                DSM
        );
        DSM.addTask(myTask);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.my_tasks));

        onData(hasToString(startsWith("test: Y")))
                .inAdapterView(withId(R.id.detail_LV))
                .perform(click());
        intended(hasComponent(TaskDetailActivity.class.getName()));

        onView(withText(R.string.button_all_bids_task)).perform(click());

        onData(hasToString(startsWith("nottest")))
                .inAdapterView(withId(R.id.detail_LV))
                .atPosition(0)
                .perform(click());

        // Bids on my tasks should have an accept and decline button
        onView(withText(R.string.button_accept_bid)).check(matches(isDisplayed()));
        onView(withText(R.string.button_decline_bid)).check(matches(isDisplayed()));

        DSM.removeTask(otherTask);
    }
}
