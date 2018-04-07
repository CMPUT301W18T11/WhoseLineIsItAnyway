package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;

/**
 * A custom DetailActivity for a task.
 */

public class TaskDetailActivity extends DetailActivity {
    public void customizeUserInterface(ViewGroup viewGroup)
    {
        addBidButton(viewGroup);
    }

    private void addBidButton(ViewGroup viewGroup)
    {
        // Make a view for the button
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_detail_bid_button, viewGroup);

        // Configure it
        Button bidButton = (Button) view.findViewById(R.id.button_place_bid);
        bidButton.setOnClickListener(new View.OnClickListener() {
            // Go to detail screen without sending data
            @Override
            public void onClick(View view) {

            }
        });

        // Add it into the existing view
        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.activity_detail_group);
        insertPoint.addView(view, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
