package com.example.nowshadhasan.databaselab.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.nowshadhasan.databaselab.R;
import com.example.nowshadhasan.databaselab.another.PrefManager;
import com.example.nowshadhasan.databaselab.fragment.AllDonorFragment;
import com.example.nowshadhasan.databaselab.fragment.ExDonorFragment;
import com.example.nowshadhasan.databaselab.fragment.HistoryFragment;
import com.example.nowshadhasan.databaselab.fragment.HomeFragment;
import com.example.nowshadhasan.databaselab.fragment.UnavailableDonorFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MaterialSearchView.OnQueryTextListener, MaterialSearchView.SearchViewListener {


    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View navHeader;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private AllDonorFragment allDonorFragment;
    private HomeFragment homeFragment;
    private HistoryFragment historyFragment;
    private UnavailableDonorFragment unavailableDonorFragment;
    private ExDonorFragment exDonorFragment;

    public static int navItemIndex = 0;

    private static final String TAG_HOME = "home";
    private static final String TAG_HISTORY = "history";
    private static final String TAG_ALL_DONOR = "allDonor";
    private static final String TAG_UNAVAILABLE = "unavailableDonor";
    private static final String TAG_EX_DONOR = "exDonor";
    public static String CURRENT_TAG = TAG_HOME;

    private String[] activityTitles;

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler handler;

    private String TAG = MainActivity.class.getSimpleName();


    final private String PREF_NAME = "myPref";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private String IPAddress = "27.147.230.14";

    MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        preferences = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

//        editor.putString("IPAddress",IPAddress);
//        editor.commit();


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent addNewDonorIntent = new Intent(MainActivity.this, AddNewDonorActivity.class);
                // editDonorIntent.putExtra("donorID", donorID); //Optional parameters
                MainActivity.this.startActivity(addNewDonorIntent);

            }
        });


        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }


    }


    private void setUpNavigationView() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;

                        break;
                    case R.id.nav_history:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_HISTORY;
                        break;
                    case R.id.nav_all_donor:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_ALL_DONOR;
                        break;
                    case R.id.nav_unavailable_donor:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_UNAVAILABLE;
                        break;
                    case R.id.nav_ex_donor:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_EX_DONOR;
                        break;
                    case R.id.nav_about_us:
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_privacy_statement:
                        startActivity(new Intent(MainActivity.this, PrivacyStatementActivity.class));
                        drawerLayout.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;

                }

                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                item.setChecked(true);

                loadHomeFragment();

                return true;

            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    private void loadHomeFragment() {
        selectNavMenu();

        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();
            toggleFab();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };

        if (mPendingRunnable != null)
            handler.post(mPendingRunnable);
        toggleFab();
        drawerLayout.closeDrawers();
        invalidateOptionsMenu();
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void toggleFab() {
        if (navItemIndex == 0)
            floatingActionButton.show();
        else
            floatingActionButton.hide();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                 historyFragment = new HistoryFragment();
                return historyFragment;
            case 2:
                allDonorFragment = new AllDonorFragment();
                return allDonorFragment;
            case 3:
                 unavailableDonorFragment = new UnavailableDonorFragment();
                return unavailableDonorFragment;
            case 4:
                 exDonorFragment = new ExDonorFragment();
                return exDonorFragment;
            default:
                return new HomeFragment();
        }
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }


        // shouldLoadHomeFragOnBackPress is useless.I also can use  true here.
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.main, menu);

        try{
            MenuItem item = menu.findItem(R.id.action_search);
            searchView.setMenuItem(item);
            searchView.setOnQueryTextListener(this);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }




        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.play_again) {
            PrefManager prefManager = new PrefManager(this);
            prefManager.setFirstTimeLaunch(true);
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));

            finish();
        }


        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();

            PrefManager prefManager = new PrefManager(this);
            prefManager.setFirstTimeLaunch(true);

            preferences = getSharedPreferences("myPref", MODE_PRIVATE);
////            Toast.makeText(getApplicationContext(), String.valueOf(preferences.getBoolean("isLoggedIn",false)), Toast.LENGTH_LONG).show();
//                SharedPreferences.Editor editor;
//                editor=preferences.edit();


            editor.clear();
            editor.commit();


            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            MainActivity.this.startActivity(intent);
            MainActivity.this.finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
//        Toast.makeText(MainActivity.this, "adsrtgs",
//                Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {


//        if(getHomeFragment().getClass().getSimpleName().equals("HomeFragment"))
//        {
////            Toast.makeText(MainActivity.this,"hjasdl",Toast.LENGTH_SHORT).show();
//            homeFragment.makeSearch(newText);
//        }
        if(navItemIndex==0)
        {
            homeFragment.makeSearch(newText);
        }

        else if(navItemIndex==1)
        {
            historyFragment.makeSearch(newText);
        }

      else if (navItemIndex==2) {
            allDonorFragment.makeSearch(newText);
        }

        else if(navItemIndex==3)
        {
            unavailableDonorFragment.makeSearch(newText);
        }


        else if( navItemIndex==4)
        {
            exDonorFragment.makeSearch(newText);
        }

        //  Toast.makeText(MainActivity.this,getHomeFragment().getClass().getSimpleName(),Toast.LENGTH_SHORT).show();
        //  allDonorFragment.makeSearch(newText);
        // homeFragment.makeSearch(newText);


//        Toast.makeText(MainActivity.this, "adsrtgs",
//                Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onSearchViewShown() {
//        Toast.makeText(MainActivity.this, "adsrtgs",
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchViewClosed() {

//        Toast.makeText(MainActivity.this, "adsrtgs",
//                Toast.LENGTH_SHORT).show();

    }
}
