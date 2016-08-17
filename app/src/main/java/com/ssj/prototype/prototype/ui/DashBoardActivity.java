package com.ssj.prototype.prototype.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ssj.prototype.prototype.R;
import com.ssj.prototype.prototype.adapters.DashBoardListArrayAdapter;
import com.ssj.prototype.prototype.database.GarageDataSource;
import com.ssj.prototype.prototype.developer.DebugActivity;
import com.ssj.prototype.prototype.model.Maintenance;
import com.ssj.prototype.prototype.model.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private GarageDataSource garageDatasource;
    private ArrayList<Maintenance> maintenances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Create the database connections
        garageDatasource = new GarageDataSource(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Add the listView listener
        ListView listView = (ListView) findViewById(R.id.listView);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickOnEntry(position);
            }
        });

        populateList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        populateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateList();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_garage) {
            Intent intent = new Intent(this, GarageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     *
     */
    private void populateList() {
        garageDatasource.open();
        maintenances = garageDatasource.getAllMaintenance();

        Collections.sort(maintenances, new Comparator<Maintenance>() {
            public int compare(Maintenance maintenance1, Maintenance maintenance2) {
                return maintenance1.getDueIn() > maintenance2.getDueIn() ? +1 : maintenance1.getDueIn() < maintenance2.getDueIn() ? -1 : 0;
            }
        });

        ((ListView) findViewById(R.id.listView)).setAdapter(new DashBoardListArrayAdapter(this, maintenances));
        //((ListView) findViewById(R.id.listView)).setAdapter(new DashBoardListArrayAdapter(this, garageDatasource.getAllMaintenanceActivities(), null));
        garageDatasource.close();
    }

    /**
     * @param position
     */
    private void clickOnEntry(int position) {
        Intent intent = new Intent(this, MaintenanceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id", maintenances.get(position).getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
