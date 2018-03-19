package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DetailableListActivity extends NavigatorActivity {
    static final String DATA_DETAILABLE_LIST = "com.whoselineisitanyway.DATA_DETAILABLE_LIST";
    static final String DATA_TITLE = "com.whoselineisitanyway.DATA_DETAILABLE_TITLE";
    private String title;
    private ListView detailsLV;
    private ArrayList<Detailable> detailList;
    private ArrayAdapter<Detailable> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        loadState();

        Intent intent = getIntent();
        if (intent.getSerializableExtra(DATA_DETAILABLE_LIST) != null) {
            detailList = (ArrayList<Detailable>) intent.getSerializableExtra(DATA_DETAILABLE_LIST);
        }
        else
        {
            detailList = new ArrayList<Detailable>();
        }
        if (intent.getSerializableExtra(DATA_TITLE) != null)
        {
            title = intent.getStringExtra(DATA_TITLE);
        } else {
            title = "WhoseLineIsItAnyway";
        }

        getSupportActionBar().setTitle(title);

        adapter = new ArrayAdapter<Detailable>(this,
                R.layout.activity_list, detailList);
        detailsLV = (ListView) findViewById(R.id.detail_LV);
        detailsLV.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        // Define action taken when clicking on each listview element
        detailsLV.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id){
                // Show the detail view of the selected item
                Detailable detail = (Detailable) adapter.getItemAtPosition(position);
                detail.showDetail(DetailActivity.class, v.getContext());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadState();
        adapter.notifyDataSetChanged();
    }

    private void loadState() {
        Log.i("DetailableListActivity", "Restoring saved instance state");
        // TODO implement
    }

    /**
     * Saves the state of the activity. Called after onStop
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i("DetailableListActivity", "onSaveInstanceState is called");
        loadState();
        savedInstanceState.putSerializable("detailList", detailList);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
