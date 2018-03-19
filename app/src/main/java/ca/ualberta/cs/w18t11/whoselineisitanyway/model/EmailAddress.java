package ca.ualberta.cs.w18t11.whoselineisitanyway.model;

import java.util.Objects;

public final class EmailAddress
{
    private final String localPart;

    private final String domain;

    public EmailAddress(final String localPart, final String domain)
    {
        if (localPart.length() == 0)
        {
            throw new IllegalArgumentException("Email address local part cannot be empty");
        }

        if (domain.length() == 0)
        {
            throw new IllegalArgumentException("Email address domain cannot be empty");
        }

        this.localPart = localPart;
        this.domain = domain;
    }

    public final String getLocalPart()
    {
        return this.localPart;
    }

    public final String getDomain()
    {
        return this.domain;
    }

    @Override
    public final String toString()
    {
        return this.localPart + "@" + this.getDomain();
    }

    @Override
    public final int hashCode()
    {
        return Objects.hash(this.localPart, this.domain);
    }

    @Override
    public final boolean equals(final Object object)
    {
        if (!(object instanceof EmailAddress))
        {
            return false;
        }

        final EmailAddress emailAddress = (EmailAddress) object;

        return this.localPart.equals(emailAddress.getLocalPart())
                && this.domain.equals(emailAddress.getDomain());
    }
}
