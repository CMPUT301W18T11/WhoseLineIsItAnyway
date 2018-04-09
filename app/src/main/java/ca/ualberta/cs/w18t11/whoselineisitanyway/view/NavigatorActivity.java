package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder.AllTasksListBuilder;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder.AssignedTasksListBuilder;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder.DetailedListBuilder;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder.MyBidTasksListBuilder;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder.MyBidsListBuilder;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder.MyTasksListBuilder;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder.NearbyTasksListBuilder;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.TaskStatus;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * A base class which includes a navigation drawer and options menu.
 * Simply extend this activity from any activity you want to have a navigation drawer.
 */
public class NavigatorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    @NonNull
    private final DataSourceManager dataSourceManager = new DataSourceManager(this);

    @Nullable
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_list);

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        this.toggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        this.toggle.syncState();

        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);

        final ActionBar actionBar = this.getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public final void onBackPressed()
    {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public final boolean onCreateOptionsMenu(@NonNull final Menu menu)
    {
        final User currentUser = this.dataSourceManager.getCurrentUser();
        assert currentUser != null;
        ((TextView) findViewById(R.id.drawer_title)).setText(
                String.format(Locale.getDefault(), "Welcome, %s!", currentUser.getUsername()));
        this.getMenuInflater().inflate(R.menu.list, menu);

        return true;
    }

    @Override
    public final boolean onOptionsItemSelected(@NonNull final MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        assert this.toggle != null;

        if (toggle.onOptionsItemSelected(item))
        {
            Log.i("OPTIONS: ", "Nav Bar Option Selected");

            return true;
        }

        switch (item.getItemId())
        {
            case R.id.profile:
                Log.i("OPTIONS: ", "Profile Option Selected");
                final User currentUser = this.dataSourceManager.getCurrentUser();
                assert currentUser != null;
                final Intent intent = new Intent(this, UserProfileActivity.class);
                final Bundle bundle = new Bundle();
                bundle.putString(UserProfileActivity.DATA_EXISTING_USERNAME,
                        currentUser.getUsername());
                intent.putExtras(bundle);
                this.startActivity(intent);

                return true;
            case R.id.search:
                Log.i("OPTIONS: ", "Search Option Selected");
                // TODO
                return true;
            case R.id.signOut:
                Log.i("OPTIONS: ", "Logout Option Selected. Attempting to logout...");
                this.dataSourceManager.unsetCurrentUser();
                this.startActivity(new Intent(this, UserLoginActivity.class));
                this.finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public final boolean onNavigationItemSelected(@NonNull final MenuItem item)
    {
        Intent intent;

        if (item.getItemId() != R.id.create_task)
        {
            Detailed[] tasks = null;
            Detailed[] bids = null;
            String title = null;
            DetailedListBuilder builder = null;

            switch (item.getItemId())
            {
                case R.id.all_tasks:
                    tasks = new AllTasksListBuilder().buildDetailedList(this);
                    title = "All Tasks";
                    builder = new AllTasksListBuilder();
                    break;

                case R.id.my_tasks:
                    tasks = new MyTasksListBuilder().buildDetailedList(this);
                    title = "My Tasks";
                    builder = new MyTasksListBuilder();
                    break;

                case R.id.assigned_tasks:
                    tasks = new AssignedTasksListBuilder().buildDetailedList(this);
                    title = "Assigned Tasks";
                    builder = new AssignedTasksListBuilder();
                    break;

                case R.id.nearby_tasks:
                    tasks = new NearbyTasksListBuilder().buildDetailedList(this);
                    title = "Nearby Tasks";
                    builder = new NearbyTasksListBuilder();
                    break;

                case R.id.tasks_bidded:
                    tasks = new MyBidTasksListBuilder().buildDetailedList(this);
                    title = "Tasks I've Bidded On";
                    builder = new MyBidTasksListBuilder();
                    break;

                case R.id.my_bids:
                    bids = new MyBidsListBuilder().buildDetailedList(this);
                    title = "My Bids";
                    builder = new MyBidsListBuilder();
                    break;

                default:
                    break;
            }

            assert title != null;
            intent = new Intent(this, DetailedListActivity.class);
            intent.putExtra(DetailedListActivity.DATA_TITLE, title);
            intent.putExtra(DetailedListActivity.DATA_LIST_BUILDER, builder);

            if (tasks != null)
            {
                intent.putExtra(DetailedListActivity.DATA_DETAILABLE_ADAPTER_TYPE,
                        AdapterType.TASK);
                intent.putExtra(DetailedListActivity.DATA_DETAILABLE_LIST, tasks);
            }
            else
            {
                intent.putExtra(DetailedListActivity.DATA_DETAILABLE_ADAPTER_TYPE, AdapterType.BID);
                intent.putExtra(DetailedListActivity.DATA_DETAILABLE_LIST, bids);
            }
        }
        else
        {
            intent = new Intent(this, CreateModifyTaskActivity.class);
        }

        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
        this.startActivity(intent);

        return true;
    }

    // Override all setContentView methods to put the content view to the FrameLayout view_stub
    // so that, we can make other activity implementations looks like normal activity subclasses.
    // Taken From: https://gist.github.com/anandbose/7d6efb35c900eaba3b26
    @Override
    public final void setContentView(final int layoutResID)
    {
        // Do nothing here.
    }

    @Override
    public final void setContentView(@NonNull final View view)
    {
        // Do nothing here.
    }

    @Override
    public final void setContentView(@NonNull final View view,
                                     @NonNull final ViewGroup.LayoutParams params)
    {
        // Do nothing here.
    }

    @NonNull
    private Task[] getAllTasks()
    {
        final Task[] tasks = this.dataSourceManager.getTasks();

        if (tasks == null)
        {
            return new Task[0];
        }

        return tasks;
    }

    @NonNull
    private Task[] getMyTasks()
    {
        final Task[] tasks = this.dataSourceManager.getTasks();
        final User currentUser = new DataSourceManager(this).getCurrentUser();
        assert tasks != null && currentUser != null;
        final ArrayList<Task> myTasks = new ArrayList<>(tasks.length);

        for (Task task : tasks)
        {
            if (task.getRequesterUsername().equals(currentUser.getUsername()))
            {
                myTasks.add(task);
            }
        }

        return myTasks.toArray(new Task[myTasks.size()]);
    }

    @NonNull
    private Task[] getAssignedTasks()
    {
        final Task[] tasks = this.dataSourceManager.getTasks();
        final User currentUser = new DataSourceManager(this).getCurrentUser();
        assert tasks != null && currentUser != null;
        final ArrayList<Task> assignedTasks = new ArrayList<>(tasks.length);

        for (Task task : tasks)
        {
            if (task.getStatus() == TaskStatus.ASSIGNED || task.getStatus() == TaskStatus.DONE)
            {
                final String provider = task.getProviderUsername();
                assert provider != null;

                if (provider.equals(task.getProviderUsername()))
                {
                    assignedTasks.add(task);
                }
            }
        }

        return assignedTasks.toArray(new Task[assignedTasks.size()]);
    }

    @NonNull
    private Task[] getNearbyTasks()
    {
        // TODO
        return new Task[0];
    }

    @NonNull
    private Task[] getTasksBidded()
    {
        // TODO: Check if we should only display bids on tasks that are currently unassigned?
        final Bid[] bids = this.dataSourceManager.getBids();
        final User currentUser = new DataSourceManager(this).getCurrentUser();
        assert bids != null && currentUser != null;
        final ArrayList<Task> tasksBidded = new ArrayList<>(bids.length);

        for (Bid bid : bids)
        {
            if (bid.getProviderUsername().equals(currentUser.getUsername()))
            {
                tasksBidded.add(this.dataSourceManager.getTask(bid.getTaskId()));
            }
        }

        return tasksBidded.toArray(new Task[tasksBidded.size()]);
    }

    @NonNull
    private Bid[] getMyBids()
    {
        // TODO: Check if we should only display bids on tasks that are currently unassigned?
        final Bid[] bids = this.dataSourceManager.getBids();
        final User currentUser = new DataSourceManager(this).getCurrentUser();
        assert bids != null && currentUser != null;
        final ArrayList<Bid> myBids = new ArrayList<>(bids.length);

        for (Bid bid : bids)
        {
            if (bid.getProviderUsername().equals(currentUser.getUsername()))
            {
                myBids.add(bid);
            }
        }

        return myBids.toArray(new Bid[myBids.size()]);
    }
}
