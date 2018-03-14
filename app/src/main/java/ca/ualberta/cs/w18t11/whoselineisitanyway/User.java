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

    private EmailAddress emailAddress;

    private PhoneNumber phoneNumber;

    private final Manager<Task> requestedTasks = new Manager<>();

    private final Manager<Task> assignedTasks = new Manager<>();

    /**
     * Constructor for creating a User object.
     * Error checking for validity of private member variables is handled externally
     * @param emailAddress user's email address, as an EmailAddress object
     * @param phoneNumber user's phone number, as a PhoneNumber object
     * @param username user's username, a.k.a. their id
     * @see EmailAddress
     * @see PhoneNumber
     */
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

    /**
     * Get the email address of the User
     * @see EmailAddress
     * @return EmailAddress object representation of User's email address
     */
    final EmailAddress getEmailAddress()
    {
        return this.emailAddress;
    }

    /**
     * Get the phone number of the User
     * @see PhoneNumber
     * @return PhoneNumber object representation of the User's phone number
     */
    final PhoneNumber getPhoneNumber()
    {
        return this.phoneNumber;
    }

    /**
     * Get the list of the User's requested tasks
     * @see Manager
     * @return Manager containing a list of Task objects
     */
    final Manager<Task> getRequestedTasks()
    {
        return this.requestedTasks;
    }

    /**
     * Get the list of the User's assigned tasks
     * @see Manager
     * @return Manager containing a list of Task objects
     */
    final Manager<Task> getAssignedTasks()
    {
        return this.assignedTasks;
    }
}
