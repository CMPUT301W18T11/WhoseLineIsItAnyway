package ca.ualberta.cs.w18t11.whoselineisitanyway.model;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.view.DetailActivity;
import io.searchbox.annotations.JestId;

/**
 * User is a class for storing and managing the information related to any user of the app.
 * A User is identified by a unique id and an email address and phone number.
 * A User may have requested or assigned tasks.
 *
 * @author Samuel Dolha
 * @version 1.0
 */
public final class User implements Detailable, Serializable
{
    @JestId
    private final String id;
    private final Manager<Task> requestedTasks = new Manager<>();
    private final Manager<Task> assignedTasks = new Manager<>();
    private EmailAddress emailAddress;
    private PhoneNumber phoneNumber;

    /**
     * Constructor for creating a User object.
     * Error checking for validity of private member variables is handled externally
     *
     * @param emailAddress user's email address, as an EmailAddress object
     * @param phoneNumber  user's phone number, as a PhoneNumber object
     * @param username     user's username, a.k.a. their id
     * @see EmailAddress
     * @see PhoneNumber
     */
    public User(final EmailAddress emailAddress, final PhoneNumber phoneNumber,
                final String username)
    {
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.id = username;
    }

    /**
     * Get the id associated with the User
     *
     * @return String representation of the User's id
     */
    public final String getID()
    {
        return this.id;
    }

    /**
     * Get the email address of the User
     *
     * @return EmailAddress object representation of User's email address
     * @see EmailAddress
     */
    public final EmailAddress getEmailAddress()
    {
        return this.emailAddress;
    }

    /**
     * Get the phone number of the User
     *
     * @return PhoneNumber object representation of the User's phone number
     * @see PhoneNumber
     */
    public final PhoneNumber getPhoneNumber()
    {
        return this.phoneNumber;
    }

    /**
     * Get the list of the User's requested tasks
     *
     * @return Manager containing a list of Task objects
     * @see Manager
     */
    public final Manager<Task> getRequestedTasks()
    {
        return this.requestedTasks;
    }

    /**
     * Get the list of the User's assigned tasks
     *
     * @return Manager containing a list of Task objects
     * @see Manager
     */
    public final Manager<Task> getAssignedTasks()
    {
        return this.assignedTasks;
    }

    @Override
    public <T extends DetailActivity> void showDetail(Class<T> detailActivityClass, Context context)
    {
        ArrayList<Detail> detailList = new ArrayList<>();
        detailList.add(new Detail("ID", getID()));
        detailList.add(new Detail("Email", getEmailAddress().toString()));
        detailList.add(new Detail("Phone Number", getPhoneNumber().toString()));

        Intent intent = new Intent(context, detailActivityClass);
        intent.putExtra(Detailable.DATA_DETAIL_LIST, detailList);
        intent.putExtra(Detailable.DATA_DETAIL_TITLE, "User");

        context.startActivity(intent);
    }

    /**
     * Provide a string to describe a user
     *
     * @return String representing the user
     */
    @Override
    public String toString()
    {
        return this.getID();
    }
}
