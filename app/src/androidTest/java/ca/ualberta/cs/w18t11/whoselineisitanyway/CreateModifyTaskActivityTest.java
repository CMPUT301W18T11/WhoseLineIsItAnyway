package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.Gravity;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.CreateModifyTaskActivity;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.UserLoginActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.fail;

/**
 * Intent tests for the CreateModifyTaskActivity
 *
 * @author Mark Griffith
 */
@RunWith(AndroidJUnit4.class)
public class CreateModifyTaskActivityTest
{
    @Rule
    public ActivityTestRule<UserLoginActivity> activityRule = new ActivityTestRule<>(
            UserLoginActivity.class);

    String myUsername = "test";

    String myTitle = "My Intent Task";

    String myDescription = "This task is for intent testing";

    LatLng taskLocation = new LatLng(53.5232, 113.5263);

    Task myTask;

    User user;

    DataSourceManager DSM;

    private UserLoginActivity loginActivity;

    @Before
    public void init()
    {
        Intents.init();
        loginActivity = activityRule.getActivity();

        myTask = new Task(myUsername, myTitle, myDescription);

        DSM = new DataSourceManager(loginActivity);

        user = DSM.getUser(myUsername);
        if (user == null)
        {
            user = new User(myUsername, new EmailAddress("test", "intent.com"),
                    new PhoneNumber(0, 123, 456, 7890));
            DSM.addUser(user);
        }

        Task[] tasks = DSM.getTasks();
        if (tasks != null)
        {
            for (Task task : tasks)
            {
                if (task.getRequesterUsername().equals(myUsername))
                {
                    DSM.removeTask(task);
                }
            }
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
        Task[] tasks = DSM.getTasks();
        if (tasks != null)
        {
            for (Task task : tasks)
            {
                if (task.getRequesterUsername().equals(myUsername))
                {
                    DSM.removeTask(task);
                }
            }
        }
        Intents.release();
    }

    /**
     * Tests creating a task without location or photo
     */
    @Test
    public void testCreateTaskBasic()
    {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.create_task));

        onView(withId(R.id.etxt_Title))
                .perform(replaceText(myTitle), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Title)).check(matches(withText(myTitle)));

        onView(withId(R.id.etxt_Description))
                .perform(replaceText(myDescription), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Description)).check(matches(withText(myDescription)));

        onView(withId(R.id.btn_Submit))
                .perform(click());
    }

    /**
     * Tests cancelling a task after typing in a title and description
     */
    @Test
    public void testCancelTaskCreation()
    {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.create_task));

        onView(withId(R.id.etxt_Title))
                .perform(replaceText(myTitle), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Title)).check(matches(withText(myTitle)));

        onView(withId(R.id.etxt_Description))
                .perform(replaceText(myDescription), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Description)).check(matches(withText(myDescription)));

        onView(withId(R.id.btn_Cancel))
                .perform(click());
    }

    /**
     * Tests clicking the 'Select Photos' button
     */
    @Test
    public void testClickSelectPhotosButton()
    {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.create_task));

        onView(withId(R.id.etxt_Title))
                .perform(replaceText(myTitle), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Title)).check(matches(withText(myTitle)));

        onView(withId(R.id.etxt_Description))
                .perform(replaceText(myDescription), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Description)).check(matches(withText(myDescription)));

        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        onView(withId(R.id.btn_UploadImage))
                .perform(click());
        try
        {
            Thread.sleep(700);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        try
        {
            UiDevice device = UiDevice.getInstance(getInstrumentation());
            UiObject allowPermissions = device.findObject(new UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .index(1));
            if (allowPermissions.exists())
            {
                allowPermissions.click();
            }
        }
        catch (UiObjectNotFoundException ex)
        {
            fail("Failed to create UiObject");
        }

        try
        {
            Thread.sleep(700);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        mDevice.pressBack();
    }

    /**
     * Tests creating a task with a photo and location
     */
    @Test
    public void testCreateTaskWithLocation()
    {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.create_task));

        onView(withId(R.id.etxt_Title))
                .perform(replaceText(myTitle), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Title)).check(matches(withText(myTitle)));

        onView(withId(R.id.etxt_Description))
                .perform(replaceText(myDescription), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Description)).check(matches(withText(myDescription)));

        // Select a location
        onView(withId(R.id.btn_callLocationDiag))
                .perform(click());
        try
        {
            UiDevice device = UiDevice.getInstance(getInstrumentation());
            UiObject allowPermissions = device.findObject(new UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .index(1));
            if (allowPermissions.exists())
            {
                allowPermissions.click();
            }
        }
        catch (UiObjectNotFoundException ex)
        {
            fail("Failed to create UiObject");
        }

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Google Maps"));
        onView(withContentDescription("Google Map")).perform(click());
        onView(withId(R.id.btn_mapdialog_Save))
                .perform(click());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));

        onView(withId(R.id.btn_Submit))
                .perform(click());
    }

    /**
     * Tests adding a photo and location to a task and clearing them
     */
    @Test
    public void testClearPhotoAndLocation()
    {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.create_task));

        // TODO figure out how to click on the photo
//        onView(withId(R.id.btn_Submit))
//                .perform(click());

        intended(hasComponent(CreateModifyTaskActivity.class.getName()));

        onView(withId(R.id.btn_callLocationDiag))
                .perform(click());
        try
        {
            UiDevice device = UiDevice.getInstance(getInstrumentation());
            UiObject allowPermissions = device.findObject(new UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .index(1));
            if (allowPermissions.exists())
            {
                allowPermissions.click();
            }
        }
        catch (UiObjectNotFoundException ex)
        {
            fail("Failed to create UiObject");
        }

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Google Maps"));
        onView(withContentDescription("Google Map")).perform(click());
        onView(withId(R.id.btn_mapdialog_Save))
                .perform(click());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));

        // Clear photo
        onView(withId(R.id.btn_ClearAllUploadImages))
                .perform(click());

        // Clear location
        onView(withId(R.id.btn_ClearLocation))
                .perform(click());
        onView(withId(R.id.txt_location_set))
                .check(matches(withText("(Location not set)")));

        onView(withId(R.id.btn_Cancel))
                .perform(click());
    }
}
