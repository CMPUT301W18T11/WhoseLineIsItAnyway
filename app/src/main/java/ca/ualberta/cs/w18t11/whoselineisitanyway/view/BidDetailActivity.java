package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.TaskStatus;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * A custom DetailActivity for a bid.
 */

public class BidDetailActivity extends DetailActivity {

    /**
     * Alter the appearance of the UI to better suit a bid.
     * @param viewGroup Parent for adding interface elements.
     */
    public void customizeUserInterface(ViewGroup viewGroup)
    {
        Bid bid = getBidFromIntent();
        if(bid != null)
        {
            renderBasedOnBid(bid, viewGroup);
        }
    }

    /**
     * Change what buttons and other UI elements are presented based on the bid.
     * @param bid to be rendered.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderBasedOnBid(Bid bid, ViewGroup viewGroup)
    {
        DataSourceManager dataSourceManager = new DataSourceManager(this);
        User currentUser = dataSourceManager.getCurrentUser();
        Task task = dataSourceManager.getTask(bid.getTaskId());

        assert currentUser != null;
        assert task != null;

        if(currentUser.getUsername().equals(task.getRequesterUsername()))
        {
            renderBidOnMyTask(bid, task, viewGroup);
        }
    }

    private void renderBidOnMyTask(Bid bid, Task task, ViewGroup viewGroup)
    {
        if(task.getStatus().equals(TaskStatus.BIDDED))
        {
            addAcceptBidButton(bid, task, viewGroup);
            addDeclineBidButton(bid, task, viewGroup);
        }
    }

    private void addAcceptBidButton(final Bid bid, final Task task, ViewGroup viewGroup)
    {
        // Make a view for the button
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater != null)
        {
            View view = inflater.inflate(R.layout.activity_detail_button, viewGroup);
            Button bidButton = view.findViewById(R.id.detail_button);
            bidButton.setText(R.string.button_accept_bid);
            bidButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task acceptedTask = task.assignProvider(bid.getProviderUsername());
                    finish();
                    acceptedTask.showDetails(TaskDetailActivity.class, view.getContext());
                }
            });

            ViewGroup insertPoint = findViewById(R.id.header_linear_layout);
            insertPoint.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private void addDeclineBidButton(final Bid bid, final Task task, ViewGroup viewGroup)
    {
        // Make a view for the button
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater != null)
        {
            View view = inflater.inflate(R.layout.activity_detail_button, viewGroup);
            Button bidButton = view.findViewById(R.id.detail_button);
            bidButton.setText(R.string.button_decline_bid);
            bidButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataSourceManager dataSourceManager = new DataSourceManager(view.getContext());
                    Task declinedBidTask = task.declineBid(bid, dataSourceManager);
                    finish();
                    declinedBidTask.showDetails(TaskDetailActivity.class, view.getContext());
                }
            });

            ViewGroup insertPoint = findViewById(R.id.header_linear_layout);
            insertPoint.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private Bid getBidFromIntent() throws NullPointerException
    {
        Intent detailIntent = getIntent();
        return (Bid) detailIntent.getSerializableExtra(Bid.BID_KEY);
    }
}
