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

import java.util.ArrayList;
import java.util.Arrays;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource.DataSource;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource.DataSourceManager;
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
            Log.i("OPTIONS: ", "Logout Option Selected");
            // TODO Handle
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
        Intent outgoingIntent = new Intent(this, DetailableListActivity.class);
        String outgoingTitle = "List";
        ArrayList<Detailed> detaileds = new ArrayList<>();
        DataSource dataSource = DataSourceManager.getInstance().getLocalDataSource();
        Task[] allTasks = dataSource.getAllTasks();
        Bid[] allBids = dataSource.getAllBids();

        User currentUser = DataSourceManager.getInstance().getCurrentUser();

        if (id == R.id.all_tasks)
        {
            Log.i("NAVBAR: ", "All Tasks Selected");
            outgoingTitle = "All Tasks";
            detaileds.addAll(Arrays.asList(allTasks));
        }
        else if (id == R.id.my_tasks)
        {
            Log.i("NAVBAR: ", "My Tasks Selected");
            outgoingTitle = "My Tasks";
            for (Task task : allTasks)
            {
                if (task.getRequesterId().equals(currentUser.getUsername()))
                {
                    detaileds.add(task);
                }
            }

        }
        else if (id == R.id.assigned_tasks)
        {
            Log.i("NAVBAR: ", "Assigned Tasks Selected");
            outgoingTitle = "Assigned Tasks";
            for (Task task : allTasks)
            {
                if (task.getProviderId() != null && task.getProviderId()
                        .equals(currentUser.getUsername()))
                {
                    detaileds.add(task);
                }
            }
        }
        else if (id == R.id.nearby_tasks)
        {
            Log.i("NAVBAR: ", "Nearby Tasks Selected");
            outgoingTitle = "Nearby Tasks";
            // TODO filter tasks based on location
        }
        else if (id == R.id.my_bids)
        {
            Log.i("NAVBAR: ", "My Bids Selected");
            outgoingTitle = "My Bids";
            for (Bid bid : allBids)
            {
                if (bid.getProviderId().equals(currentUser.getUsername()))
                {
                    detaileds.add(bid);
                }
            }
        }
        else if (id == R.id.create_task)
        {
            Log.i("NAVBAR: ", "Create Task Selected");
            // TODO Handle
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        outgoingIntent.putExtra(DetailableListActivity.DATA_TITLE, outgoingTitle);
        outgoingIntent.putExtra(DetailableListActivity.DATA_DETAILABLE_LIST, detaileds);

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
}
