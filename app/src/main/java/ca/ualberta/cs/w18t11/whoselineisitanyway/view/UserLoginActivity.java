package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource.DataSourceManager;


/**
 * A login screen that offers login via email/password.
 */
public class UserLoginActivity extends AppCompatActivity
{
    // A dummy authentication store containing known user names
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "bob", "alice", "eve"
    };

    // Keep track of the login task to ensure we can cancel it if requested
    private UserLoginTask authTask = null;

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
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        if (authTask != null)
        {
            return;
        }

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
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            authTask = new UserLoginTask(this, username);
            authTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String username)
    {
        //TODO: Replace this with actual logic
        return !username.contains("A Bad Word");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        else
        {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    {

        private final String username;
        private final Context context;

        UserLoginTask(Context context, String username)
        {
            this.context = context;
            this.username = username;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            // TODO: attempt authentication against a network service

            try
            {
                // Simulate network access
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS)
            {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(username))
                {
                    // Account exists, return true if the password matches.
                    DataSourceManager.getInstance().setCurrentUser(username);
                    Intent intent = new Intent(context, NavigatorActivity.class);
                    context.startActivity(intent);
                    return true;
                }
            }

            // TODO: register the new account here.

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            authTask = null;
            showProgress(false);

            if (success)
            {
                finish();
            }
            else
            {
                txtUsername.setError(getString(R.string.error_invalid_username));
                txtUsername.requestFocus();
            }
        }

        @Override
        protected void onCancelled()
        {
            authTask = null;
            showProgress(false);
        }

    }
}

