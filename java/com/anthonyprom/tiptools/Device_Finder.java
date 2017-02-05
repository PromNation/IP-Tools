package com.anthonyprom.tiptools;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class Device_Finder extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle = "Device Finder";

    private WifiManager wifi;
    private WifiInfo wifiInfo;
    private String networkName;
    private int networkIP;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device__finder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(mTitle);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if(savedInstanceState == null){
            //selectItem(0);
        }

        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(2).setChecked(true);

        getInfo();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_ping) {
            Intent intent = new Intent(this, Ping.class);
            startActivity(intent);
        } else if (id == R.id.nav_calc) {
            Intent intent = new Intent(this, IPCalculator.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getInfo(){
        wifi = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifi.getConnectionInfo();
        networkName = wifiInfo.getSSID();
        networkIP = wifiInfo.getIpAddress();
    }
}
