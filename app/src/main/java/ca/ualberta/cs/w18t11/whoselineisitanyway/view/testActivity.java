package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.rating.Rating;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.validator.TextValidator;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.validator.TextValidatorResult;

/*
 * This is just a generic activity for implementing parts of code; it is not intended to run as part of the app
 * and should be deleted before commiting to the final master release.
 *
 * Note to TA: Please basically ignore this file. It's a scrap area mostly for making dialogs. It will
 * not be present in the final edition of the project and is not intended for assessment.
 *
 * @author Lucas Thalen
 */
public class testActivity extends AppCompatActivity
{
    private LatLng location;
    private boolean res = false;
    private User resultUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button btnTest = (Button) findViewById(R.id.btnTest);
        Button btnMapSetDialog = (Button) findViewById(R.id.btn_mapdiag);
        Button button = (Button) findViewById(R.id.debug_Button);
        btnTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            }
        });
        btnMapSetDialog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }


}
