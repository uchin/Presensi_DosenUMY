package com.bsi.myapplication;


import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;

public class MainPrecense extends AppCompatActivity {


    TextView ndosen, nprodi;
    TextClock jam, tanggal;
    Button babsen, bdetail, bPulang;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main_precense);

        ndosen = (TextView) findViewById(R.id.dosen);
        nprodi = (TextView) findViewById(R.id.prodi);
        jam = (TextClock) findViewById(R.id.jam);
        tanggal = (TextClock) findViewById(R.id.tanggal);
        babsen = (Button) findViewById(R.id.btnCeckIn);
        bPulang = (Button) findViewById(R.id.btnCeckOut);
        bdetail = (Button) findViewById(R.id.btnDetails);

        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setIcon(R.drawable.ic_action_name);


        jam.setFormat12Hour("kk:mm:ss");
        jam.setTimeZone("GMT+07:00");

        tanggal.setFormat12Hour("EEEE dd MMMM yyyy");
        tanggal.setTimeZone("GMT+07:00");



        bPulang.setVisibility(View.GONE);




        //Display the IP address, obtained from function getIPAddress, using textview
        //tv.setText(getIPAddress());
        getIPAddress();


        babsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                babsen.setVisibility(View.GONE);
                bPulang.setVisibility(View.VISIBLE);

                Date date = new Date();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
                String nowTime = timeFormat.format(date);

                Toast bread = Toast.makeText(getApplicationContext(), "Anda Datang jam "+nowTime, Toast.LENGTH_LONG);
                bread.show();
            }
        });

        bPulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                babsen.setVisibility(View.VISIBLE);
                bPulang.setVisibility(View.GONE);

                Date date = new Date();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
                String nowTime = timeFormat.format(date);

                Toast bread = Toast.makeText(getApplicationContext(), "Anda pulang jam "+nowTime, Toast.LENGTH_LONG);
                bread.show();
            }
        });


        bdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(MainPrecense.this, DetailPresensi.class);
                startActivity(i);
            }
        });
    }


    public String getIPAddress() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ipAddress = inetAddress.getHostAddress();
                        Log.e("IP address", "" + ipAddress);
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("MainPrecense", ex.toString());
        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        /*get popup from res>menu*/
        inflater.inflate(R.menu.activity_main_precense_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Ask the user if they want to quit
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.quit)
                    .setMessage(R.string.really_quit)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Stop the activity
                            MainPrecense.this.finish();
                        }

                    })
                    .setNegativeButton(R.string.no, null)
                    .show();

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    /*list menu actionbar*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_help: {
                Intent r = new Intent(MainPrecense.this, HelpActivity.class);
                startActivity(r);
            }
            return true;
            case R.id.sliderImg: {
                Intent r = new Intent(MainPrecense.this, Connect365.class);
                startActivity(r);
            }
            return true;
            case R.id.detailPresen: {
                Intent r = new Intent(MainPrecense.this, DetailPresensi.class);
                startActivity(r);
            }
            return true;
            case R.id.action_exit: {

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.quit)
                        .setMessage(R.string.really_quit)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Stop the activity
                                MainPrecense.this.finish();
                            }

                        })
                        .setNegativeButton(R.string.no, null)
                        .show();

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
