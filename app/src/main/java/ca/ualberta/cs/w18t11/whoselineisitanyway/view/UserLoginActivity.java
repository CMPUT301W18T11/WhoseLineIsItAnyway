package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

/**
 * A login screen that offers login via username.
 *
 * @author Lucas Thalen, Samuel Dolha
 * @version 2.0
 */
public final class UserLoginActivity extends AppCompatActivity
        implements UserRegisterDialog.diagUserRegistrationListener
{
    private EditText usernameField;

    private final DataSourceManager dataSourceManager = new DataSourceManager(this);

    @Override
    protected final void onCreate(@Nullable final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_user_login);

        this.setTitle(R.string.title_UserLoginActivity);
        this.findViewById(R.id.btn_Login).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin();
            }
        });
        this.usernameField = findViewById(R.id.etxt_Username);

        final User user = this.dataSourceManager.getCurrentUser();

        if (user != null)
        {
            this.loginUser(user);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        // Reset errors.
        this.usernameField.setError(null);

        // Store values at the time of the login attempt.
        final String username = this.usernameField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (username.isEmpty())
        {
            usernameField.setError(getString(R.string.error_field_required));
            focusView = usernameField;
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
            final User user = dataSourceManager.getUser(username);

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

    private void loginUser(@NonNull final User user)
    {
        Log.i("UserLogin", "Registered user. Logging in...");

        String outgoingTitle = "List";
        Intent outgoingIntent = new Intent(this, DetailedListActivity.class);
        Task[] allTasks = dataSourceManager.getTasks();
        ArrayList<Detailed> tasks = new ArrayList<Detailed>(Arrays.asList(allTasks));

        dataSourceManager.setCurrentUser(user);

        outgoingIntent.putExtra(DetailedListActivity.DATA_TITLE, outgoingTitle);
        outgoingIntent.putExtra(DetailedListActivity.DATA_DETAILABLE_LIST, tasks);
        startActivity(outgoingIntent);
        finish();
    }

    private void registerUser(@NonNull final String username)
    {
        Log.i("UserLogin", "Registering new user...");
        UserRegisterDialog registerDiag = new UserRegisterDialog();
        registerDiag.showDialog(this, username);

    }

    @Override
    public void RegisterDiag_PosResultListener(final User user)
    {
        if (dataSourceManager.addUser(user))
        {
            loginUser(user);
        }
    }

    @Override
    public void RegisterDiag_NegResultListener()
    {
    }
}

