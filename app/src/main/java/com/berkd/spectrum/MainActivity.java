package com.berkd.spectrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this); // implements NavigationItemetc....

        /**
         * This handles all the navigation drawer behaviour (i.e. the three line hamburger menu)
         */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.addDrawerListener(toggle);
        toggle.syncState(); // Take care of rotating the hamburger icon

        setupFragment(savedInstanceState, navigationView); // Called to setup initial fragment view, see function below...
    }

    // ctrl+i
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.hamburger_frequencyResponse:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FrequencyResponseFragment()).commit();
                break;


            case R.id.hamburger_saved:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SavedFragment()).commit();
                break;


            case R.id.hamburger_soundMeter:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MicrophoneFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START); // close drawer when something is selected


        return true; // if false, no item will be selected

    }

    /**
     * ON_BACK_PRESSED
     *  Handle everything when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * ABOUT_POPUP
     *  Just a popup displaying some information, used for the "About" button but currently
     *  here for debugging purposes.
     */
    public void aboutPopup() {
        AboutPopup aboutPopup = new AboutPopup();
        aboutPopup.show(getSupportFragmentManager(), "popup dialog");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * ESSENTIAL FOR MENU CREATION!
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    /**
     * WHEN OPTIONS MENU IS SELECT, CURRENTLY DOES NOT MUCH BUT WIP
     * TODO -> Get these functions things implemented (CURRENTLY WIP)
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            // item1 = Clear List
            case R.id.item1:
                aboutPopup();
                break;

            // item3 = FontSize
            case R.id.item2:
                aboutPopup();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * SETUP_FRAGMENT
     *  Initial setup for setting up fragment. Required whenever device rotates or is created initially.
     *  Also, it will DEFAULT to Frequency Response window.
     *
     * @param savedInstanceState
     * @param navigationView
     */
    public void setupFragment(Bundle savedInstanceState, NavigationView navigationView) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FrequencyResponseFragment()).commit();
            navigationView.setCheckedItem(R.id.hamburger_frequencyResponse); // needed when app rotates
        }
    }

}
