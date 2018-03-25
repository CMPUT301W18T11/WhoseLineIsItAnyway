package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.rating.Rating;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.validator.TextValidator;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.validator.TextValidatorResult;

/**
 * Created by lucas on 2018-03-24.
 */

// TODO Add interface for listener
    // TODO code submission button
    // TODO code cancel button

public class UserRegisterDialog {
    private Activity caller;
    private View diagView;
    private AlertDialog diag;
    private String username;
    private final TextValidator validator = new TextValidator();
    private User result;

    public void showDialog(final Activity caller, final String username) {
        this.username = username;
        this.caller = caller;
        Context context = (Context) caller;

        AlertDialog.Builder diagBuilder = new AlertDialog.Builder(context);
        diagView = View
                .inflate(context, R.layout.dialog_register_user, null );
        diagBuilder.setView(diagView);

        diag = diagBuilder.create();
        diag.setContentView(diagView);


        final TextView txtUsername = (TextView) diagView.findViewById(R.id.txtUsername);
        final EditText etxtPhone = (EditText) diagView.findViewById(R.id.etxtPhoneNum);
        final EditText etxtEmail = (EditText) diagView.findViewById(R.id.etxtEmail);

        final Button btnOK = (Button) diagView.findViewById(R.id.btn_OK);
        final Button btnCancel = (Button) diagView.findViewById(R.id.btn_cancel);
        
        txtUsername.setText(username);
        diag.show();

        // EVENTHANDLERS
        btn_Cancel_onClick(btnCancel);
        btn_OK_onClick(btnOK);
        etxt_Phone_onTextChanged(etxtPhone);
        etxt_Email_onTextChanged(etxtEmail);
    }

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
                if (fieldContents.isEmpty())
                {
                    return;
                }
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
    private void btn_Cancel_onClick(final Button btnCancel) {}
    private void btn_OK_onClick(final Button btnOK) {

    }
    private void setResult(User user) {

    }
}
