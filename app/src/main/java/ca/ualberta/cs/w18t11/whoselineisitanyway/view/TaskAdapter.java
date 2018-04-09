package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.TaskStatus;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

public final class TaskAdapter extends ArrayAdapter<Task>
{
    private final DataSourceManager dataSourceManager = new DataSourceManager(this.getContext());

    public TaskAdapter(@NonNull final Context context, final int resource,
                       @NonNull final Task[] tasks)
    {
        super(context, resource, tasks);
    }

    @NonNull
    @Override
    public final View getView(final int position, @Nullable final View convertView,
                              @NonNull final ViewGroup parent)
    {
        View view = convertView;

        if (view == null)
        {
            final LayoutInflater inflater = (LayoutInflater) this.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            view = inflater.inflate(R.layout.row_task, parent, false);
        }

        final Task task = this.getItem(position);

        if (task != null)
        {
            final TextView titleValue = view.findViewById(R.id.taskRow_titleValue);
            final TextView requesterLabel = view.findViewById(R.id.taskRow_requesterLabel);
            final TextView requesterValue = view.findViewById(R.id.taskRow_requesterValue);
            final TextView providerLabel = view.findViewById(R.id.taskRow_providerLabel);
            final TextView providerValue = view.findViewById(R.id.taskRow_providerValue);
            final TextView bidLabel = view.findViewById(R.id.taskRow_bidLabel);
            final TextView bidValue = view.findViewById(R.id.taskRow_bidValue);
            final TextView myBidLabel = view.findViewById(R.id.taskRow_myBidLabel);
            final TextView myBidValue = view.findViewById(R.id.taskRow_myBidValue);

            final User user = this.dataSourceManager.getCurrentUser();
            assert user != null;
            final String username = user.getUsername();
            final boolean isRequester = username.equals(task.getRequesterUsername());
            final boolean isProvider = username.equals(task.getProviderUsername());

            final NumberFormat currencyFormat = NumberFormat
                    .getCurrencyInstance(Locale.getDefault());

            titleValue.setText(task.getTitle());
            requesterLabel.setText(R.string.label_requester);

            if (isRequester)
            {
                requesterValue.setText(R.string.value_currentUser);
            }
            else
            {
                requesterValue.setText(task.getRequesterUsername());
            }

            if (task.getStatus() != TaskStatus.REQUESTED)
            {
                final Bid[] bids = task.getBids();
                assert bids != null;

                if (!isRequester)
                {
                    for (Bid bid : bids)
                    {
                        if (bid.getProviderUsername().equals(username))
                        {
                            myBidLabel.setText(R.string.label_myBid);
                            myBidValue.setText(currencyFormat.format(bid.getValue()));

                            break;
                        }
                    }
                }

                if (task.getStatus() == TaskStatus.BIDDED)
                {
                    bidLabel.setText(R.string.label_lowestBid);
                    bidValue.setText(currencyFormat.format(task.getLowestBid().getValue()));
                }
                else
                {
                    bidLabel.setText(R.string.label_acceptedBid);

                    for (Bid bid : bids)
                    {
                        if (bid.getProviderUsername().equals(task.getProviderUsername()))
                        {
                            bidValue.setText(currencyFormat.format(bid.getValue()));

                            break;
                        }
                    }

                    if (task.getStatus() == TaskStatus.ASSIGNED)
                    {
                        providerLabel.setText(R.string.label_assignedProvider);
                    }
                    else
                    {
                        providerLabel.setText(R.string.label_doneProvider);
                    }

                    if (isProvider)
                    {
                        providerValue.setText(R.string.value_currentUser);
                    }
                    else
                    {
                        providerValue.setText(task.getProviderUsername());
                    }
                }
            }
        }

        return view;
    }
}
