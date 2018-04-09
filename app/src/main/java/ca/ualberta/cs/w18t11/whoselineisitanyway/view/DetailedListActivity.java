package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder.DetailedListBuilder;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;

/**
 * An activity for displaying a list of Detailed abjects.
 * Defines the action taken when clicking on each object in the list.
 */
public final class DetailedListActivity extends NavigatorActivity
{
    public static final String DATA_DETAILABLE_LIST
            = "NavigatorActivity_detailedList";

    public static final String DATA_TITLE = "NavigatorActivity_title";

    public static final String DATA_LIST_BUILDER = "NavigatorActivity_listBuilder";

    public static final String DATA_DETAILABLE_ADAPTER_TYPE
            = "NavigatorActivity_adapter";

    private ArrayList<Detailed> detaileds = new ArrayList<>();

    private ArrayList<Task> tasks = new ArrayList<>();

    private ArrayAdapter adapter;

    private AdapterType adapterType;

    private DetailedListBuilder detailedListBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_list);
//        this.loadState();

        detailedListBuilder = (DetailedListBuilder) this.getIntent()
                .getSerializableExtra(DATA_LIST_BUILDER);
        final ActionBar actionBar = this.getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(this.getIntent().getStringExtra(DetailedListActivity.DATA_TITLE));

        final ListView listView = findViewById(R.id.detail_LV);

        adapterType = (AdapterType) this.getIntent()
                .getSerializableExtra(DetailedListActivity.DATA_DETAILABLE_ADAPTER_TYPE);
        switch (adapterType)
        {
            case TASK:
                adapter = new TaskAdapter(this, R.layout.row_task, tasks);
                break;
            case BID:
                // TODO: Replace with BidAdapter.
                adapter = new ArrayAdapter<>(this, R.layout.list_object, detaileds);
                break;
        }

        listView.setAdapter(adapter);
        reloadData(Arrays.asList(detailedListBuilder.buildDetailedList(this)));


//        adapter.notifyDataSetChanged();

        // Define action taken when clicking on each listview element
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id)
            {
                // Show the detail view of the selected item
                Detailed detail = (Detailed) adapter.getItemAtPosition(position);
                detail.showDetails(DetailActivity.class, v.getContext());
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        reloadData(Arrays.asList(detailedListBuilder.buildDetailedList(this)));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        reloadData(Arrays.asList(detailedListBuilder.buildDetailedList(this)));
    }

    private void reloadData(List<Detailed> newData)
    {
        switch (adapterType)
        {
            case TASK:
                tasks.clear();
                for (Detailed detailed : newData)
                {
                    tasks.add((Task) detailed);
                }
                break;
            case BID:
            default:
                detaileds.clear();
                detaileds.addAll(newData);
                break;
        }
        adapter.notifyDataSetChanged();
    }
//
//    private void loadState()
//    {
//        Log.i("DetailedListActivity", "Restoring saved instance state");
//        // TODO implement
//    }
//
//    /**
//     * Saves the state of the activity. Called after onStop
//     *
//     * @param savedInstanceState
//     */
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState)
//    {
//        Log.i("DetailedListActivity", "onSaveInstanceState is called");
//        loadState();
//        savedInstanceState.putSerializable("detaileds", detaileds);
//        // Always call the superclass so it can save the view hierarchy state
//        super.onSaveInstanceState(savedInstanceState);
//    }
}
