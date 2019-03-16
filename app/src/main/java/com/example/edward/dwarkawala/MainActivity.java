package com.example.edward.dwarkawala;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Objects;

import Adapters.HomePagerAdapter;
import Models.AccountData;

import static com.example.edward.dwarkawala.CompleteAccount.ACCOUNT_DATA;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public static final String TAG = MainActivity.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton hamNavigation,optionsButton;
    private HomePagerAdapter pagerAdapter;
    private FirebaseAuth mAuth;
    private SharedPreferences preferences;
    public AccountData accountData = null;
    public  DrawerLayout drawer;
    public static GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //if (shouldChangeStatusBarTintToDark) {
//                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            } else {
//                // We want to change tint color to white again.
//                // You can also record the flags in advance so that you can turn UI back completely if
//                // you have set other flags before, such as translucent or full screen.
//                decor.setSystemUiVisibility(0);
//            }
        }

        viewPager = (ViewPager) findViewById(R.id.viewPagerID);
        pagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutID);
        tabLayout.setupWithViewPager(viewPager);
        mAuth = FirebaseAuth.getInstance();
        hamNavigation = (ImageButton) findViewById(R.id.hamButtonID);
        optionsButton = (ImageButton) findViewById(R.id.optionsButtonID);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);


        Gson gson = new Gson();
        if (preferences.contains(ACCOUNT_DATA)){

            String accountJson = preferences.getString(ACCOUNT_DATA, "");
            accountData = gson.fromJson(accountJson, AccountData.class);
        }


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();


        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(MainActivity.this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG,"GoogleApi can't connect");
                    }
                } )
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();



        hamNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }


            }
        });


        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(MainActivity.this,optionsButton);
                popupMenu.getMenuInflater().inflate(R.menu.menu_main,popupMenu.getMenu());



                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.signout_menuID){

                            mAuth.signOut();
                            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                                @Override
                                public void onResult(@NonNull Status status) {
                                    Log.d(TAG,"Sign out successfully");
                                }
                            });

                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);




                        } else if (item.getItemId() == R.id.exit_menuID){

                            finish();

                        }else if (item.getItemId() == R.id.settings_menuID){

                            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                            startActivity(intent);

                        }

                        return true;
                    }
                });

                popupMenu.show();

            }
        });



//        Log.d(TAG,accountData.getName());
//        Log.d(TAG,accountData.getEmail());
//        Log.d(TAG,accountData.getLatitude());
//        Log.d(TAG,accountData.getLongitude());
//        Log.d(TAG,accountData.getPhoneNumber());
//        Log.d(TAG,accountData.getPassword());


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
