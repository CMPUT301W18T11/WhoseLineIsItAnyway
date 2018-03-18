package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    TextView title;
    ListView detailList;
    ArrayList<Detail> details;

    /**
     * Preform any activities needed on creation
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
    public void customizeUserInterface() {
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
        detailList = findViewById(R.id.listview_activity_detail_list);
        detailList.setAdapter(new DetailRowAdapter(this, details));
    }

    /**
     * Grab data from the incoming intent
     */
    private void setupFromIntent()
    {
        Intent intent = getIntent();
        if(intent != null)
        {
            if(intent.getSerializableExtra(Detailable.DATA_DETAIL_LIST) != null)
            {
                details = (ArrayList<Detail>) intent.getSerializableExtra(Detailable.DATA_DETAIL_LIST);
            }
            if(intent.getStringExtra(Detailable.DATA_DETAIL_TITLE) != null)
            {
                title.setText(intent.getStringExtra(Detailable.DATA_DETAIL_TITLE));
            }
        }
        else
        {
            // Mock up a task to view
            Task task = new Task("Test Task", "A task to test");
            task.showDetail(DetailActivity, );
        }
    }
}
