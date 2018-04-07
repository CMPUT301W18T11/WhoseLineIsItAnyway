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
import java.util.Arrays;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;



// FOR DEBUGGING PRACTICES:
// all entered usernames should start with db_ so they can be easily purged
// used: debug
// used: db_Paul


/**
 * A login screen that offers login via username.
 */
public class UserLoginActivity extends AppCompatActivity implements UserRegisterDialog.diagUserRegistrationListener
{
    private EditText txtUsername;
    private View progressView;
    private View loginFormView;
    private User user; // The user to register if so required
    private DataSourceManager DSM = new DataSourceManager(this);

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
            User user = DSM.getUser(username);

            if (user != null)
            {
                loginUser(user);
            }
            else
            {
                registerUser(username);
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
        Log.i("UserLogin", "Registered user. Logging in...");

        String outgoingTitle = "List";
        Intent outgoingIntent = new Intent(this, DetailedListActivity.class);
        Task[] allTasks = DSM.getTasks();
        ArrayList<Detailed> tasks = new ArrayList<Detailed>(Arrays.asList(allTasks));

        outgoingIntent.putExtra(DetailedListActivity.DATA_TITLE, outgoingTitle);
        outgoingIntent.putExtra(DetailedListActivity.DATA_DETAILABLE_LIST, tasks);
        startActivity(outgoingIntent);
        finish();
    }

    private void registerUser(String username)
    {
        Log.i("UserLogin", "Registering new user...");
        UserRegisterDialog registerDiag = new UserRegisterDialog();
        registerDiag.showDialog(this, username);

    }

    @Override
    public void RegisterDiag_PosResultListener(final User result) {
        this.user = result;
        if (DSM.addUser(this.user))
        {
            loginUser(this.user);
        }
        else
        {
            // TODO Do nothing here?
        }
    }

    @Override
    public void RegisterDiag_NegResultListener() {}
}

