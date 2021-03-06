package ca.ualberta.cs.w18t11.whoselineisitanyway.model.user;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents an email address.
 *
 * @author Samuel Dolha
 * @version 3.0
 */
public final class EmailAddress implements Serializable
{
    /**
     * A string representation of the expected format of an email address.
     */
    public static final String FORMAT = "user@domain";

    /**
     * Auto-generated, unique ID to support class versioning for Serializable.
     *
     * @see Serializable
     */
    private static final long serialVersionUID = 6005603262449334472L;

    /**
     * The local part of the email address.
     */
    @NonNull
    private final String localPart;

    /**
     * The domain of the email address.
     */
    @NonNull
    private final String domain;

    /**
     * @param localPart The local part of the email address.
     * @param domain    The domain of the email address.
     * @throws IllegalArgumentException For an empty localPart or domain.
     */
    public EmailAddress(@NonNull final String localPart, @NonNull final String domain)
    {
        if (localPart.isEmpty())
        {
            throw new IllegalArgumentException("localPart cannot be empty");
        }

        if (domain.isEmpty())
        {
            throw new IllegalArgumentException("domain cannot be empty");
        }

        this.localPart = localPart;
        this.domain = domain;
    }

    /**
     * @param emailAddress The string representation.
     * @return An email address corresponding to the string representation.
     * @throws IllegalArgumentException For a malformed string representation.
     */
    public static EmailAddress fromString(@NonNull final String emailAddress)
    {
        final Matcher matcher = Pattern.compile("^([^@]+)@([^@]+)$").matcher(emailAddress);

        if (!matcher.matches())
        {
            throw new IllegalArgumentException(
                    String.format(Locale.getDefault(), "emailAddress must have the form %s",
                            EmailAddress.FORMAT));
        }

        return new EmailAddress(matcher.group(1), matcher.group(2));
    }

    /**
     * @return The local part of the email address.
     */
    @NonNull
    public final String getLocalPart()
    {
        return this.localPart;
    }

    /**
     * @return The domain of the email address.
     */
    @NonNull
    public final String getDomain()
    {
        return this.domain;
    }

    /**
     * @return A string representation of the email address.
     * @see Object
     */
    @NonNull
    @Override
    public final String toString()
    {
        return this.localPart + "@" + this.getDomain();
    }

    /**
     * @return A hashcode of the email address.
     * @see Object
     */
    @Override
    public final int hashCode()
    {
        return Objects.hash(this.localPart, this.domain);
    }

    /**
     * @param object The object with which to compare the email address.
     * @return Whether the object represents the same email address.
     * @see Object
     */
    @Override
    public final boolean equals(final Object object)
    {
        if (!(object instanceof EmailAddress))
        {
            return false;
        }

        if (object == this)
        {
            return true;
        }

        final EmailAddress emailAddress = (EmailAddress) object;

        return new EqualsBuilder().append(this.localPart, emailAddress.getLocalPart())
                .append(this.domain, emailAddress.getDomain()).isEquals();
    }
}
