package ca.ualberta.cs.w18t11.whoselineisitanyway;

final class User implements Serializable
{
    @JestId
    private final String DocID;
    
    private final EmailAddress emailAddress;

    private final PhoneNumber phoneNumber;

    private final Manager<Task> requestedTasks = new Manager<>();

    private final Manager<Task> assignedTasks = new Manager<>();

    User(final EmailAddress emailAddress, final PhoneNumber phoneNumber)
    {
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.DocID = emailAddress.getLocalPart() + emailAddress.getDomain();
    }

    final EmailAddress getEmailAddress()
    {
        return this.emailAddress;
    }

    final PhoneNumber getPhoneNumber()
    {
        return this.phoneNumber;
    }

    final Manager<Task> getRequestedTasks()
    {
        return this.requestedTasks;
    }

    final Manager<Task> getAssignedTasks()
    {
        return this.assignedTasks;
    }

    
}
