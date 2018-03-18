package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class NavigatorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

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

        if (id == R.id.all_tasks) {
            Log.i("NAVBAR: ", "All Tasks Selected");
            // TODO Handle
        } else if (id == R.id.my_tasks) {
            Log.i("NAVBAR: ", "My Tasks Selected");
            // TODO Handle
        } else if (id == R.id.assigned_tasks) {
            Log.i("NAVBAR: ", "Assigned Tasks Selected");
            // TODO Handle
        } else if (id == R.id.nearby_tasks) {
            Log.i("NAVBAR: ", "Nearby Tasks Selected");
            // TODO Handle
        } else if (id == R.id.my_bids) {
            Log.i("NAVBAR: ", "My Bids Selected");
            // TODO Handle
        } else if (id == R.id.create_task) {
            Log.i("NAVBAR: ", "Create Task Selected");
            // TODO Handle
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
