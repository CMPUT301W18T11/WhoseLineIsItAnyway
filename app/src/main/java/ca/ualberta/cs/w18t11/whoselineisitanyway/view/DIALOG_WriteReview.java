package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.rating.Rating;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * <h1>WriteReviewDialog</h1>
 * Type: Dialog
 * Collects a review on task marked completed and updates the provider user profile
 *
 * @author Lucas Thalen
 * IN: Provider user object
 * OUT: None (DSM is used to update the provider USer)
 */
public class DIALOG_WriteReview
{
    // General Context and Interface
    private DataSourceManager DSM;

    private Activity caller;

    private Context context;

    private OnReviewListener returnListener;

    // Dialog components and creation
    private AlertDialog diag;

    private View diagView;

    private Rating result;

    private User provider;

    /**
     * Constructor to create a dialog
     *
     * @param caller             calling activity/intent for context purposes and such
     * @param providerUSerObject this is the user object for the provider of the task being reviewed
     */
    public DIALOG_WriteReview(final Activity caller, User providerUSerObject)
    {
        this.caller = caller;
        context = caller;
        DSM = new DataSourceManager(context);

        if (context instanceof OnReviewListener)
        {
            returnListener = (OnReviewListener) context;
        }
        else
        {
            throw new RuntimeException("Calling class must contain interface methods!");
        }

        log("Assign a provider user for the Review Dialog");
        provider = providerUSerObject;
        createDialog();

        // EVENTHANDLERS
        btn_save_onClick();
        btn_cancel_onClick();
        etxt_review_onTextChanged();

    }

    private void createDialog()
    {
        final int LAYOUT_TEMPLATE = R.layout.dialog_write_review; // Get the template for the dialog

        // CREATE DIALOG
        AlertDialog.Builder diagBuilder = new AlertDialog.Builder(context);
        diagView = View
                .inflate(context, LAYOUT_TEMPLATE, null);
        diagBuilder.setView(diagView);

        diag = diagBuilder.create();
        diag.setTitle("Leave Feedback (Optional)");
    }

    public void showDialog()
    {
        diag.show();
    }

    private void btn_save_onClick()
    {
        Button submit = diagView.findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final EditText review = diagView.findViewById(R.id.etxt_review);
                final RatingBar rbQuality = diagView.findViewById(R.id.rb_quality);
                final RatingBar rbTTC = diagView.findViewById(R.id.rb_ttc);
                final RatingBar rbProf = diagView.findViewById(R.id.rb_prof);

                if (review.getText().length() == 0)
                {
                    review.setError("If you leave a review, please qualify your experience!");
                    return;
                }

                int quality = (int) rbQuality.getRating();
                int ttc = (int) rbTTC.getRating();
                int prof = (int) rbProf.getRating();

                log("Construct a rating for the WriteReviewDialog");
                result = new Rating(quality, ttc, prof);
                result.setReviewMSG(review.getText().toString());
                log("Rating: " + result.toString());

                provider.addUserReview(result);
                boolean addUserRes = DSM.addUser(provider);
                log("Saving REVIEW to PROVIDER:\n    " + "Status: " + String.valueOf(addUserRes) +
                        "\n    Provider: " + provider.getUsername() +
                        "\n    ESID:" + provider.getElasticId());
                returnListener.onReviewSubmitted();

            }
        });
    }

    private void btn_cancel_onClick()
    {
        Button cancel = diagView.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                diag.dismiss();
                returnListener.onReviewCanceled();
            }
        });

    }

    private void etxt_review_onTextChanged()
    {
        final EditText field = diagView.findViewById(R.id.etxt_review);
        final TextView charcount = diagView.findViewById(R.id.txt_charcount);

        field.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                int length = field.getText().length();
                charcount.setText(String.valueOf(length) + "/250"); // update charcounter text
                double percent = (double) length / 250;
                // Set varying colour severity on the charcounter text as it approaches limit
                if (percent < 0.80)
                {
                    charcount.setTextColor(0xFF006400);
                }
                else if (percent < 0.90)
                {
                    charcount.setTextColor(0xFFeead0e);
                }
                else if (percent < 1.0)
                {
                    charcount.setTextColor(0xFFee7600);
                }
                else if (percent == 1.0)
                {
                    charcount.setTextColor(0xFFb22222);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

    }

    private void log(String message)
    {
        Log.i("DIALOG_WREVIEW", message);
    }

    public interface OnReviewListener
    {
        void onReviewSubmitted();

        void onReviewCanceled();
    }

}
