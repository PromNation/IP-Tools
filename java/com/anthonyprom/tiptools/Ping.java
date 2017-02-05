package com.anthonyprom.tiptools;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ping extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle = "Ping";

    private String pingAddress = "";

    private EditText terminal;
    private EditText pingThis;

    private Button pingExe;

    private View thisView;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping);
        thisView = getWindow().getDecorView();

        Intent intent = getIntent();

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

        terminal = (EditText)findViewById(R.id.pingTextField);
        terminal.setBackgroundColor(Color.BLACK);
        terminal.setTextColor(Color.WHITE);

        pingThis = (EditText)findViewById(R.id.pingAddTextField);

        pingExe = (Button)findViewById(R.id.exePing);
        pingExe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pingAddress = pingThis.getText().toString();
               // new Thread(new Runnable(){
                  //  public void run(){

                  //  }
                //})
                ping(pingAddress);
            }
        });


        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        adView = (AdView)findViewById(R.id.adView4);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        navigationView.getMenu().getItem(1).setChecked(true);
    }

    public String ping(String url){
        terminal.append("\n \n");
        String s = "";
        String inputLine;
        try{
            Process p = Runtime.getRuntime().exec(
                    "/system/bin/ping -c 4 " + url
            );
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            inputLine = reader.readLine();
            while((inputLine != null)){
                if(inputLine.length() > 0 && inputLine.contains("avg")){
                    break;
                }
                terminal.append(inputLine + "\n");
                inputLine = reader.readLine();
            }

            //int i;
            //char[] buffer = new char[4096];
           // StringBuffer output = new StringBuffer();
            //while((i = reader.read(buffer)) > 0){
            //    output.append(buffer, 0, i);
            //}
            reader.close();
            //s = output.toString();
            //terminal.setText(s);

        }
        catch(IOException e){
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calc) {
            Intent intent = new Intent(this, IPCalculator.class);
            startActivity(intent);
        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(this, Info.class);
            startActivity(intent);
        } else if (id == R.id.nav_devicefinder) {
            Intent intent = new Intent(this, Device_Finder.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
