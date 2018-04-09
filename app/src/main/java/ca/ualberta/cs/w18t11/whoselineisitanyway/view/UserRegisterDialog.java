package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.validator.TextValidator;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.validator.TextValidatorResult;

/**
 * <h1>UserRegisterDialog</h1>
 * Type: Dialog
 * This class is designed to create a dialog supporting the fields for a user registration for a new
 * user of the application. It will validate them and most importantly enforces thread-safe methods
 * to prevent a race condition while awaiting the result.
 * @author Lucas Thalen
 */
/*
 * Very important: IMPLEMENT THE CALLBACK METHODS IN THE CALLING CLASS
 * Due to how Android handles threads (almost entirely Async) these are the only way to effect
 * a code "pause" while awaiting a result. You must design your code such that this method is the
 * last executed in a block; use its callback methods to continue your code by calling an additional
 * method. Anything else will result in a race condition where code progresses without the returned
 * result being properly assigned.
 */

public class UserRegisterDialog {
    public interface diagUserRegistrationListener {
        void RegisterDiag_PosResultListener(final User result);
        void RegisterDiag_NegResultListener();
    }


    private Activity caller;
    private View diagView;
    private AlertDialog diag;
    private final TextValidator validator = new TextValidator();
    private User result;
    private diagUserRegistrationListener returnListener;

    /**
     * Prompt the user with a Registration Dialog
     * @param caller This is used for the dialogbuilder context requirements; Type: Activity
     * @param username No need to make them enter the username twice.
     */
    public void showDialog(final Activity caller, final String username) {

        // Set context for dialog
        this.caller = caller;
        Context context = (Context) caller;

        // Validate that the class calling this has implemented the callback methods
        if (context instanceof diagUserRegistrationListener) {
            returnListener = (diagUserRegistrationListener) context;
        } else { throw new RuntimeException("Calling class must contain interface methods!"); }

        // Dialog making stuff
        AlertDialog.Builder diagBuilder = new AlertDialog.Builder(context);
        diagView = View
                .inflate(context, R.layout.dialog_register_user, null );
        diagBuilder.setView(diagView);

        diag = diagBuilder.create();
        //diag.requestWindowFeature(Window.set);
        //diag.setContentView(diagView);
        // End dialog making stuff

        final TextView txtUsername = (TextView) diagView.findViewById(R.id.txtUsername);
        final EditText etxtPhone = (EditText) diagView.findViewById(R.id.etxtPhoneNum);
        final EditText etxtEmail = (EditText) diagView.findViewById(R.id.etxtEmail);

        final Button btnOK = (Button) diagView.findViewById(R.id.btn_OK);
        final Button btnCancel = (Button) diagView.findViewById(R.id.btn_cancel);
        
        txtUsername.setText(username);
        diag.show(); // CALL DIALOG

        // EVENTHANDLERS
        btn_Cancel_onClick(btnCancel);
        btn_OK_onClick(btnOK, txtUsername);
        etxt_Phone_onTextChanged(etxtPhone);
        etxt_Email_onTextChanged(etxtEmail);

    }

    /**
     * EVENTHANDLER: Ongoing validation of the Phone Number field to ensure compliant entry
     * @param etxtPhone A reference to the EditText to monitor
     */
    private void etxt_Phone_onTextChanged(final EditText etxtPhone) {
        etxtPhone.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }
            @Override
            public void afterTextChanged(Editable editable)
            {
                String fieldContents = etxtPhone.getText().toString();
                TextValidatorResult phoneRes = validator.validatePhoneNumber(fieldContents, true);
                if (fieldContents.isEmpty()) {}
                else
                {
                    if (phoneRes.isError())
                    {
                        etxtPhone.setError(phoneRes.getErrorMSG());
                        int fieldLen = etxtPhone.getText().length();
                        etxtPhone.setText(fieldContents.substring(0, fieldLen - 1));
                        etxtPhone.setSelection(fieldLen - 1);

                    }
                }
            }
        });
    }
    /**
     * EVENTHANDLER: Ongoing validation of the email field to ensure compliant entry
     * @param etxtEmail A reference to the EditText to monitor
     */
    private void etxt_Email_onTextChanged(final EditText etxtEmail) {
        etxtEmail.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                String fieldContents = etxtEmail.getText().toString();
                TextValidatorResult emailRes = validator.validateEmail(fieldContents, true);
                if (fieldContents.isEmpty())
                {
                    return;
                }
                else
                {
                    if (emailRes.isError())
                    {
                        etxtEmail.setError(emailRes.getErrorMSG());
                        int fieldLen = etxtEmail.getText().length();
                        etxtEmail.setText(fieldContents.subSequence(0, fieldLen - 1));
                        etxtEmail.setSelection(fieldLen - 1);
                    }
                }
            }
        });
    }

    /**
     * EVENTHANDLER: The user has clicked the button to cancel registration
     * @param btnCancel A reference to the button to add the eventhandler to
     */
    private void btn_Cancel_onClick(final Button btnCancel) {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(null);
                returnListener.RegisterDiag_NegResultListener(); // Callback to main method
                /*
                * in main, this should execute code flow that handles any issues if a user were to cancel the
                * registration process. Will probably be left blank.
                 */
                diag.dismiss();
            }
        });
    }

    /**
     * EVENTHANDLER: The user has clicked the button to submit their registration
     * @param btnOK A reference to the button to be clicked
     * @param txtUsername Get the username that was selected
     */
    private void btn_OK_onClick(final Button btnOK, final TextView txtUsername) {
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final EditText phone = (EditText) diagView.findViewById(R.id.etxtPhoneNum);
               final EditText email = (EditText) diagView.findViewById(R.id.etxtEmail);
               final TextValidatorResult phoneRes = validator.validatePhoneNumber(phone.getText().toString(), false);
               final TextValidatorResult emailRes = validator.validateEmail(email.getText().toString(), false);

               // Do final validation on the parts; if noncompliant, do nothing.
               if (phoneRes.isError() || emailRes.isError()) {
                   phone.setError(phoneRes.getErrorMSG());
                   email.setError(emailRes.getErrorMSG());
               } else {
                   // Create a PhoneNumber and an EmailAddress; use this for a new User object and set it as
                   // the return result.
                   PhoneNumber phoneNum = assemblePhone(phoneRes.getComponents());
                   EmailAddress emailAdd = assembleEmail(emailRes.getComponents());
                   setResult(new User(txtUsername.getText().toString(), emailAdd, phoneNum));
                   returnListener.RegisterDiag_PosResultListener(result);
                   /*
                    * This is the success returning callback in the calling class. This should handle code that
                    * deals with a User object (such as integrating the new registration into the database and such)
                    * in the caller method.
                    */
                   diag.dismiss();
               }
            }
        });

    }

    /**
     * Takes the text components from a valid TextValidatorResult and parses them into a PhoneNumber object
     * @param components
     * @return PhoneNumber phone
     */
    private PhoneNumber assemblePhone(ArrayList<String> components) {
        PhoneNumber phone;
        int fields[] = new int[4];
        // No index 0 in components as this is the regex result for the full string
        fields[0] = Integer.valueOf(components.get(1)); // Country Code
        fields[1] = Integer.valueOf(components.get(2)); // Area
        fields[2] = Integer.valueOf(components.get(3)); // Exchange
        fields[3] = Integer.valueOf(components.get(4)); // Customer Num.
        phone = new PhoneNumber(
            fields[0],
            fields[1],
            fields[2],
            fields[3]
        );
        return phone;
    }

    /**
     * Takes the text components from a valid TextValidatorResult and parses them into an EmailAddress object
     * @param components
     * @return EmailAddress email
     */
    private EmailAddress assembleEmail(ArrayList<String> components) {
        EmailAddress email;
        String fields[] = new String[2];
        fields[0] = components.get(1); // <    >@example.com
        fields[1] = components.get(2); // example@<    >
        email = new EmailAddress(
            fields[0],
            fields[1]
        );
        return email;
    }

    /**
     * To avoid permissions issues regarding anonymous methods, use this to set the global User user
     * @param user
     */
    private void setResult(User user) {
        result = user;
    }
}
