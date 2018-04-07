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
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_detail_bid_button, viewGroup);

        // fill in any details dynamically here
        Button bidButton = (Button) view.findViewById(R.id.button_place_bid);
        bidButton.setOnClickListener(new View.OnClickListener() {
            // Go to detail screen without sending data
            @Override
            public void onClick(View view) {

            }
        });

        // insert into main view
        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.activity_detail_group);
        insertPoint.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }

    private void addBidButton()
    {

    }
}
