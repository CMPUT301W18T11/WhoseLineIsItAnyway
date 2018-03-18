package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class AllTasksActivity extends ListActivity {
    private static final String SAVEFILE = "AllTasksActivity.sav";
    private ListView allTasksLV;
    private int updatedSubPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        loadState();

        allTasksLV = (ListView) findViewById(R.id.all_tasks_LV);
        allTasksLV.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        // Define action taken when clicking on each listview element
        allTasksLV.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id){
                // TODO implement activity change
                // Get the selected item
                Task task = (Task) adapter.getItemAtPosition(position);
                updatedSubPos = position;

                saveState();

                // Start next activity and send it the selected item
//                Intent nextActivity = new Intent(AllTasksActivity.this, NEXT_ACTIVITY.class);
//                startActivity(nextActivity);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadState();
    }

    private void loadState() {
        // TODO actually implement this

        // This is placeholder code.
        objList.add(new Task("Task 1", "Description 1"));
        objList.add(new Task("Task 2", "Description 3"));
        objList.add(new Task("Task 3", "Description 3"));
    }

    private void saveState() {
        // TODO implement
    }
}
