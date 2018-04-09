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
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.hasToString;

/**
 * Intent tests for placing bids
 *
 * @author Mark Griffith
 */
@RunWith(AndroidJUnit4.class)
public class PlaceBidTest
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
        else
        {
            DSM.addUser(user);
        }
        otherUser = DSM.getUser(myUsername);
        if (otherUser == null)
        {
            otherUser = new User(otherUsername, new EmailAddress("nottest", "intent.com"),
                    new PhoneNumber(2, 222, 222, 2222));
            DSM.addUser(otherUser);
        }
        else
        {
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
     * Tests placing a bid on another user's requested task
     */
    @Test
    public void testPlaceBidOnRequested()
    {
        // Create task to bid on
        otherTask = new Task(otherUsername, "Bid On Me!", otherDescription);
        DSM.removeTask(otherTask);
        DSM.addTask(otherTask);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.all_tasks));

        onData(hasToString(startsWith("nottest: B")))
                .inAdapterView(withId(R.id.detail_LV))
                .atPosition(0)
                .perform(click());
        intended(hasComponent(TaskDetailActivity.class.getName()));

        // Other requested tasks should only have a bid button
        onView(withText(R.string.button_place_bid)).check(matches(isDisplayed()));
        onView(withText(R.string.button_place_bid)).perform(click());

        // Place bid
        onView(withId(R.id.etxt_BidAmount))
                .perform(typeText("1.00"), closeSoftKeyboard());
        onView(withId(R.id.btn_save)).perform(click());

        DSM.removeTask(otherTask);
    }

    /**
     * Tests placing a bid on another user's bidded task
     */
    @Test
    public void testBidOnBiddedTask()
    {
        // Create task to bid on
        otherTask = new Task(otherUsername, "Bid On Me!", otherDescription);
        DSM.removeTask(otherTask);
        DSM.addTask(otherTask);
        otherTask = otherTask.submitBid(
                new Bid(myUsername,
                        otherTask.getElasticId(),
                        new BigDecimal(1)
                ));
        DSM.addTask(otherTask);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.all_tasks));

        onData(hasToString(startsWith("nottest: B")))
                .inAdapterView(withId(R.id.detail_LV))
                .perform(click());
        intended(hasComponent(TaskDetailActivity.class.getName()));

        // Other bidded tasks should only have a bid button
        onView(withText(R.string.button_place_bid)).check(matches(isDisplayed()));
        onView(withText(R.string.button_place_bid)).perform(click());

        // Place bid
        onView(withId(R.id.etxt_BidAmount))
                .perform(typeText("1.00"), closeSoftKeyboard());
        onView(withId(R.id.btn_save)).perform(click());

        DSM.removeTask(otherTask);
    }

    /**
     * Tests clicking the cancel button in the place bid dialog
     */
    @Test
    public void testCancelPlaceBid()
    {
        // Create task to bid on
        otherTask = new Task(otherUsername, "Bid On Me!", otherDescription);
        DSM.removeTask(otherTask);
        DSM.addTask(otherTask);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.all_tasks));

        onData(hasToString(startsWith("nottest: B")))
                .inAdapterView(withId(R.id.detail_LV))
                .atPosition(0)
                .perform(click());
        intended(hasComponent(TaskDetailActivity.class.getName()));

        // Other requested tasks should only have a bid button
        onView(withText(R.string.button_place_bid)).check(matches(isDisplayed()));
        onView(withText(R.string.button_place_bid)).perform(click());

        // Place bid
        onView(withId(R.id.etxt_BidAmount))
                .perform(typeText("1.00"), closeSoftKeyboard());
        onView(withId(R.id.btn_cancel)).perform(click());

        DSM.removeTask(otherTask);
    }
}