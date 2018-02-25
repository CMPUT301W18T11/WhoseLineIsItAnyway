package ca.ualberta.cs.w18t11.whoselineisitanyway;

final class EmailAddress
{
    private final String localPart;

    private final String domain;

    EmailAddress(final String localPart, final String domain)
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

    final String getLocalPart()
    {
        return this.localPart;
    }

    final String getDomain()
    {
        return this.domain;
    }

    @Override
    public final String toString()
    {
        return this.localPart + "@" + this.getDomain();
    }
}
