package ca.ualberta.cs.w18t11.whoselineisitanyway.model.user;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detail;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.elastic.Elastic;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.rating.RatingCollector;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.DetailActivity;

/**
 * Represents a user.
 *
 * @author Samuel Dolha
 * @version 3.0
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
     * The user's username.
     */
    @NonNull
    private final String username;
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
    /**
     * The user's rating collector
     *
     * @see RatingCollector
     */
    @NonNull
    private final RatingCollector ratingCollector;
    /**
     * The user's unique ID in Elasticsearch.
     */
    @Nullable
    private String elasticId;

    /**
     * Creates a user without an elastic ID.
     *
     * @param username     The user's username.
     * @param emailAddress The user's email address.
     * @param phoneNumber  The user's phone number.
     * @throws IllegalArgumentException For an empty username.
     * @see EmailAddress
     * @see PhoneNumber
     * @see Task
     */
    public User(@NonNull final String username, @NonNull final EmailAddress emailAddress,
                @NonNull final PhoneNumber phoneNumber)
    {
        if (username.isEmpty())
        {
            throw new IllegalArgumentException("username cannot be empty");
        }

        this.username = username;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.ratingCollector = new RatingCollector();
    }

    /**
     * Creates a user with an elastic ID.
     *
     * @param elasticId    The user's elastic ID.
     * @param username     The user's username.
     * @param emailAddress The user's email address.
     * @param phoneNumber  The user's phone number.
     * @throws IllegalArgumentException For an empty username.
     * @see EmailAddress
     * @see PhoneNumber
     * @see Task
     */
    public User(@NonNull final String elasticId, @NonNull final String username,
                @NonNull final EmailAddress emailAddress, @NonNull final PhoneNumber phoneNumber)
    {
        if (elasticId.isEmpty())
        {
            throw new IllegalArgumentException("elasticId cannot be empty");
        }

        if (username.isEmpty())
        {
            throw new IllegalArgumentException("username cannot be empty");
        }

        this.elasticId = elasticId;
        this.username = username;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.ratingCollector = new RatingCollector();
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
     * @return The user's rating collector
     * @see RatingCollector
     */
    @NonNull
    public final RatingCollector getRatingCollector() { return this.ratingCollector; }

    /**
     * @return The user's elastic ID.
     * @see Elastic
     */
    @Nullable
    @Override
    public final String getElasticId()
    {
        return this.elasticId;
    }

    /**
     * Sets the user's elastic ID.
     *
     * @param id The user's new elastic ID.
     * @see Elastic
     */
    @Override
    public final void setElasticId(@NonNull final String id)
    {
        if (id.isEmpty())
        {
            throw new IllegalArgumentException("id cannot be empty");
        }

        this.elasticId = id;
    }

    /**
     * Shows the user's details.
     *
     * @param detailActivityClass The activity in which to display the details.
     * @param context             The context in which to start the activity.
     * @param <T>                 The type of DetailActivity.
     * @see Detailed
     */
    @Override
    public final <T extends DetailActivity> void showDetails(
            @NonNull final Class<T> detailActivityClass, @NonNull final Context context)
    {
        context.startActivity(getDetailsIntent(detailActivityClass, context));
    }

    /**
     * Get an intent to show the details of the User.
     *
     * @param detailActivityClass The activity in which to display the details.
     * @param context             The context in which to start the activity.
     * @param <T>                 The type of DetailActivity.
     * @see Detailed
     */
    @Override
    public final <T extends DetailActivity> Intent getDetailsIntent(
            @NonNull final Class<T> detailActivityClass, @NonNull final Context context)
    {
        final Intent intent = new Intent(context, detailActivityClass);
        intent.putExtra(Detailed.DETAILS_KEY, (ArrayList<Detail>) Arrays
                .asList(new Detail("username", this.getUsername(), null),
                        new Detail("emailAddress", this.getEmailAddress().toString(), null),
                        new Detail("phoneNUmber", this.getPhoneNumber().toString(), null)));
        intent.putExtra(Detailed.TITLE_KEY, "User");
        return intent;
    }

    /**
     * @return A hashcode for the user.
     * @see Object
     */
    @Override
    public final int hashCode()
    {
        return Objects.hash(this.getUsername(), this.getEmailAddress(), this.getPhoneNumber());
    }

    /**
     * @param object The object with which to compare the user.
     * @return Whether the object represents the same user.
     * @see Object
     */
    @Override
    public final boolean equals(@Nullable final Object object)
    {
        if (!(object instanceof User))
        {
            return false;
        }

        if (object == this)
        {
            return true;
        }

        final User user = (User) object;

        if (this.getElasticId() != null && user.getElasticId() != null)
        {
            return this.getElasticId().equals(user.getElasticId());
        }

        return this.getUsername().equals(user.getUsername());
    }
}
