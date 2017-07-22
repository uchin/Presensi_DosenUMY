package com.bsi.presensidosen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bsi.presensidosen.helper.AppKey;
import com.bsi.presensidosen.helper.ResponseHandler;
import com.bsi.presensidosen.helper.SessionManagement;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements ResponseHandler.ResponseListener {
    //ConnectionClass connectionClass;
    final Context context = this;
    private ResponseHandler handler;
    ProgressDialog prosesLogin, prosesVersi, prosesKoneksi;
    AlertDialog alert, alertDialog;
    private final String TAG = LoginActivity.class.getSimpleName();
    int CODE_LOGIN = 76;
    int CEK_VERSI = 78;
    Button login;
    TextView email, password, forgotPassword, privacy;
    String msgValidation;
    public String msgKoneksiServer;
    SessionManagement session;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        session = new SessionManagement(getApplication());

        email = (TextView) findViewById(R.id.email);
        password = (TextView) findViewById(R.id.password);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        privacy = (TextView) findViewById(R.id.privacy);

        session.checkLogin();
        cekKoneksi();

        login = (Button) findViewById(R.id.login_button);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //login.setBackgroundColor(ContextCompat.getColor(context, R.color.merah));
                doLogin();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Lupa Password ?");
                alertDialogBuilder
                        .setMessage("Jika Anda lupa username atau password, Silahkan hubungi Admin BSI UMY ext 164 untuk mendapatkan password yang baru")
                        .setCancelable(false)
                        .setNegativeButton("FAQ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(LoginActivity.this, HelpActivity.class);
                                startActivity(i);
                            }
                        })
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("PANDUAN", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(LoginActivity.this, WebViewPanduan.class);
                                startActivity(i);
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


        String app_version = "2.0.1";
        String html = "LOGIN berarti Anda Setuju dengan <br><a href='lauch.TermOfService://Kode?param1=isi-param'>Ketentuan Layanan</a>" +
                " dan " +
                "<a href='lauch.PrivacyPolicy://Kode?param2=isi-param'>Kebijakan Privasi</a>" +
                "<br><br><br> &copy; 2016,<a href='lauch.WebViewActivity://Kode?param3=isi-param'> BSI UMY,</a> Presensi Dosen, Versi " + app_version;

        privacy.setText(Html.fromHtml(html));
        privacy.setClickable(true);
        privacy.setMovementMethod(LinkMovementMethod.getInstance());
        stripUnderlines(privacy);
        privacy.setLinkTextColor(Color.argb(200, 200, 200, 200));


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void doLogin() {
        if (validateLogin(email, password)) {
            String usernamee = email.getText().toString();
            String passworde = password.getText().toString();
            //Log.d(TAG, "email= " + usernamee + ", password= " + passworde);

            prosesLogin = new ProgressDialog(LoginActivity.this);
            prosesLogin.setTitle("Proses Login.");
            prosesLogin.setMessage("Mohon Tunggu...");
            prosesLogin.setCancelable(false);
            prosesLogin.setCanceledOnTouchOutside(false);
            prosesLogin.show();

            handler = new ResponseHandler(AppKey.getUserLogin(usernamee, passworde), CODE_LOGIN, LoginActivity.this);
            handler.execute();

        } else {
            showErrorAlert(msgValidation);
        }
    }

    private boolean validateLogin(TextView email, TextView password) {
        String usrnm = email.getText().toString();
        String passwd = password.getText().toString();
        if (usrnm.equalsIgnoreCase("") && passwd.equalsIgnoreCase("")) {
            msgValidation = (getString(R.string.pwd_harus_isi));
            return false;
        } else if (usrnm.equalsIgnoreCase("")) {
            msgValidation = (getString(R.string.pwd_harus_isi));
            return false;
        } else if (passwd.equalsIgnoreCase("")) {
            msgValidation = (getString(R.string.pwd_harus_isi));
            return false;
        } else {
            //cekKoneksi();
            return true;
        }
    }

    public void cekKoneksi() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                prosesVersi = new ProgressDialog(LoginActivity.this);
                prosesVersi.setTitle("Cek server.");
                prosesVersi.setMessage("Mohon Tunggu...");
                prosesVersi.setCancelable(false);
                prosesVersi.setCanceledOnTouchOutside(false);
                prosesVersi.show();

                String app_version = "2.0.1";
                handler = new ResponseHandler(AppKey.getAppVersion(app_version), CEK_VERSI, LoginActivity.this);
                handler.execute();

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                serverError(msgKoneksiServer = "Anda belum terhubung ke wifi UMY");
            }
        } else {
            serverError(msgKoneksiServer = "Tidak Ada Koneksi, Nyalakan wifi dan sambungkan ke wifi UMY");
        }
    }


    private void serverError(String msgKoneksiServer) {

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(LoginActivity.this);
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Info!");
        alertDialog.setMessage(msgKoneksiServer);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        login.setBackgroundColor(ContextCompat.getColor(context, R.color.biru_tua));
                    }
                });

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                alertDialog.setPositiveButton("SAMBUNGKAN WIFI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));

                    }
                });
                alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                });
            }
        } else {
            alertDialog.setPositiveButton("SAMBUNGKAN WIFI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                    dialog.cancel();
                }
            });
            alertDialog.setNegativeButton(R.string.cancel, null);

        }

        if (!((Activity) context).isFinishing()) {
            alertDialog.show();
        }
    }


    private void showErrorAlert(String message) {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(LoginActivity.this);
        alert.setIcon(android.R.drawable.ic_dialog_info);
        alert.setTitle(getString(R.string.title_login_gagal));
        alert.setMessage(message);
        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                login.setBackgroundColor(ContextCompat.getColor(context, R.color.biru_tua));
            }
        });
        alert.show();

    }

    public void onSuccess(JSONObject jObject, int reqCode) {
        if (reqCode == CODE_LOGIN) {
            prosesLogin.dismiss();
            if (jObject != null) {
                try {
                    if (jObject.getBoolean(AppKey.KEY_STATUS)) {
                        JSONArray jsonArray = jObject.getJSONArray(AppKey.KEY_DATA);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String Id_Pegawai = jsonObject.getString("id_pegawai");
                        String Email = jsonObject.getString("email");
                        String Nik = jsonObject.getString("nik");
                        String Nama = jsonObject.getString("nama");
                        String Nama_Prodi = jsonObject.getString("nama_unitkerja");
                        String Photos = jsonObject.getString("photos");

                        session.createLoginSession(Id_Pegawai, Email, Nik, Nama, Nama_Prodi, Photos);
                        toMainPrecense();
                    } else {
                        showErrorAlert(jObject.getString(AppKey.KEY_MESSAGE));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showErrorAlert("Anda belum terhubung ke wifi UMY ok");
            }
        } else if (reqCode == CEK_VERSI) {
            prosesVersi.dismiss();
            if (jObject != null) {
                try {
                    if (jObject.getBoolean(AppKey.KEY_STATUS)) {
                        JSONArray jsonArray = jObject.getJSONArray(AppKey.KEY_DATA_VERSI);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String appVersi = jsonObject.getString("app_version");
                        String vVersi = jsonObject.getString("validasi_versi");
                        String mUpdate = jsonObject.getString("Is_MustUpdates");

                        if (vVersi.equals("1")) {
                            session = new SessionManagement(LoginActivity.this);
                            if (session.isLoggedIn()) {
                                toMainPrecense();
                            }

                        } else if (vVersi.equals("0") && mUpdate.equals("TRUE")) {
                            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(LoginActivity.this);
                            alert.setIcon(android.R.drawable.ic_dialog_info);
                            alert.setTitle("Info Update!");
                            alert.setCancelable(false);
                            alert.setMessage("Mohon Ma'af, versi terbaru telah tersedia. untuk bisa terus menggunakan aplikasi ini, Anda perlu download versi terbaru.");
                            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                            alert.setPositiveButton("DOWNLOAD", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kepegawaian.umy.ac.id/Download"));
                                    startActivity(browserIntent);
                                    finish();
                                }
                            });
                            alert.show();
                        } else if (vVersi.equals("0") && mUpdate.equals("FALSE")) {
                            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(LoginActivity.this);
                            alert.setIcon(android.R.drawable.ic_dialog_info);
                            alert.setTitle("Info Update!");
                            alert.setCancelable(false);
                            alert.setMessage("Versi terbaru telah tersedia. Pilih update untuk download versi terbaru.");
                            alert.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kepegawaian.umy.ac.id/Download"));
                                    startActivity(browserIntent);
                                    finish();
                                }
                            });
                            alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    session = new SessionManagement(LoginActivity.this);
                                    if (session.isLoggedIn()) {
                                        toMainPrecense();
                                    }
                                }
                            });


                            alert.show();


                        }

                    } else {
                        showErrorAlert(jObject.getString(AppKey.KEY_MESSAGE));
                        //serverError = ("Anda Belum terhubung ke wifi UMY");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                serverError(msgKoneksiServer = "Gagal terhubung ke Server, coba buka kembali aplikasi beberapa saat lagi.");
                //serverError = ("Anda Belum terhubung ke wifi UMY");
                /*android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(LoginActivity.this);
                alert.setIcon(android.R.drawable.ic_dialog_info);
                alert.setTitle("Info");
                alert.setCancelable(false);
                alert.setMessage("Anda Belum terhubung ke wifi UMY");
                alert.setPositiveButton("SAMBUNGKAN WIFI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kepegawaian.umy.ac.id/Download"));
                        startActivity(browserIntent);
                        finish();
                    }
                });
                alert.setNegativeButton(R.string.cancel, null);
                alert.show();*/
            }
        }
    }

    private void toMainPrecense() {
        Intent i = new Intent(LoginActivity.this, MainPrecense.class);
        startActivity(i);
        finish();
    }

    /*remove underline text link*/
    private void stripUnderlines(TextView privacy) {
        Spannable s = new SpannableString(privacy.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        privacy.setText(s);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
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

    @SuppressLint("ParcelCreator")
    private class URLSpanNoUnderline extends URLSpan {
        URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);

        }
    }

    public void onError(String error, int reqCode) {
        //pDialogl.dismiss();
        //Log.e("Error", error);
        serverError(msgKoneksiServer);
    }


}
