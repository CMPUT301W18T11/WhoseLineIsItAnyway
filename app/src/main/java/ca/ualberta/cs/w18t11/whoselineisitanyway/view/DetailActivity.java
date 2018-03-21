package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detail;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailable;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

public class DetailActivity extends AppCompatActivity
{

    TextView title;
    ListView detailList;
    DetailRowAdapter rowAdapter;
    ArrayList<Detail> details;

    /**
     * Preform any activities needed on creation
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeUserInterface();
        setupFromIntent();
        customizeUserInterface();
    }

    /**
     * Customize the user interface by adding buttons or modifying default elements
     * To be overridden if needed, otherwise, leave it blank
     */
    public void customizeUserInterface()
    {
        // Override
    }

    /**
     * Initialize the user interface by setting the values for the default elements
     */
    private void initializeUserInterface()
    {
        title = findViewById(R.id.textview_activity_detail_title);
        title.setText("Detail");
        details = new ArrayList<>();
        rowAdapter = new DetailRowAdapter(this, details);
        detailList = findViewById(R.id.listview_activity_detail_list);
        detailList.setAdapter(rowAdapter);
    }

    /**
     * Grab data from the incoming intent
     */
    private void setupFromIntent()
    {
        Intent intent = getIntent();
        if (intent.getStringExtra(Detailable.DATA_DETAIL_TITLE) != null)
        {
            if (intent.getSerializableExtra(Detailable.DATA_DETAIL_LIST) != null)
            {
                details.addAll((ArrayList<Detail>) intent
                        .getSerializableExtra(Detailable.DATA_DETAIL_LIST));
                rowAdapter.notifyDataSetChanged();
            }
            if (intent.getStringExtra(Detailable.DATA_DETAIL_TITLE) != null)
            {
                title.setText(intent.getStringExtra(Detailable.DATA_DETAIL_TITLE));
            }
        }
        else
        {
            // Mock up a task to view
            Task task = new Task("id", "requesterId", "Test Task", "A task to test");
            Bid bid = new Bid("1234", "5432", new BigDecimal(1234));
            final User user = new User(
                    new EmailAddress("user", "gmail.com"), new PhoneNumber(3, 333, 333, 3333),
                    "username");
            user.showDetail(DetailActivity.class, this);
        }
    }
}
