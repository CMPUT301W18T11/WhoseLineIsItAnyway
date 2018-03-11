package ca.ualberta.cs.w18t11.whoselineisitanyway;

/**
 * User is a class for storing and managing the information related to any user of the app.
 * A User is identified by a unique id and an email address and phone number.
 * A User may have requested or assigned tasks.
 * @author Samuel Dolha
 * @version 1.0
 */
final class User
{
    private final String id;

    private final EmailAddress emailAddress;

    private final PhoneNumber phoneNumber;

    private final Manager<Task> requestedTasks = new Manager<>();

    private final Manager<Task> assignedTasks = new Manager<>();

    User(final EmailAddress emailAddress, final PhoneNumber phoneNumber, final String username)
    {
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.id = username;
    }

    /**
     * Get the id associated with the User
     * @return String representation of the User's id
     */
    final String getID() { return this.id; }
    
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
