package com.bsi.presensidosen;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bsi.presensidosen.helper.AppKey;
import com.bsi.presensidosen.helper.ResponseHandler;
import com.bsi.presensidosen.helper.SessionManagement;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Mukhlasin on 8/28/2016.
 */
public class DetailPresensi extends AppCompatActivity implements ResponseHandler.ResponseListener {

    //UI References
    private ResponseHandler handler;
    private final int REQ_DETAIL = 83;
    private ProgressDialog pdDialog, ptDialog, pbDialog;
    AlertDialog alertDialog;
    private final String TAG = DetailPresensi.class.getSimpleName();
    private TableLayout tabelData;
    private TableRow tableBaris;
    //int state = 0;

    private final int REQ_VALIDASI = 89;
    private final int REQ_TAHUN = 1;
    private final int REQ_BULAN = 2;

    private final String TAG_BULAN = "id_bulan";
    private final String TAG_NBULAN = "nama_bulan";
    private final String TAG_TAHUN = "id_tahun";
    private final String TAG_NTAHUN = "nama_tahun";

    private final String TAG_TAHUN_SKR = "tahun";
    private final String TAG_NBULAN_SKR = "bulannomor";

    ArrayList<String> id_bulan_list = new ArrayList<String>();
    ArrayList<String> nama_bulan_list = new ArrayList<String>();
    ArrayList<String> id_tahun_list = new ArrayList<String>();
    ArrayList<String> nama_tahun_list = new ArrayList<String>();
    ArrayList<String> nomor_bulan_list = new ArrayList<String>();

    //private static final String TAG_REFF = "reff";
    SessionManagement session;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.ma
     */

    private GoogleApiClient client;
    Spinner SpinnerBulan;
    Spinner SpinnerTahun;
    int Hold;
    private String tahun, bulan;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        session = new SessionManagement(getApplicationContext());

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_detail_presensi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle("Detail Presensi Bulanan");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session.checkLogin();

        pdDialog = new ProgressDialog(DetailPresensi.this);
        pdDialog.setMessage("Loading...");
        pdDialog.setCanceledOnTouchOutside(false);
        pdDialog.show();


        handler = new ResponseHandler(AppKey.getBulanSpinner(), REQ_BULAN, DetailPresensi.this);
        handler.execute();

        handler = new ResponseHandler(AppKey.getTahunSpinner(), REQ_TAHUN, DetailPresensi.this);
        handler.execute();



        /*Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH) + 1;

        HashMap<String, String> user = session.getUserDetails();
        int idpegawai = Integer.parseInt(user.get(SessionManagement.KEY_ID));

        handler = new ResponseHandler(AppKey.getDetailPrecense(idpegawai, String.valueOf(month), String.valueOf(year)), REQ_DETAIL, this);
        handler.execute();*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onSuccess(JSONObject jObject, int reqCode) {
        pdDialog.dismiss();
        if (reqCode == REQ_BULAN) {
            try {
                //Log.d(TAG, "DATATAHUN = " + jObject.toString());
                if (jObject.getBoolean(AppKey.KEY_STATUS)) {
                    JSONArray jsonarray = jObject.getJSONArray(AppKey.KEY_SPINNER_BULAN);

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        String id_bulan = jsonObject.getString(TAG_BULAN);
                        String nama_bulan = jsonObject.getString(TAG_NBULAN);
                        String nomor_bulan = jsonObject.getString("bulannomor");

                        id_bulan_list.add(id_bulan);
                        nama_bulan_list.add(nama_bulan);
                        nomor_bulan_list.add(nomor_bulan);
                    }

                    Calendar c = Calendar.getInstance();
                    final int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int date = c.get(Calendar.DATE);


                    SpinnerBulan = (Spinner) findViewById(R.id.sBulan);
                    SpinnerBulan.setAdapter(new ArrayAdapter<String>(this,
                            R.layout.spinner_item_position,
                            nama_bulan_list));

                    //Spinner spinner = (Spinner) findViewById(R.id.spinner1);
                    //String[] years = {"1996","1997","1998","1998"};
                    ArrayAdapter<String> langAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_position, nama_bulan_list);
                    langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                    SpinnerBulan.setAdapter(langAdapter);

                    SpinnerBulan.setSelection(Integer.parseInt(String.valueOf(month)));

                    SpinnerBulan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                               @Override
                                                               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                   HashMap<String, String> user = session.getUserDetails();
                                                                   int idpegawai = Integer.parseInt(user.get(SessionManagement.KEY_ID));
                                                                   String nik = user.get(SessionManagement.KEY_NIK);

                                                                   bulan = id_bulan_list.get(position);
                                                                   //Log.d(TAG, "TAHUNNNN = " + bulan);

                                                                   String stahun = "2016";
                                                                   //String sbulan = "10";

                                                                   handler = new ResponseHandler(AppKey.getDetailPrecense(idpegawai, bulan, String.valueOf(year)), REQ_DETAIL, DetailPresensi.this);
                                                                   handler.execute();
                                                               }

                                                               @Override
                                                               public void onNothingSelected(AdapterView<?> parent) {
                                                               }
                                                           }
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (reqCode == REQ_TAHUN) {
            pdDialog.dismiss();
            try {
                //Log.d(TAG, "DATATAHUN = " + jObject.toString());
                if (jObject.getBoolean(AppKey.KEY_STATUS)) {
                    JSONArray jsonarray = jObject.getJSONArray(AppKey.KEY_SPINNER_TAHUN);

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        String id_tahun = jsonObject.getString(TAG_TAHUN);
                        String nama_tahun = jsonObject.getString(TAG_NTAHUN);
                        id_tahun_list.add(id_tahun);
                        nama_tahun_list.add(nama_tahun);
                    }

                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    final int month = c.get(Calendar.MONTH);


                    /*SpinnerTahun = (Spinner) findViewById(R.id.sTahun);
                    SpinnerTahun.setAdapter(new ArrayAdapter<String>(this,
                            R.layout.spinner_item_position,
                            nama_tahun_list));

                    ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_position, nama_tahun_list);
                    langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                    SpinnerTahun.setAdapter(langAdapter);*/

                    ArrayList<String> years = new ArrayList<String>();
                    int thisYear = Calendar.getInstance().get(Calendar.YEAR);
                    for (int i = 2013; i <= thisYear; i++) {
                        years.add(Integer.toString(i));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_position, years);
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                    Spinner SpinnerTahun = (Spinner) findViewById(R.id.sTahun);
                    SpinnerTahun.setAdapter(adapter);

                    //String test = SpinnerTahun.setSelection(Integer.parseInt(String.valueOf(year)));

                    String thisYearp = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    switch (thisYearp) {
                        case "2017":
                            SpinnerTahun.setSelection(4);
                            //return true;
                            break;
                        case "2018":
                            SpinnerTahun.setSelection(5);
                            //return true;
                            break;
                        case "2019":
                            SpinnerTahun.setSelection(6);
                            //return true;
                            break;
                        case "2020":
                            SpinnerTahun.setSelection(7);
                            //return true;
                            break;
                        case "2021":
                            SpinnerTahun.setSelection(8);
                            //return true;
                            break;
                        default:
                            SpinnerTahun.setSelection(0);
                            break;
                    }

                    SpinnerTahun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                               @Override
                                                               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                   HashMap<String, String> user = session.getUserDetails();
                                                                   int idpegawai = Integer.parseInt(user.get(SessionManagement.KEY_ID));
                                                                   String nik = user.get(SessionManagement.KEY_NIK);

                                                                   tahun = nama_tahun_list.get(position);
                                                                   //Log.d(TAG, "TAHUNNNN = " + bulan);


                                                                   handler = new ResponseHandler(AppKey.getDetailPrecense(idpegawai, bulan, tahun), REQ_DETAIL, DetailPresensi.this);
                                                                   handler.execute();
                                                               }

                                                               @Override
                                                               public void onNothingSelected(AdapterView<?> parent) {
                                                               }
                                                           }
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        } else if (reqCode == REQ_DETAIL) {
            pdDialog.dismiss();
            try {
                Log.d(TAG, "DETAIL = " + jObject.toString());
                if (jObject.getBoolean(AppKey.KEY_STATUS)) {
                    tabelData = (TableLayout) findViewById(R.id.tblDetail);

                    while (tabelData.getChildCount() > 0) {
                        tabelData.removeViewAt(0);
                    }

                    JSONArray jsonarraydetail = jObject.getJSONArray(AppKey.KEY_DATADETAIL);

                    for (int i = 0; i < jsonarraydetail.length(); i++) {
                        JSONObject jsonObjectDetail = jsonarraydetail.getJSONObject(i);
                        TableRow tableBaris = new TableRow(this);

                        TextView tanggal = new TextView(this);
                        tanggal.setText((jsonObjectDetail.getString("tglabsen")));
                        tanggal.setGravity(Gravity.CENTER);
                        tanggal.setBackgroundColor(Color.rgb(220, 220, 220));
                        tanggal.setTextColor(Color.rgb(20, 20, 20));
                        tanggal.setPadding(0, 20, 0, 20);
                        tableBaris.addView(tanggal);

                        TextView jamdatang = new TextView(this);
                        jamdatang.setText(jsonObjectDetail.getString("jamdatang"));
                        jamdatang.setBackgroundColor(Color.rgb(211, 211, 211));
                        jamdatang.setTextColor(Color.rgb(20, 20, 20));
                        jamdatang.setPadding(0, 20, 0, 20);
                        jamdatang.setGravity(Gravity.CENTER);
                        tableBaris.addView(jamdatang);

                        TextView jampulang = new TextView(this);
                        jampulang.setText(jsonObjectDetail.getString("jampulang"));
                        jampulang.setBackgroundColor(Color.rgb(202, 201, 201));
                        jampulang.setTextColor(Color.rgb(20, 20, 20));
                        jampulang.setPadding(0, 20, 0, 20);
                        jampulang.setGravity(Gravity.CENTER);
                        tableBaris.addView(jampulang);

                        tabelData.addView(tableBaris);
                        tabelData.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(this, "BELUM ADA DATA PRESENSI", Toast.LENGTH_SHORT).show();
                    tabelData.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //TODO: handle exception
                //tabelData.setVisibility(View.GONE);
                //Toast.makeText(this, "Gagal Koneksi Ke Server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toMainPrecense() {
        Intent i = new Intent(DetailPresensi.this, MainPrecense.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
    }

    @Override
    public void onError(String error, int reqCode) {
        pdDialog.dismiss();
        alertDialog.dismiss();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DetailPresensi Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
