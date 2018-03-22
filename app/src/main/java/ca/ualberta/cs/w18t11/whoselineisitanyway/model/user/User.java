package ca.ualberta.cs.w18t11.whoselineisitanyway.model.user;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detail;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.elastic.Elastic;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.DetailActivity;
import io.searchbox.annotations.JestId;

/**
 * Represents a user.
 *
 * @author Samuel Dolha
 * @version 2.0
 */
public final class User implements Detailed, Elastic, Serializable
{
    /**
     * An auto-generated, unique ID to support class versioning for Serializable.
     *
     * @see Serializable
     */
    private static final long serialVersionUID = 5194623147675047167L;

    /**
     * The bid's unique ID in Elasticsearch.
     */
    @Nullable
    private String elasticId;

    /**
     * The user's username.
     */
    @NonNull
    private final String username;


    /**
     * The user's requested tasks.
     *
     * @see Task
     */
    @NonNull
    private final ArrayList<Task> requestedTasks = new ArrayList<>();

    /**
     * The tasks assigned to the user.
     *
     * @see Task
     */
    @NonNull
    private final ArrayList<Task> assignedTasks = new ArrayList<>();

    /**
     * The user's email address.
     *
     * @see EmailAddress
     */
    @NonNull
    private final EmailAddress emailAddress;

    /**
     * The user's phone number.
     *
     * @see PhoneNumber
     */
    @NonNull
    private final PhoneNumber phoneNumber;

    @JestId
    private String id;

    /**
     * @param emailAddress The user's email address.
     * @param phoneNumber  The user's phone number.
     * @param username     The user's username.
     * @throws IllegalArgumentException For an empty username.
     * @see EmailAddress
     * @see PhoneNumber
     * @see Task
     */
    public User(@NonNull final EmailAddress emailAddress, @NonNull final PhoneNumber phoneNumber,
                @NonNull final String username)
    {
        if (username.isEmpty())
        {
            throw new IllegalArgumentException("username cannot be empty");
        }

        this.username = username;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return The user's username.
     */
    @NonNull
    public final String getUsername()
    {
        return this.username;
    }

    /**
     * @return The user's id.
     */
    @NonNull
    public final String getId()
    {
        return this.id;
    }

    /**
     * Set the user's id
     * @param id user id String
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * @return The user's email address.
     * @see EmailAddress
     */
    @NonNull
    public final EmailAddress getEmailAddress()
    {
        return this.emailAddress;
    }

    /**
     * @return The user's phone number.
     * @see PhoneNumber
     */
    @NonNull
    public final PhoneNumber getPhoneNumber()
    {
        return this.phoneNumber;
    }

    /**
     * @return The user's requested tasks.
     * @see Task
     */
    @NonNull
    public final Task[] getRequestedTasks()
    {
        return this.requestedTasks.toArray(new Task[0]);
    }

    /**
     * @return The tasks assigned to the user.
     * @see Task
     */
    @NonNull
    public final Task[] getAssignedTasks()
    {
        return this.assignedTasks.toArray(new Task[0]);
    }

    @Nullable
    @Override
    public final String getElasticId()
    {
        return this.elasticId;
    }

    @Override
    public final void setElasticId(@NonNull final String id)
    {
        if (id.isEmpty())
        {
            throw new IllegalArgumentException("id cannot be empty");
        }

        this.elasticId = id;
    }

    @Override
    public final <T extends DetailActivity> void showDetails(
            @NonNull final Class<T> detailActivityClass, @NonNull final Context context)
    {
        ArrayList<Detail> detailList = new ArrayList<>();
        detailList.add(new Detail("ID", this.getUsername(), null));
        detailList.add(new Detail("Email", getEmailAddress().toString(), null));
        detailList.add(new Detail("Phone Number", getPhoneNumber().toString(), null));

        Intent intent = new Intent(context, detailActivityClass);
        intent.putExtra(Detailed.DETAILS_KEY, detailList);
        intent.putExtra(Detailed.TITLE_KEY, "User");

        context.startActivity(intent);
    }

    /**
     * @return A string representation of the user.
     */
    @Override
    public final String toString()
    {
        return this.getUsername();
    }
}
