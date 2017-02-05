package com.anthonyprom.tiptools;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class IPCalculator extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int[] ipIntArr = {192, 168, 0, 1};//Integer array containing the IP address

    private String[] ipStrArr = {"192", "168", "0", "1"};//Integer array containing the IP address

    private int submaskDash = 24;//Integer for Dash

    private int[] subnetIntArr = {255, 255, 255, 0};//Integer array containing the subnet

    private String[] subnetStrArr = {"255", "255", "255", "0"};//String array containing the Subnet

    private int subnetBits = 0;//Integer for subnet bits
    private int maxBits = 1;//Integer for max subnet bits
    private int hostBits = 8;//Integer for host bits
    private int maxHosts = 254;//Integer for max host bits

    private String fullSubMask;
    private String invertedMask;

    private String[] binaryStrIpAdd = new String[4];
    private String[] binaryStrSubmask = new String[4];

    private int[] broadcastIntArr = {192, 168, 0, 255};//Integer array containing the Broadcast address

    private String[] broadcastStrArr = {"192", "168", "0", "255"};//Integer array containing the Broadcast address

    private String hostRangeBegin = "192.168.0.1";//String for beginning of the host range
    private String hostRangeEnd = "192.168.0.254";//String for end of the host range

    //Text Fields
    private EditText seekText;
    private EditText ip1;
    private EditText ip2;
    private EditText ip3;
    private EditText ip4;
    private EditText sub1;
    private EditText sub2;
    private EditText sub3;
    private EditText sub4;
    private EditText net1;
    private EditText net2;
    private EditText net3;
    private EditText net4;
    private EditText broad1;
    private EditText broad2;
    private EditText broad3;
    private EditText broad4;
    private EditText subBits;
    private EditText maxSubBits;
    private EditText hostBitsVal;
    private EditText maxHostBits;
    private EditText netRange;

    //Seekbar
    private SeekBar seekBar;

    //Advertisement
    private AdView mAdView;

    //Navigation drawer stuff
    private CharSequence mTitle = "Subnet Calculator";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipcalculator);

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

        seekText = (EditText)findViewById(R.id.textSubmask);
        ip1 = (EditText)findViewById(R.id.textIP1);
        ip2 = (EditText)findViewById(R.id.textIP2);
        ip3 = (EditText)findViewById(R.id.textIP3);
        ip4 = (EditText)findViewById(R.id.textIP4);
        sub1 = (EditText)findViewById(R.id.sub1);
        sub2 = (EditText)findViewById(R.id.sub2);
        sub3 = (EditText)findViewById(R.id.sub3);
        sub4 = (EditText)findViewById(R.id.sub4);
        net1 = (EditText)findViewById(R.id.net1);
        net2 = (EditText)findViewById(R.id.net2);
        net3 = (EditText)findViewById(R.id.net3);
        net4 = (EditText)findViewById(R.id.net4);
        broad1 = (EditText)findViewById(R.id.broad1);
        broad2 = (EditText)findViewById(R.id.broad2);
        broad3 = (EditText)findViewById(R.id.broad3);
        broad4 = (EditText)findViewById(R.id.broad4);
        subBits = (EditText)findViewById(R.id.subBitsVal);
        maxSubBits = (EditText)findViewById(R.id.maxSubVal);
        hostBitsVal = (EditText)findViewById(R.id.hostVal);
        maxHostBits = (EditText)findViewById(R.id.maxHostVal);
        netRange = (EditText)findViewById(R.id.range);

        seekBar = (SeekBar)findViewById(R.id.submaskBar);

        calcSubmask();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekText.setText("/" + i);
                submaskDash = i;
                calcSubmask();
                calculate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(4).setChecked(true);
    }

    public void init(){

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ipcalculator, menu);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Helper method for converting to binary
    public String convertToBinary(int x){
        String binary = Integer.toBinaryString(x);
        String zeros = "";
        for(int i = 0; i < (8 - binary.length()); i++){
            zeros += "0";
        }
        binary = zeros.concat(binary);
        return binary;
    }

    //Helper method for counting the number of '1's
    public int countOnes(String bin){
        int count = 0;
        for(char c : bin.toCharArray()){
            if(c == '1'){
                count++;
            }
        }
        return count;
    }

    //Helper method for creating binary submask
    public String getSubmask(int x){
        String mask = "";
        for(int i = 0; i < 32; i++){
            if(i < x){
                mask += 1;
            }
            else{
                mask += 0;
            }
        }
        return mask;
    }

    //Helper method for inverting binary
    public String invert(String x){
        String newString = "";
        for(char c : x.toCharArray()){
            if(c == '1'){
                newString += "0";
            }
            else{
                newString += "1";
            }
        }
        return newString;
    }

    public void calcSubmask(){
        //Calculate submask////////////////////////////////////
        fullSubMask = getSubmask(submaskDash);

        binaryStrSubmask[0] = fullSubMask.substring(0, 8);
        binaryStrSubmask[1] = fullSubMask.substring(8, 16);
        binaryStrSubmask[2] = fullSubMask.substring(16, 24);
        binaryStrSubmask[3] = fullSubMask.substring(24, 32);

        subnetIntArr[0] = Integer.parseInt(binaryStrSubmask[0], 2);
        subnetIntArr[1] = Integer.parseInt(binaryStrSubmask[1], 2);
        subnetIntArr[2] = Integer.parseInt(binaryStrSubmask[2], 2);
        subnetIntArr[3] = Integer.parseInt(binaryStrSubmask[3], 2);

        sub1.setText(subnetIntArr[0] + "");
        sub2.setText(subnetIntArr[1] + "");
        sub3.setText(subnetIntArr[2] + "");
        sub4.setText(subnetIntArr[3] + "");
        //End submask//////////////////////////////////////////
    }


    //Calculate
    public void calculate(){
        //Calculate network address////////////////////////////
        ipStrArr[0] = ip1.getText().toString();
        ipStrArr[1] = ip2.getText().toString();
        ipStrArr[2] = ip3.getText().toString();
        ipStrArr[3] = ip4.getText().toString();
        ipIntArr[0] = Integer.parseInt(ipStrArr[0]);
        ipIntArr[1] = Integer.parseInt(ipStrArr[1]);
        ipIntArr[2] = Integer.parseInt(ipStrArr[2]);
        ipIntArr[3] = Integer.parseInt(ipStrArr[3]);

        binaryStrIpAdd[0] = convertToBinary(ipIntArr[0]);
        binaryStrIpAdd[1] = convertToBinary(ipIntArr[1]);
        binaryStrIpAdd[2] = convertToBinary(ipIntArr[2]);
        binaryStrIpAdd[3] = convertToBinary(ipIntArr[3]);

        subnetStrArr[0] = ANDIT(binaryStrSubmask[0], binaryStrIpAdd[0]);
        subnetStrArr[1] = ANDIT(binaryStrSubmask[1], binaryStrIpAdd[1]);
        subnetStrArr[2] = ANDIT(binaryStrSubmask[2], binaryStrIpAdd[2]);
        subnetStrArr[3] = ANDIT(binaryStrSubmask[3], binaryStrIpAdd[3]);

        net1.setText("" + Integer.parseInt(subnetStrArr[0], 2));
        net2.setText("" + Integer.parseInt(subnetStrArr[1], 2));
        net3.setText("" + Integer.parseInt(subnetStrArr[2], 2));
        net4.setText("" + Integer.parseInt(subnetStrArr[3], 2));

        //End network address//////////////////////////////////

        //Calculate Broadcast address//////////////////////////
        invertedMask = invert(fullSubMask);
        String odin = invertedMask.substring(0, 8);
        String dva = invertedMask.substring(8, 16);
        String tri = invertedMask.substring(16, 24);
        String chetvertyy = invertedMask.substring(24, 32);
        broadcastIntArr[0] = ORIT(binaryStrIpAdd[0], odin);
        broadcastIntArr[1] = ORIT(binaryStrIpAdd[1], dva);
        broadcastIntArr[2] = ORIT(binaryStrIpAdd[2], tri);
        broadcastIntArr[3] = ORIT(binaryStrIpAdd[3], chetvertyy);

        broad1.setText("" + broadcastIntArr[0]);
        broad2.setText("" + broadcastIntArr[1]);
        broad3.setText("" + broadcastIntArr[2]);
        broad4.setText("" + broadcastIntArr[3]);

        //End broadcast address////////////////////////////////

        //Calculate Host number///////////////////////////////
        subnetBits = countOnes((fullSubMask.substring(24, 32)));
        subBits.setText("" + subnetBits);

        hostBits = (32 - countOnes(fullSubMask));
        hostBitsVal.setText("" + hostBits);

        //End host number/////////////////////////////////////

        //Calculate max hosts/////////////////////////////////
        int host = (32 - submaskDash);
        maxHosts = (int)(Math.pow(2, host) - 2);
        if(maxHosts < 0){
            maxHosts = 0;
        }
        maxHostBits.setText("" + maxHosts);


        //End max hosts///////////////////////////////////////

        //Calculate Range/////////////////////////////////////
        int first = Integer.parseInt((ANDIT(binaryStrIpAdd[0], odin)), 2);
        int second = Integer.parseInt((ANDIT(binaryStrIpAdd[1], dva)), 2);
        int third = Integer.parseInt((ANDIT(binaryStrIpAdd[2], tri)), 2);
        int fourth = Integer.parseInt((ANDIT(binaryStrIpAdd[3], chetvertyy)), 2);
        hostRangeBegin = first + "." + second + "." + third + "." + fourth;
        hostRangeEnd = broadcastIntArr[0] + "." + broadcastIntArr[1] + "." + broadcastIntArr[2] + "." + broadcastIntArr[3];

        netRange.setText(hostRangeBegin + " - " + hostRangeEnd);
        //End range//////////////////////////////////////////



    }

    //Helper method for calculating AND
    public String ANDIT(String x, String y) {
        String r = "";
        for(int i = 0; i < x.length(); i++) {
            if (x.charAt(i) == '1') {
                if (y.charAt(i) == '1') {
                    r += "1";
                }
                else{
                    r += "0";
                }
            } else {
                r += "0";
            }
        }
        return r;
    }

    public int ORIT(String x, String y){
        String r = "";
        for(int i = 0; i < x.length(); i++) {
            if (x.charAt(i) == '1') {
                r += "1";
            }
            else if(y.charAt(i) == '1'){
                r += "1";
            }
            else{
                r += "0";
            }
        }
        return (Integer.parseInt(r, 2));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ping) {
            Intent intent = new Intent(this, Ping.class);
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
