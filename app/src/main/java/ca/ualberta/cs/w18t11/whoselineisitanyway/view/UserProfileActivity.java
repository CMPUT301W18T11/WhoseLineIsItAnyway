package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.rating.Rating;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.rating.RatingCollector;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;


/**
 * <h1>ca.ualberta.cs.w18t11.whoselineisitanyway.view.UserProfileActivity</h1>
 * This is an activity to show an arbitrarily passed user profile to a requester. READ ONLY
 * Make sure to pass the user as part of the bundle to this intent, tagged as EXISTING_USER
 * @author Lucas Thalen
 * @see User
 * @see RatingCollector
 */
public class UserProfileActivity extends AppCompatActivity {

    TextView usernameField;
    TextView contactField;
    TextView ratingSummary;
    ListView ratingList;
    Button btnClose;
    User currentUser; // Get the user that the person wants to see data on from the intent bundle
    ArrayList<Rating> ratings; // Get a list of ratings from the rating collector - is there a more OOP way to do this?
    RatingCollector reviews; // Get the ratingcollector from the user so that ratings can be read.
    private ArrayAdapter<Rating> ratingListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_userprofile);
        setTitle("View User");
        // Check that a user has been passed in the intent, if not throw an error and alert user + log
        try {
            currentUser = (User) savedInstanceState.get("EXISTING_USER");
        } catch (RuntimeException ex) {
            Log.i("UserProfileView", "No user data was passed to the activity via intent! tag it with EXISTING_USER!");
            Toast.makeText(this, "There was an error and the user couldn't be shown.", Toast.LENGTH_SHORT).show();
            finish(); // Exit the intent, there was an irrecoverable error
        }

        usernameField = (TextView) findViewById(R.id.txt_userName);
        contactField = (TextView) findViewById(R.id.txt_contact);
        ratingSummary = (TextView) findViewById(R.id.txt_reviewHeader);
        ratingList = (ListView) findViewById(R.id.lst_reviews);
        btnClose = (Button) findViewById(R.id.btn_close);

        // Extract the user's ratings
        reviews = currentUser.getRatingCollector();
        ratings = reviews.getRatingsList();

        usernameField.setText(currentUser.getUsername()); // Set the username
        // Construct a nice string for the user's contact information with columnarizeText columns
        // Gives each field + 4 space minimum before showing the contact info so it lines up and looks nice.
        String contactInfo =
                columnarizeText("Phone Number:", 18) + currentUser.getPhoneNumber().toString()
                + "\n" +
                columnarizeText("Email Address:", 18) + currentUser.getEmailAddress().toString();

        contactField.setText(contactInfo); // Set the contact info to this string
        ratingSummary.setText(reviews.toString()); // This gets the general summary (5 reviews, 5 stars, blah blah and sets it as text


        // Initialize Rating Adapter to act on the list of user ratings
        ratingListAdapter = new ArrayAdapter<Rating>(this, R.layout.list_object, ratings);
        ratingList.setAdapter(ratingListAdapter);
        ratingListAdapter.notifyDataSetChanged();

        // EVENT HANDLERS
        btnClose_onClick(); // On closing the intent. Doesn't have to do anything special, so just close it.
        lstRatings_onItemClicked(); // Get the rating that was selected and push it to full review to see the full text + stats for it
    }

    private void btnClose_onClick() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void lstRatings_onItemClicked() {
        ratingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rating ratingToShow = ratings.get(position);
                FullReviewDialog reviewDiag = new FullReviewDialog();
                reviewDiag.showDialog(UserProfileActivity.this, ratingToShow);
            }
        });
    }

    // Columnarize text. Java's inbuilt string manipulation is awkward, use what I know.
    private String columnarizeText(String input, Integer len) {
        if (len == null) { len = 25; } // This can be adjusted if deemed appropriate, but make it a optional
        if (input.length() > len) { return input; }
        int spacers = len - input.length();
        String spaceString = new String(new char[spacers]).replace("\0", " ");
        return input + spaceString;
    }
}
