package ca.ualberta.cs.w18t11.whoselineisitanyway;

final class User
{
    private final EmailAddress emailAddress;

    private final PhoneNumber phoneNumber;

    User(final EmailAddress emailAddress, final PhoneNumber phoneNumber)
    {
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    final EmailAddress getEmailAddress()
    {
        return this.emailAddress;
    }

    final PhoneNumber getPhoneNumber()
    {
        return this.phoneNumber;
    }
}
