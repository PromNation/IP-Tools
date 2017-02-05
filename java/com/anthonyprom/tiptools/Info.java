package com.anthonyprom.tiptools;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

public class Info extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle = "Info";

    private WifiManager wifi;
    private WifiInfo wifiInfo;
    private String networkName;
    private String publicIP;
    private int networkIP;
    private String networkMAC;
    private int signalStr;
    private int linkSpeed;
    private int frequency;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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

        navigationView.getMenu().getItem(0).setChecked(true);

        getInfo();
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

    public boolean onCreateOptionsMenu(Info info) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, (Menu) info);
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

        if (id == R.id.nav_ping) {
            Intent intent = new Intent(this, Ping.class);
            startActivity(intent);
        } else if (id == R.id.nav_calc) {
            Intent intent = new Intent(this, IPCalculator.class);
            startActivity(intent);
        } else if (id == R.id.nav_devicefinder) {
            Intent intent = new Intent(this, Device_Finder.class);
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
        networkMAC = wifiInfo.getBSSID();
        //frequency = wifiInfo.getFrequency();//Needs API 21
        networkIP = wifiInfo.getIpAddress();
        signalStr = wifiInfo.getRssi();
        linkSpeed = wifiInfo.getLinkSpeed();


        //Todo: Get logs working
        //Todo: Get internet provider location information



        Log.i("TIP", "///////////////////////////////////////////////////////////////////");
        Log.i("TIP", networkName);
        Log.i("TIP", "" + networkMAC);
        Log.i("TIP", "" + networkIP);
        Log.i("TIP", "" + signalStr);
        Log.i("TIP", "" + linkSpeed);


    }

}
