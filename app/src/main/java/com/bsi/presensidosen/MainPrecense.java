package com.bsi.presensidosen;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import com.androidquery.AQuery;
import com.bsi.presensidosen.helper.AppKey;
import com.bsi.presensidosen.helper.ResponseHandler;
import com.bsi.presensidosen.helper.SessionManagement;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.util.Log.d;
import static com.bsi.presensidosen.R.id.btnDetails;
import static com.bsi.presensidosen.R.id.ip;
import static com.bsi.presensidosen.R.id.jamServer;
import static com.bsi.presensidosen.helper.AppKey.getURLImageUmy;
import static com.bsi.presensidosen.helper.AppKey.getURLImageUmyEmpty;

//import static com.bsi.presensidosen.R.id.btnDetailsGone;

public class MainPrecense extends AppCompatActivity implements ResponseHandler.ResponseListener {
    private static final String LOG = "MainPrecense";
    final Context context = this;
    ProgressDialog pDialog;
    //AlertDialog alertDialog;
    final ThreadLocal<ResponseHandler> handler = new ThreadLocal<>();

    public Thread tj, io;

    int CODE_CHECKIN = 83;
    int CODE_CHECKOUT = 85;
    int CODE_IP = 86;
    int CODE_JAM = 87;
    int CODE_CHECKLAST = 88;
    int CEK_VERSI = 89;

    //public final String ippublicUMY = "103.251.182.254";
    //public final String ippublicUMY = "10.20.10.205";
    //public final String ippublicUMY = "36.81.30.106";

    //public final String ippublicUMY = "103.251.183.1";
    //public final String ippublicUMY = "114.79.47.47";

    TextView ndosen, nprodi, ipp, tjamserver, ttanggalserver;
    TextClock jam, tanggal;
    Button babsen, bdetail, bdetailGone, bPulang;
    CircleImageView imgFoto;

    //String msgValidation;
    String serverError;

    SessionManagement session;

    public AQuery aq, aq2;
    private final String TAG = MainPrecense.class.getSimpleName();
    private Timer autoUpdate;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_precense);

        session = new SessionManagement(getApplicationContext());

        ndosen = (TextView) findViewById(R.id.dosen);
        nprodi = (TextView) findViewById(R.id.prodi);
        ipp = (TextView) findViewById(ip);
        tjamserver = (TextView) findViewById(jamServer);
        ttanggalserver = (TextView) findViewById(R.id.tanggalserver);
        jam = (TextClock) findViewById(R.id.jam);
        tanggal = (TextClock) findViewById(R.id.tanggal);
        babsen = (Button) findViewById(R.id.btnCeckIn);
        bPulang = (Button) findViewById(R.id.btnCeckOut);
        bdetail = (Button) findViewById(btnDetails);
       // bdetailGone = (Button) findViewById(btnDetailsGone);
        imgFoto = (CircleImageView) findViewById(R.id.view_dosen);

        aq = new AQuery(this);
        aq2 = new AQuery(this);

        session.checkLogin();



        HashMap<String, String> user = session.getUserDetails();
        String photos = user.get(SessionManagement.KEY_PHOTOS);
        String idphotos = user.get(SessionManagement.KEY_ID) + ".JPG";

        if (idphotos.equals(photos)) {
            aq.id(imgFoto).image(getURLImageUmy(photos));
        } else {
            aq2.id(imgFoto).image(getURLImageUmyEmpty());
        }

        String nama = user.get(SessionManagement.KEY_NAMA_LENGKAP);
        ndosen.setText(nama);

        String nama_prodi = user.get(SessionManagement.KEY_NAMA_PRODI);
        nprodi.setText(nama_prodi);

        SharedPreferences prefs = getSharedPreferences("PresensiDosen", Context.MODE_PRIVATE);
        final String absen = prefs.getString("ABSEN", null);

        /*pDialog = new ProgressDialog(MainPrecense.this);
        pDialog.setTitle("Loading...");
        pDialog.setMessage("Please wait.");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();*/


        /*assert username != null;*/
        if (absen != null && absen.equals("yes")) {
            babsen.setVisibility(View.GONE);
            bPulang.setVisibility(View.VISIBLE);
        } else {
            bdetail.setVisibility(View.VISIBLE);
        }

        user = session.getUserDetails();
        int idpegawai = Integer.parseInt(user.get(SessionManagement.KEY_ID));
        handler.set(new ResponseHandler(
                AppKey.getLastPrecense(idpegawai), CODE_CHECKLAST, this));
        handler.get().execute();

        getIPAddress();
        //cekKoneksiInternet();


        ipp.setText("");


        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setIcon(R.drawable.ic_action_name);

        jam.setFormat12Hour("kk:mm:ss");
        jam.setTimeZone("GMT+07:00");

        tanggal.setFormat12Hour("EEEE dd MMMM yyyy");
        tanggal.setTimeZone("GMT+07:00");

        jam.setVisibility(View.GONE);
        tanggal.setVisibility(View.GONE);

        babsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                doAbsenMasuk();
            }
        });

        bPulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                doAbsenPulang();

            }
        });



        tj = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateJamServer();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        tj.start();

        /*io = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cekKoneksiInternet();
                                getIPpublic();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        io.start();*/


        bdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                tj.interrupt();
                //io.interrupt();
                Intent i = new Intent(MainPrecense.this, DetailPresensi.class);
                startActivity(i);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        String app_version = "2.0.1";


        //bdetail.setVisibility(View.GONE);
    }

    private void toLoginActivity() {
        Intent i = new Intent(MainPrecense.this, LoginActivity.class);
        startActivity(i);
        finish();
    }


    private void doAbsenMasuk() {
        tj.interrupt();
        //io.interrupt();

        HashMap<String, String> user = session.getUserDetails();
        int id_pegawai = Integer.parseInt(user.get(SessionManagement.KEY_ID));
        String ipmasuk = getIPAddress();

        pDialog = new ProgressDialog(MainPrecense.this);
        pDialog.setTitle("Loading...");
        pDialog.setMessage("Please wait.");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        long tanggalku = 0;
        long jamdatang = 0;
        handler.set(new ResponseHandler(
                AppKey.getAddCheckIn(id_pegawai, tanggalku, jamdatang, ipmasuk), CODE_CHECKIN, this));
        handler.get().execute();
    }

    private void doAbsenPulang() {

        tj.interrupt();
        //io.interrupt();

        HashMap<String, String> user = session.getUserDetails();
        int id_pegawai = Integer.parseInt(user.get(SessionManagement.KEY_ID));

        Log.d("IDPRESS", String.valueOf(id_pegawai));

        pDialog = new ProgressDialog(MainPrecense.this);
        pDialog.setTitle("Loading...");
        pDialog.setMessage("Please wait.");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        long jampulang = 0;
        long tanggalku = 0;
        String ip_keluar = getIPAddress();

        handler.set(new ResponseHandler(
                AppKey.getAddCheckOut(id_pegawai, tanggalku, jampulang, ip_keluar), CODE_CHECKOUT, this));
        handler.get().execute();

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onSuccess(JSONObject jObject, int reqCode) {
        //pDialog.dismiss();
        if (reqCode == CODE_CHECKIN) {
            tj.interrupt();
            //io.interrupt();
            //pDialog.dismiss();
            if (jObject != null) {
                //Log.d(TAG, "CHECKIN" + jObject.toString());
                try {
                    if (jObject.getBoolean(AppKey.KEY_STATUS)) {
                        showErrorAlert(jObject.getString("message"));
                        session.sudahAbsen();
                        babsen.setVisibility(View.GONE);
                        bPulang.setVisibility(View.VISIBLE);
                    } else {
                        showErrorAlert(jObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    serverError("Gagal terhubung ke server, silahkan coba beberapa saat lagi");
                } else {
                    serverError("Wifi belum aktif!, Pastikan koneksi wifi aktif dan terhubung ke salah satu wifi UMY");
                }
            }
        } else if (reqCode == CODE_CHECKOUT) {
            tj.interrupt();
            //io.interrupt();
            //pDialog.dismiss();
            if (jObject != null) {
                try {
                    if (jObject.getBoolean(AppKey.KEY_STATUS)) {
                        showErrorAlert(jObject.getString("message"));

                        session.hapusAbsen();
                        bPulang.setVisibility(View.GONE);
                        babsen.setVisibility(View.VISIBLE);
                        bdetail.setVisibility(View.VISIBLE);
                    } else {
                        showErrorAlert(jObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    serverError("Gagal terhubung ke server, silahkan coba beberapa saat lagi");
                } else {
                    serverError("Wifi belum aktif!, Pastikan koneksi wifi aktif dan terhubung ke salah satu wifi UMY");
                }
            }
        } else if (reqCode == CODE_CHECKLAST) {
            if (jObject != null) {
                try {
                    JSONArray jsonArray = jObject.getJSONArray(AppKey.KEY_LAST_PRECENSE);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    //String jamDatang = jsonObject.getString("jam_datang");
                    //String ippublicHP = jObject.getString("ip");
                    String jamPulang = jsonObject.getString("jam_pulang");
                    String tanggalabsen = jsonObject.getString("tglakhirabsen");

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                    String datehp = df.format(c.getTime());

                    if (jamPulang != null && !jamPulang.isEmpty() && !jamPulang.equals("null")) {
                        //session.sudahAbsen();
                        babsen.setVisibility(View.VISIBLE);
                        bPulang.setVisibility(View.GONE);
                        //bdetailGone.setVisibility(View.GONE);
                        //bdetailGone.setVisibility(View.VISIBLE);
                    } else if (!tanggalabsen.equals(datehp)) {
                        //session.hapusAbsen();
                        babsen.setVisibility(View.VISIBLE);
                        bPulang.setVisibility(View.GONE);
                        //bdetailGone.setVisibility(View.GONE);
                        //bdetailGone.setVisibility(View.VISIBLE);
                    } else {
                        //session.hapusAbsen();
                        babsen.setVisibility(View.GONE);
                        bPulang.setVisibility(View.VISIBLE);
                        bdetailGone.setVisibility(View.GONE);
                        //bdetailGone.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //bdetail.setText("RIWAYAT PRESENSI");
                //bdetail.setVisibility(View.GONE);
                bdetail.setVisibility(View.VISIBLE);
                //bdetailGone.setVisibility(View.VISIBLE);
                babsen.setVisibility(View.GONE);
                bPulang.setVisibility(View.GONE);
                //ipp.setText("Tidak ada koneksi internet");
            }
        } else if (reqCode == CODE_IP) {
            if (jObject != null) {
                try {
                    String ippublicHP = jObject.getString("ip");
                    //ipp.setText("Anda terhubung di luar jaringan UMY");

                    /*if (ippublicUMY.equals(ippublicHP)) {
                        ipp.setText("Anda terhubung di jaringan UMY");
                        bdetail.setVisibility(View.VISIBLE);
                        bdetailGone.setVisibility(View.GONE);

                    } else {
                        //session.hapusAbsen();
                        babsen.setVisibility(View.GONE);
                        bPulang.setVisibility(View.GONE);
                        bdetail.setText("RIWAYAT PRESENSI");
                        bdetailGone.setVisibility(View.GONE);
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                bdetail.setText("DETAILS");
                //bdetail.setVisibility(View.GONE);
                bdetail.setVisibility(View.VISIBLE);
                //babsen.setVisibility(View.GONE);
                //bPulang.setVisibility(View.GONE);
                //bdetailGone.setVisibility(View.GONE);
               // ipp.setText("Tidak ada koneksi internet");
            }
        } else if (reqCode == CODE_JAM) {
            if (jObject != null) {
                try {
                    JSONArray jsonArray = jObject.getJSONArray(AppKey.KEY_JAMSERVER);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String jamku = jsonObject.getString("jam");
                    String hari = jsonObject.getString("hari");
                    String tanggalse = jsonObject.getString("tanggal");

                    jam.setVisibility(View.GONE);
                    tanggal.setVisibility(View.GONE);

                    tjamserver.setText(jamku);
                    ttanggalserver.setText(hari + " " + tanggalse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                jam.setVisibility(View.VISIBLE);
                tanggal.setVisibility(View.VISIBLE);
                tjamserver.setVisibility(View.VISIBLE);
                ttanggalserver.setVisibility(View.VISIBLE);
            }
        }
    }


    private void showErrorAlert(String message) {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
        //alert.setIcon(android.R.drawable.ic_dialog_info);
        alert.setTitle("Presensi Berhasil");
        alert.setCancelable(false);
        alert.setMessage(message);
        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
                startActivity(getIntent());
            }
        });
        alert.show();
    }

    private void serverError(String serverError) {
        android.app.AlertDialog.Builder pDialog = new android.app.AlertDialog.Builder(this);
        pDialog.setIcon(android.R.drawable.ic_dialog_info);
        pDialog.setTitle("Gagal terhubung ke Server!");
        pDialog.setCancelable(false);
        pDialog.setMessage("Gagal Koneksi ke server, aktifkan wifi dan cek kembali koneksi internet Anda");

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            pDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

        } else {
            toLoginActivity();
            pDialog.setPositiveButton("AKTIFKAN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                    //dialog.cancel();
                }
            });
            pDialog.setNegativeButton(R.string.cancel, null);
        }
        pDialog.show();
    }

    public void onError(String error, int reqCode) {
        //pDialog.dismiss();
        //Log.e("Error", error);
        //serverError(serverError);
    }

    public String getIPAddress() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ipAddress = inetAddress.getHostAddress();
                        d("IP address", "" + ipAddress);
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            //d("MainPrecense", ex.toString());
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
            SharedPreferences prefs = getSharedPreferences("PresensiDosen", Context.MODE_PRIVATE);
            final String absen = prefs.getString("ABSEN", null);

            Intent main = new Intent(Intent.ACTION_MAIN);
            main.addCategory(Intent.CATEGORY_HOME);
            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);

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
            case R.id.tutorial: {
                Intent r = new Intent(MainPrecense.this, WebViewPanduan.class);
                startActivity(r);
            }
            return true;
            case R.id.detailPresen: {
                Intent r = new Intent(MainPrecense.this, DetailPresensi.class);
                startActivity(r);
            }
            return true;
            case R.id.about: {
                Intent r = new Intent(MainPrecense.this, About.class);
                startActivity(r);
            }
            return true;
            case R.id.refesh: {
                finish();
                startActivity(getIntent());
            }
            return true;
            case R.id.sembunyikan: {
                Intent main = new Intent(Intent.ACTION_MAIN);
                main.addCategory(Intent.CATEGORY_HOME);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(main);
            }
            return true;
            case R.id.action_exit: {
                AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);

                alertDialogs.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialogs.setTitle(R.string.quit);
                alertDialogs.setMessage(R.string.really_quit);
                alertDialogs.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainPrecense.this.finish();
                        session.logoutUser();
                        System.exit(0);
                    }

                });
                alertDialogs.setNegativeButton(R.string.cancel, null);
                alertDialogs.show();

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void updateJamServer() {
        //Get JamServer
        handler.set(new ResponseHandler(AppKey.getTimeServer(), CODE_JAM, MainPrecense.this));
        handler.get().execute();
    }


    @Override
    public void onStart() {
        super.onStart();

        babsen.setVisibility(View.GONE);
        bPulang.setVisibility(View.GONE);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MainPrecense Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.bsi.presensidosen/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);

        Log.d(LOG, "onStart");


    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MainPrecense Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.bsi.presensidosen/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();

        Log.d(LOG, "onStart");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(LOG, "onRestart");
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //System.gc();
        tj.interrupt();
        //io.interrupt();



        Log.d(LOG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG, "onDestroy");

    }


}


