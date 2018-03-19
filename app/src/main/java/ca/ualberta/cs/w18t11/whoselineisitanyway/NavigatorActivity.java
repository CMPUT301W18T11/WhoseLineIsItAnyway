package ca.ualberta.cs.w18t11.whoselineisitanyway;

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
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import java.math.BigDecimal;
import java.util.ArrayList;

public class NavigatorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    /*Taken From: https://gist.github.com/anandbose/7d6efb35c900eaba3b26*/
    private FrameLayout viewStub; //This is the framelayout to keep your content view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (toggle.onOptionsItemSelected(item)) {
            Log.i("OPTIONS: ", "Nav Bar Option Selected");
            return true;
        }

        int id = item.getItemId();
        if (id == R.id.profile) {
            Log.i("OPTIONS: ", "Profile Option Selected");
            // TODO Handle
            return true;
        }
        if (id == R.id.search) {
            Log.i("OPTIONS: ", "Search Option Selected");
            // TODO Handle
            return true;
        }
        if (id == R.id.logout) {
            Log.i("OPTIONS: ", "Logout Option Selected");
            // TODO Handle
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent outgoingIntent = new Intent(this, DetailableListActivity.class);
        String outgoingTitle = "List";
        ArrayList<Detailable> detailables = new ArrayList<>();


        // Hardcoded values for demo: ** Remove later **
        // Get the current user:
        User currentUser = new User(new EmailAddress("bob", "gmail.com"), new PhoneNumber(0, 123, 456, 7890), "bob");
        User providerUserA = new User(new EmailAddress("alice", "gmail.com"), new PhoneNumber(0, 123, 456, 7890), "alice");
        User providerUserB = new User(new EmailAddress("eve", "gmail.com"), new PhoneNumber(0, 123, 456, 7890), "eve");
        // Pull all info for tasks and bids
        //List of tasks:
        Task task1 = new Task("Demo Task 1", "A really good task");
        Task task2 = new Task("Demo Task 2", "A really great task");
        Task task3 = new Task("Demo Task 3", "A alright task");
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);
        allTasks.add(task2);
        allTasks.add(task3);
        // List of the Bids:
        Bid bid1a = new Bid(providerUserA.getID(), "task1ID", new BigDecimal(5));
        Bid bid1b = new Bid(providerUserB.getID(), "task1ID", new BigDecimal(6));
        Bid bid1c = new Bid(currentUser.getID(), "task1ID", new BigDecimal(7));
        Bid bid2a = new Bid(providerUserA.getID(), "task2ID", new BigDecimal(700));
        Bid bid2b = new Bid(providerUserB.getID(), "task2ID", new BigDecimal(500));
        Bid bid2c = new Bid(currentUser.getID(), "task2ID", new BigDecimal(750));
        Bid bid3a = new Bid(providerUserA.getID(), "task3ID", new BigDecimal(5));
        Bid bid3b = new Bid(providerUserB.getID(), "task3ID", new BigDecimal(5));
        Bid bid3c = new Bid(currentUser.getID(), "task3ID", new BigDecimal(7));
        ArrayList<Bid> allBids = new ArrayList<>();
        allBids.add(bid1a);
        allBids.add(bid1b);
        allBids.add(bid1c);
        allBids.add(bid2a);
        allBids.add(bid2b);
        allBids.add(bid2c);
        allBids.add(bid3a);
        allBids.add(bid3b);
        allBids.add(bid3c);

        if (id == R.id.all_tasks) {
            Log.i("NAVBAR: ", "All Tasks Selected");
            outgoingTitle = "All Tasks";
            detailables.addAll(allTasks);

        } else if (id == R.id.my_tasks) {
            Log.i("NAVBAR: ", "My Tasks Selected");
            outgoingTitle = "My Tasks";
            // TODO filter tasks based on requester ID == current user's ID
        } else if (id == R.id.assigned_tasks) {
            Log.i("NAVBAR: ", "Assigned Tasks Selected");
            outgoingTitle = "Assigned Tasks";
            // TODO filter tasks based on provider ID == current user's ID
        } else if (id == R.id.nearby_tasks) {
            Log.i("NAVBAR: ", "Nearby Tasks Selected");
            outgoingTitle = "Nearby Tasks";
            // TODO filter tasks based on location
        } else if (id == R.id.my_bids) {
            Log.i("NAVBAR: ", "My Bids Selected");
            outgoingTitle = "My Bids";
            // TODO filter bids based on provider ID == current user's ID
            for(Bid bid: allBids)
            {
                if(bid.getProviderId() == currentUser.getID())
                {
                    detailables.add(bid);
                }
            }
        } else if (id == R.id.create_task) {
            Log.i("NAVBAR: ", "Create Task Selected");
            // TODO Handle
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        outgoingIntent.putExtra(DetailableListActivity.DATA_TITLE, outgoingTitle);
        outgoingIntent.putExtra(DetailableListActivity.DATA_DETAILABLE_LIST, detailables);

        startActivity(outgoingIntent);
        finish();
        return true;
    }

    /* Override all setContentView methods to put the content view to the FrameLayout view_stub
     * so that, we can make other activity implementations looks like normal activity subclasses.
     * Taken From: https://gist.github.com/anandbose/7d6efb35c900eaba3b26
     */
    @Override
    public void setContentView(int layoutResID) {
        if (viewStub != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, viewStub, false);
            viewStub.addView(stubView, lp);
        }
    }

    @Override
    public void setContentView(View view) {
        if (viewStub != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            viewStub.addView(view, lp);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (viewStub != null) {
            viewStub.addView(view, params);
        }
    }
}
