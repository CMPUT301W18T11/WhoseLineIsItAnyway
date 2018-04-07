package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.content.Intent;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
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
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Intent tests for the CreateModifyTaskActivity
 *
 * @author Mark Griffith
 */
@RunWith(AndroidJUnit4.class)
public class CreateModifyTaskActivityTest
{
    String testUsername;
    String taskTitle;
    String taskDescription;
    LatLng taskLocation;
    Task testTask;
    @Rule
    public ActivityTestRule<UserLoginActivity> activityRule = new ActivityTestRule<>(
            UserLoginActivity.class);
    private UserLoginActivity loginActivity;

    @Before
    public void init()
    {
        Intents.init();
        loginActivity = activityRule.getActivity();

        testUsername = "test";
        taskTitle = "Intent Test Task";
        taskDescription = "This task is for intent testing";
        taskLocation = new LatLng(53.5232, 113.5263);
        testTask = new Task(testUsername, taskTitle, taskDescription);

        DataSourceManager DSM = new DataSourceManager(loginActivity);
        User user = DSM.getUser(testUsername);
        if (user == null)
        {
            user = new User(testUsername, new EmailAddress("test", "intent.com"),
                    new PhoneNumber(0, 123, 456, 7890));
            DSM.addUser(user);
        }
        if (DSM.getTask(testUsername, taskTitle) != null) { DSM.removeTask(testTask); }
    }

    @After
    public void release()
    {
        Intents.release();
    }

    /**
     * Tests creating a task without location or photo
     */
    @Test
    public void CreateTaskBasicTest()
    {
        // Login and get to CreateModifyTaskActivity
        onView(withId(R.id.etxt_Username))
                .perform(typeText(testUsername), closeSoftKeyboard());
        onView(withId(R.id.btn_Login))
                .perform(click());
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.create_task));

        onView(withId(R.id.etxt_Title))
                .perform(replaceText(taskTitle), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Title)).check(matches(withText(taskTitle)));

        onView(withId(R.id.etxt_Description))
                .perform(replaceText(taskDescription), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Description)).check(matches(withText(taskDescription)));

        onView(withId(R.id.btn_Submit))
                .perform(click());

//        intended(hasComponent(DetailedListActivity.class.getName()));
    }

    /**
     * Tests cancelling a task after typing in a title and description
     */
    @Test
    public void CancelTaskCreationTest()
    {
        // Login and get to CreateModifyTaskActivity
        onView(withId(R.id.etxt_Username))
                .perform(typeText(testUsername), closeSoftKeyboard());
        onView(withId(R.id.btn_Login))
                .perform(click());
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.create_task));

        onView(withId(R.id.etxt_Title))
                .perform(replaceText(taskTitle), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Title)).check(matches(withText(taskTitle)));

        onView(withId(R.id.etxt_Description))
                .perform(replaceText(taskDescription), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Description)).check(matches(withText(taskDescription)));

        onView(withId(R.id.btn_Cancel))
                .perform(click());

//        intended(hasComponent(DetailedListActivity.class.getName()));
    }

    /**
     * Tests creating a task with a photo and location
     */
    @Test
    public void CreateTaskWithPhotoTest()
    {
        // Login and get to CreateModifyTaskActivity
        onView(withId(R.id.etxt_Username))
                .perform(typeText(testUsername), closeSoftKeyboard());
        onView(withId(R.id.btn_Login))
                .perform(click());
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.create_task));

        onView(withId(R.id.etxt_Title))
                .perform(replaceText(taskTitle), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Title)).check(matches(withText(taskTitle)));

        onView(withId(R.id.etxt_Description))
                .perform(replaceText(taskDescription), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Description)).check(matches(withText(taskDescription)));

        onView(withId(R.id.btn_UploadImage))
                .perform(click());
        intended(hasAction(Intent.ACTION_GET_CONTENT));
//        Espresso.onView(withContentDescription("")).perform(click());
        // TODO click on image to upload

        onView(withId(R.id.btn_Submit))
                .perform(click());

        // TODO ???
    }

    /**
     * Tests creating a task with a photo and location
     */
    @Test
    public void CreateTaskWithLocationTest()
    {
        // Login and get to CreateModifyTaskActivity
        onView(withId(R.id.etxt_Username))
                .perform(typeText(testUsername), closeSoftKeyboard());
        onView(withId(R.id.btn_Login))
                .perform(click());
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.create_task));

        onView(withId(R.id.etxt_Title))
                .perform(replaceText(taskTitle), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Title)).check(matches(withText(taskTitle)));

        onView(withId(R.id.etxt_Description))
                .perform(replaceText(taskDescription), closeSoftKeyboard());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
        onView(withId(R.id.etxt_Description)).check(matches(withText(taskDescription)));

        // Select a location
        onView(withId(R.id.btn_callLocationDiag))
                .perform(click());
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Google Maps"));
        onView(withContentDescription("Google Map")).perform(click());
        onView(withId(R.id.btn_mapdialog_Save))
                .perform(click());
        intended(hasComponent(CreateModifyTaskActivity.class.getName()));

        onView(withId(R.id.btn_Submit))
                .perform(click());

//        intended(hasComponent(DetailedListActivity.class.getName()));
    }

    /**
     * Tests adding a duplicate photo to the task
     */
//    @Test
//    public void AddDuplicatePhotoTest()
//    {
//
//        // Login and get to CreateModifyTaskActivity
//        onView(withId(R.id.etxt_Username))
//                .perform(typeText(testUsername), closeSoftKeyboard());
//        onView(withId(R.id.btn_Login))
//                .perform(click());
//        onView(withId(R.id.drawer_layout))
//                .check(matches(isClosed(Gravity.LEFT)))
//                .perform(DrawerActions.open());
//        onView(withId(R.id.nav_view))
//                .perform(NavigationViewActions.navigateTo(R.id.create_task));
//
//        onView(withId(R.id.etxt_Title))
//                .perform(replaceText(taskTitle), closeSoftKeyboard());
//        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
//        onView(withId(R.id.etxt_Title)).check(matches(withText(taskTitle)));
//
//        onView(withId(R.id.etxt_Description))
//                .perform(replaceText(taskDescription), closeSoftKeyboard());
//        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
//        onView(withId(R.id.etxt_Description)).check(matches(withText(taskDescription)));
//
////        onView(withId(R.id.btn_UploadImage))
////                .perform(click());
//        // TODO click on image to upload
//        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
//
////        onView(withId(R.id.btn_UploadImage))
////                .perform(click());
//        // TODO click on image to upload
//        intended(hasComponent(CreateModifyTaskActivity.class.getName()));
//
//        onView(withText("The image is a duplicate.")).check(matches(isDisplayed()));
//
//        // TODO ???
//    }

    /**
     * Tests adding a photo and location to a task and clearing them
     */
    @Test
    public void ClearPhotoAndLocationTest()
    {
        // Login and get to CreateModifyTaskActivity
        onView(withId(R.id.etxt_Username))
                .perform(typeText(testUsername), closeSoftKeyboard());
        onView(withId(R.id.btn_Login))
                .perform(click());
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.create_task));

//        onView(withId(R.id.btn_Submit))
//                .perform(click());
        // TODO click on image to upload

        intended(hasComponent(CreateModifyTaskActivity.class.getName()));

        onView(withId(R.id.btn_callLocationDiag))
                .perform(click());
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
        onView(withId(R.id.etxt_Username))
                .check(matches(withText("Location not set")));
    }
}
