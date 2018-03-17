package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class testActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                // THIS IS THE CODE FOR AN ALERTDIALOG FOR REGISTERING THE USER
                final TextValidator txtvalidate = new TextValidator();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        testActivity.this);
                AlertDialog alertDialog = alertDialogBuilder.create();
                LayoutInflater inflater = testActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.layout_register_user, null);
                final EditText etxtUsername = (EditText) findViewById(R.id.etxtUsername);
                final EditText etxtPhone = (EditText) findViewById(R.id.etxtPhoneNum);
                final EditText etxtEmail = (EditText) findViewById(R.id.etxtEmail);
                alertDialogBuilder.setView(dialogView).setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
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

                            String username = etxtUsername.getText().toString();

                            // Create a Working Phone Number
                            int[] phoneComponents = new int[4]; // Hold integer conversions of split up phone number
                            phoneComponents[0] = Integer.valueOf(phoneRes.getComponents().get(1));
                            phoneComponents[1] = Integer.valueOf(phoneRes.getComponents().get(2));
                            phoneComponents[2] = Integer.valueOf(phoneRes.getComponents().get(3));
                            phoneComponents[3] = Integer.valueOf(phoneRes.getComponents().get(4));
                            PhoneNumber phone = new PhoneNumber(
                                    phoneComponents[0],
                                    phoneComponents[1],
                                    phoneComponents[2],
                                    phoneComponents[3]
                            );

                            // Create a working Email Object for the user
                            String[] emailComponents = new String[2];
                            emailComponents[0] = emailRes.getComponents().get(1);
                            emailComponents[1] = emailRes.getComponents().get(2);
                            EmailAddress email = new EmailAddress(
                                    emailComponents[0],
                                    emailComponents[1]
                            );

                            // Create User Object
                            User newUser = new User(email, phone, username);

                            // THIS IS WHERE THE BACKEND COMES IN - PATCH IN BACKEND CODE

                            // END BACKEND CODE

                            dialog.dismiss(); // Close dialog, create new user
                        }
                    }


                });


            }
        });
    }
}
