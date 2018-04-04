package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * A base class which includes a navigation drawer and options menu.
 * Simply extend this activity from any activity you want to have a navigation drawer.
 */
public class NavigatorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    /*Taken From: https://gist.github.com/anandbose/7d6efb35c900eaba3b26*/
    private FrameLayout viewStub; //This is the framelayout to keep your content view
    private DataSourceManager DSM = DataSourceManager.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_list);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Set the user field text in the navbar
        ((TextView) findViewById(R.id.drawer_username))
                .setText(DSM.getCurrentUser().getUsername());
        ((TextView) findViewById(R.id.drawer_email))
                .setText(DSM.getCurrentUser().getEmailAddress().toString());

        // Adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (toggle.onOptionsItemSelected(item))
        {
            Log.i("OPTIONS: ", "Nav Bar Option Selected");
            return true;
        }

        int id = item.getItemId();
        if (id == R.id.profile)
        {
            Log.i("OPTIONS: ", "Profile Option Selected");
            // TODO Handle
            return true;
        }
        if (id == R.id.search)
        {
            Log.i("OPTIONS: ", "Search Option Selected");
            // TODO Handle
            return true;
        }
        if (id == R.id.logout)
        {
            Log.i("OPTIONS: ", "Logout Option Selected. Attempting to logout...");

            /* TODO do we need to unset the DataSourceManager's current user? It just gets set
                    on the next login anyways... */
            Intent outgoingIntent = new Intent(this, UserLoginActivity.class);
            startActivity(outgoingIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String outgoingTitle = "List";
        Intent outgoingIntent = new Intent(this, DetailableListActivity.class);
        ArrayList<Detailed> detailedArrayList = new ArrayList<>();

        if (id == R.id.all_tasks)
        {
            Log.i("NAVBAR: ", "All Tasks Selected");
            outgoingTitle = "All Tasks";
            detailedArrayList = buildAllTasksList();
        }
        else if (id == R.id.my_tasks)
        {
            Log.i("NAVBAR: ", "My Tasks Selected");
            outgoingTitle = "My Tasks";
            detailedArrayList = buildMyTasksList();
        }
        else if (id == R.id.assigned_tasks)
        {
            Log.i("NAVBAR: ", "Assigned Tasks Selected");
            outgoingTitle = "Assigned Tasks";
            detailedArrayList = buildAssignedTasksList();
        }
        else if (id == R.id.nearby_tasks)
        {
            Log.i("NAVBAR: ", "Nearby Tasks Selected");
            outgoingTitle = "Nearby Tasks";
            detailedArrayList = buildNearbyTasksList();
        }
        else if (id == R.id.my_bids)
        {
            Log.i("NAVBAR: ", "My Bids Selected");
            outgoingTitle = "My Bids";
            detailedArrayList = buildMyBidsList();


        }
        else if (id == R.id.create_task)
        {

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

            Log.i("NAVBAR: ", "Create Task Selected");

            // TODO fix return-to-login screen error
            // TODO make inheritance compatible?

            outgoingTitle= "Create Task";
            outgoingIntent = new Intent(this, CreateModifyTaskActivity.class);
            startActivity(outgoingIntent);
            finish();
            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        outgoingIntent.putExtra(DetailableListActivity.DATA_TITLE, outgoingTitle);
        outgoingIntent.putExtra(DetailableListActivity.DATA_DETAILABLE_LIST, detailedArrayList);

        startActivity(outgoingIntent);
        finish();
        return true;
    }

    /* Override all setContentView methods to put the content view to the FrameLayout view_stub
     * so that, we can make other activity implementations looks like normal activity subclasses.
     * Taken From: https://gist.github.com/anandbose/7d6efb35c900eaba3b26
     */
    @Override
    public void setContentView(int layoutResID)
    {
        if (viewStub != null)
        {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, viewStub, false);
            viewStub.addView(stubView, lp);
        }
    }

    @Override
    public void setContentView(View view)
    {
        if (viewStub != null)
        {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            viewStub.addView(view, lp);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params)
    {
        if (viewStub != null)
        {
            viewStub.addView(view, params);
        }
    }

    /**
     * Build a list containing all tasks.
     * @return ArrayList of the details representing those tasks.
     */
    private ArrayList<Detailed> buildAllTasksList()
    {
        // TODO: Extract filtering to the DataSourceManager
        Task[] allTasks = DataSourceManager.getInstance(this).getAllTasks();
        return new ArrayList<Detailed>(Arrays.asList(allTasks));
    }

    /**
     * Build a list of all tasks requested by the current user.
     * @return ArrayList of the details representing those tasks.
     */
    private ArrayList<Detailed> buildMyTasksList()
    {
        // TODO: Extract filtering to the DataSourceManager
        ArrayList<Detailed> detailedArrayList = new ArrayList<>();
        Task[] allTasks = DataSourceManager.getInstance(this).getAllTasks();
        User currentUser = DataSourceManager.getInstance(this).getCurrentUser();

        if (allTasks != null)
        {
            for (Task task : allTasks)
            {
                if (task.getRequesterUsername().equals(currentUser.getUsername()))
                {
                    detailedArrayList.add(task);
                }
            }
        }

        return detailedArrayList;
    }

    /**
     * Build a list of all tasks assigned to the current user.
     * @return ArrayList of the details representing those tasks.
     */
    private ArrayList<Detailed> buildAssignedTasksList()
    {
        // TODO: Extract filtering to the DataSourceManager
        ArrayList<Detailed> detailedArrayList = new ArrayList<>();
        Task[] allTasks = DataSourceManager.getInstance(this).getAllTasks();
        User currentUser = DataSourceManager.getInstance(this).getCurrentUser();

        if (allTasks != null)
        {
            for (Task task : allTasks)
            {
                if (task.getProviderUsername() != null && task.getProviderUsername()
                        .equals(currentUser.getUsername()))
                {
                    detailedArrayList.add(task);
                }
            }
        }

        return detailedArrayList;
    }

    /**
     * Build a list of all tasks nearby the current user.
     * @return ArrayList of the details representing those tasks.
     */
    private ArrayList<Detailed> buildNearbyTasksList()
    {
        // TODO: Extract filtering to the DataSourceManager
        ArrayList<Detailed> detailedArrayList = new ArrayList<>();
        Detailed[] allTasks = DataSourceManager.getInstance(this).getAllTasks();

        // TODO: Add tasks based on distance (and status?)

        return detailedArrayList;
    }

    /**
     * Build a list of all bids posted by the current user.
     * @return ArrayList of the details representing those tasks.
     */
    private ArrayList<Detailed> buildMyBidsList()
    {
        // TODO: Extract filtering to the DataSourceManager
        // TODO: Check if we should only display bids on tasks that are currently unassigned?
        ArrayList<Detailed> detailedArrayList = new ArrayList<>();
        Bid[] allBids = DataSourceManager.getInstance(this).getAllBids();
        User currentUser = DataSourceManager.getInstance(this).getCurrentUser();

        if (allBids != null)
        {
            for (Bid bid : allBids)
            {
                if (bid.getProviderUsername().equals(currentUser.getUsername()))
                {
                    detailedArrayList.add(bid);
                }
            }
        }

        return detailedArrayList;
    }
}
