package ca.ualberta.cs.w18t11.whoselineisitanyway.model.user;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a phone number.
 *
 * @author Samuel Dolha
 * @version 3.0
 */
public final class PhoneNumber implements Serializable
{
    /**
     * A string representation of the expected format of a phone number.
     */
    public static final String FORMAT = "+### (###) ###-####";

    /**
     * An auto-generated, unique ID to support class versioning for Serializable.
     *
     * @see Serializable
     */
    private static final long serialVersionUID = 4774107513379063750L;

    /**
     * The country code of the phone number.
     */
    private final int countryCode;

    /**
     * The area code of the phone number.
     */
    private final int areaCode;

    /**
     * The exchange code of the phone number.
     */
    private final int exchangeCode;

    /**
     * The line number of the phone number.
     */
    private final int lineNumber;

    /**
     * @param countryCode  The country code of the phone number.
     * @param areaCode     The area code of the phone number.
     * @param exchangeCode The exchange code of the phone number.
     * @param lineNumber   The line number of the phone number.
     */
    public PhoneNumber(final int countryCode,
                       final int areaCode,
                       final int exchangeCode,
                       final int lineNumber)
    {
        this.countryCode = countryCode;
        this.areaCode = areaCode;
        this.exchangeCode = exchangeCode;
        this.lineNumber = lineNumber;
    }

    /**
     * @param phoneNumber The string representation.
     * @return A phone number corresponding to the string representation.
     * @throws IllegalArgumentException For a malformed string representation.
     */
    public static PhoneNumber fromString(@NonNull final String phoneNumber)
    {
        final Matcher matcher = Pattern.compile("^\\+(\\d{1,3}) \\((\\d{3})\\) (\\d{3})-(\\d{4})$")
                .matcher(phoneNumber);

        if (!matcher.matches())
        {
            throw new IllegalArgumentException(
                    String.format(Locale.getDefault(), "phoneNumber must have the form %s",
                            PhoneNumber.FORMAT));
        }

        return new PhoneNumber(Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4)));
    }

    /**
     * @return The country code of the phone number.
     */
    public final int getCountryCode()
    {
        return this.countryCode;
    }

    /**
     * @return The area code of the phone number.
     */
    public final int getAreaCode()
    {
        return this.areaCode;
    }

    /**
     * @return The exchange code of the phone number.
     */
    public final int getExchangeCode()
    {
        return this.exchangeCode;
    }

    /**
     * @return The line number of the phone number.
     */
    public final int getLineNumber()
    {
        return this.lineNumber;
    }

    /**
     * @return A string representation of the phone number.
     * @see Object
     */
    @Override
    public final String toString()
    {
        return "+" + this.countryCode + " (" + this.areaCode + ") "
                + this.exchangeCode + "-" + this.lineNumber;
    }

    /**
     * @return A hashcode of the phone number.
     * @see Object
     */
    @Override
    public final int hashCode()
    {
        return Objects.hash(this.countryCode, this.areaCode, this.exchangeCode, this.lineNumber);
    }

    /**
     * @param object The object with which to compare the phone number.
     * @return Whether the object represents the same phone number.
     * @see Object
     */
    @Override
    public final boolean equals(final Object object)
    {
        if (!(object instanceof PhoneNumber))
        {
            return false;
        }

        if (object == this)
        {
            return true;
        }

        PhoneNumber phoneNumber = (PhoneNumber) object;

        return new EqualsBuilder().append(this.countryCode, phoneNumber.getCountryCode())
                .append(this.areaCode, phoneNumber.getAreaCode())
                .append(this.exchangeCode, phoneNumber.getExchangeCode())
                .append(this.lineNumber, phoneNumber.getLineNumber()).isEquals();
    }
}
