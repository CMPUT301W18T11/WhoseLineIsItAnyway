package ca.ualberta.cs.w18t11.whoselineisitanyway;

final class User
{
    private final EmailAddress emailAddress;

    User(final EmailAddress emailAddress, final PhoneNumber phoneNumber)
    {
        this.emailAddress = emailAddress;
    }

    final EmailAddress getEmailAddress()
    {
        return this.emailAddress;
    }
}
