package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.controllers.ElasticSearchUserController;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;


/**
 * A login screen that offers login via username.
 */
public class UserLoginActivity extends AppCompatActivity
{
    // A dummy authentication store containing known user names
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "bob", "alice", "eve"
    };

    private EditText txtUsername;
    private View progressView;
    private View loginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        setTitle(R.string.title_UserLoginActivity);
        Button UserLoginButton = (Button) findViewById(R.id.btn_Login);
        UserLoginButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin();
            }
        });

        txtUsername = findViewById(R.id.etxt_Username);
        loginFormView = findViewById(R.id.LoginView);
        progressView = findViewById(R.id.login_progress);

        // ADD EVENT HANDLERS
        etxtUsername_onCharLimitReached(); // Set warning on field when length limit reached
    }

    /**
     * EVENTHANDLER: onCharLimitReached() for etxtUsername
     * This will set a flag if the user enters 8 chars warning them no more will be allowed.
     * AddressOf: etxtUsername (Username input EditText view)
     */
    private void etxtUsername_onCharLimitReached()
    {
        final EditText username = (EditText) findViewById(R.id.etxt_Username);
        username.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (username.getText().length() == 8)
                {
                    username.setError("Usernames can only be 8 characters long.");
                }
                else
                {
                    username.setError(null);
                }
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        // Reset errors.
        txtUsername.setError(null);

        // Store values at the time of the login attempt.
        String username = txtUsername.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(username))
        {
            txtUsername.setError(getString(R.string.error_field_required));
            focusView = txtUsername;
            cancel = true;
        }
        else if (!isUsernameValid(username))
        {
            txtUsername.setError(getString(R.string.error_invalid_username));
            focusView = txtUsername;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else
        {
            // attempt authentication against a network
            String query = "{" +
                    "  \"query\": {" +
                    "    \"match\": {" +
                    "      \"username\": \"" + username + "\"" +
                    "    }" +
                    "  }" +
                    "}";

            // Get the user with username from the database
            ElasticSearchUserController.GetUsersTask getUsersTask
                    = new ElasticSearchUserController.GetUsersTask();
            getUsersTask.execute(query);

            try
            {
                ArrayList<User> users = getUsersTask.get();
                if (users != null)
                {
                    if (users.size() == 1)
                    {
                        Log.i("UserLogin", "User exists. Attempting login...");
                        loginUser(users.get(0));
                    }
                    else if (users.size() > 1)
                    {
                        Log.i("UserLogin Error", "Username appears more than once in the database");
                    }
                    else
                    {
                        Log.i("UserLogin", "Attempting user registration...");
                        registerUser(username);
                    }
                }
                else
                {
                    // TODO offline logic
                    Log.i("UserLogin", "Initiating offline behaviour...");
                }
            }
            catch (InterruptedException e)
            {
                Log.i("UserLogin", "Login interrupted:" + e.toString());
            }
            catch (ExecutionException e)
            {
                Log.i("UserLogin", "getUsersTask exception:" + e.toString());
            }
        }
    }

    private boolean isUsernameValid(String username)
    {
        //TODO: Replace this with actual logic
        return !username.contains("A Bad Word");
    }

    private void loginUser(User user)
    {
        // Set the current user and start the app
        DataSourceManager.getInstance().setCurrentUser(user.getUsername());
        Intent intent = new Intent(this, NavigatorActivity.class);
        this.startActivity(intent);
    }

    private void registerUser(String username)
    {
        // TODO refactor this to use the createUserActivity
        User user = new User(username, new EmailAddress("DefaultLocalPart", "Domain@Default.com"),
                new PhoneNumber(0, 0, 0, 0));

        // Add the user to database
        ElasticSearchUserController.AddUsersTask addUserTask
                = new ElasticSearchUserController.AddUsersTask();
        addUserTask.execute(user);

        try
        {
            String jId = addUserTask.get();
            if (jId != null)
            {
                // TODO add new user to dataManager
                loginUser(user);
            }
            else
            {
                Log.i("UserLogin", "registed user JestId is null");
            }
        }
        catch (InterruptedException e)
        {
            Log.i("UserLogin", "Login interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("UserLogin", "addUserTask exception:" + e.toString());
        }
    }
}

