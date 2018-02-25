package ca.ualberta.cs.w18t11.whoselineisitanyway;

final class EmailAddress
{
    private final String localPart;

    EmailAddress(final String localPart, final String domain)
    {
        this.localPart = localPart;
    }

    final String getLocalPart()
    {
        return this.localPart;
    }
}
