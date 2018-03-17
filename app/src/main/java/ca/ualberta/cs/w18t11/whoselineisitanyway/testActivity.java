package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * This is just a generic activity for implementing parts of code; it is not intended to run as part of the app
 * and should be deleted before commiting to the final master release.
 * @author Lucas Thalen
 */
public class testActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateRegistrationDialog("TEST");
            }
        });
    }

    /**
     * GENERATE REGISTRATION DIALOG
     * This function generates a registration dialog to enroll a new user in the application
     * database.
     * @param usr This can be edited once input is known/needed/unneeded
     * @see UserLoginActivity
     * @see User
     * @return Either a boolean or a complete user object; assign to object type and check class to
     * determine if the code ran successfully or not.
     */
    private Object generateRegistrationDialog(String usr) {
        // Parts of this, mostly the generation of the actual dialog, are based on web results.
        // while the post in question addresses email and phone, I only referenced the creation logic
        // not the actual text management code or interface

        // https://stackoverflow.com/a/5235348/4914842
        // Cephron | Creative Commons 3.0 Attrib SA
        // Posted 03/08/2011 | Accessed 03/18/2018
        // Creating a dialog from a custom layout

        final String[] usrID = new String[1];
        final String[] emailComponents = new String[2];
        final int[] phoneComponents = new int[4];
        final boolean[] res = new boolean[1];

        final TextValidator txtvalidate = new TextValidator();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                testActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.layout_register_user, (ViewGroup) findViewById(R.id.layout_root));
        alertDialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Register New User");

        final EditText etxtUsername = (EditText) dialogView.findViewById(R.id.etxtUsername);
        final EditText etxtPhone = (EditText) dialogView.findViewById(R.id.etxtPhoneNum);
        final EditText etxtEmail = (EditText) dialogView.findViewById(R.id.etxtEmail);

        final Button btnOK = (Button) dialogView.findViewById(R.id.btn_OK);
        final Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);

        // Input expected username
        etxtUsername.setText(usr);

        etxtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etxtUsername.getText().length() == 8) {
                    etxtUsername.setError("Username can be 9 characters max.");
                }
            }
        });

        etxtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String fieldContents = etxtPhone.getText().toString();
                TextValidatorResult phoneRes = txtvalidate.validatePhoneNumber(fieldContents, true);
                if (fieldContents.isEmpty()) {
                    return;
                } else {
                    if (phoneRes.isError()) {
                        etxtPhone.setError(phoneRes.getErrorMSG());
                        int fieldLen = etxtPhone.getText().length();
                        etxtPhone.setText(fieldContents.substring(0, fieldLen - 1));
                        etxtPhone.setSelection(fieldLen - 1);

                    }
                }
            }
        });

        etxtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String fieldContents = etxtEmail.getText().toString();
                TextValidatorResult emailRes = txtvalidate.validateEmail(fieldContents, true);
                if (fieldContents.isEmpty()) {
                    return;
                } else {
                    if (emailRes.isError()) {
                        etxtEmail.setError(emailRes.getErrorMSG());
                        int fieldLen = etxtEmail.getText().length();
                        etxtEmail.setText(fieldContents.subSequence(0, fieldLen - 1));
                        etxtEmail.setSelection(fieldLen - 1);
                    }
                }
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get final say on if input is good
                TextValidatorResult phoneRes = txtvalidate.validatePhoneNumber(etxtPhone.getText().toString(), false);
                TextValidatorResult emailRes = txtvalidate.validateEmail(etxtEmail.getText().toString(), false);
                if (phoneRes.isError()) {
                    etxtPhone.setError(phoneRes.getErrorMSG());
                } // Get format errors and set msg on edittext
                if (emailRes.isError()) {
                    etxtEmail.setError(emailRes.getErrorMSG());
                }
                if (!(phoneRes.isError() || emailRes.isError())) { // No errors to report

                    usrID[0] = etxtUsername.getText().toString();

                    // Create a Working Phone Number
                    // Hold integer conversions of split up phone number
                    phoneComponents[0] = Integer.valueOf(phoneRes.getComponents().get(1));
                    phoneComponents[1] = Integer.valueOf(phoneRes.getComponents().get(2));
                    phoneComponents[2] = Integer.valueOf(phoneRes.getComponents().get(3));
                    phoneComponents[3] = Integer.valueOf(phoneRes.getComponents().get(4));


                    // Create a working Email Object for the user
                    emailComponents[0] = emailRes.getComponents().get(1);
                    emailComponents[1] = emailRes.getComponents().get(2);



                    res[0] = true;
                    alertDialog.dismiss(); // Close dialog, create new user

                }
            }


        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res[0] = false;
                alertDialog.dismiss();
                // PUT RETURN CONDITIONS ON NEGATIVE HERE
            }
        });
        alertDialog.show();

        // Try to create new user

        // Check that necessary parameters for a new user are present
        if (res[0] != false) {
            PhoneNumber phone = new PhoneNumber(
                    phoneComponents[0],
                    phoneComponents[1],
                    phoneComponents[2],
                    phoneComponents[3]
            );

            EmailAddress email = new EmailAddress(
                    emailComponents[0],
                    emailComponents[1]
            );

            return(new User(email, phone, usrID[0]));
        }
        else { return false; }

    }
}
