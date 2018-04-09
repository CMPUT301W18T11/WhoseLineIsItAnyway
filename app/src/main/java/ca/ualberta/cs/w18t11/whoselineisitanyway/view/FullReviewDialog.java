package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.rating.Rating;

/**
 * Dialog for submitting user reviews
 */
public class FullReviewDialog
{
    private Rating review;

    private Activity caller;

    private View diagView;

    private AlertDialog diag;


    public void showDialog(final Activity caller, final Rating rate)
    {
        review = rate;
        this.caller = caller;
        Context context = caller;

        AlertDialog.Builder diagBuilder = new AlertDialog.Builder(context);
        diagView = View
                .inflate(context, R.layout.dialog_full_review, null);
        diagBuilder.setView(diagView);

        diag = diagBuilder.create();

        TextView reviewField = diagView.findViewById(R.id.txt_Reviewtext);
        reviewField.setText(rate.fullReview());
        diag.show();

        // EVENTHANDLERS
        btn_Close_onClick();
    }

    private void btn_Close_onClick()
    {
        Button close = diagView.findViewById(R.id.btn_dialog_view_fullreview_Exit);
        close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                diag.dismiss();
            }
        });
    }
}
