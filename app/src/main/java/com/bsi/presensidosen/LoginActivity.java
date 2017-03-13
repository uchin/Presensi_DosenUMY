package com.bsi.presensidosen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    ConnectionClass connectionClass;

    final Context context = this;

    Button login;
    TextView username, password, forgotPassword, privacy;

    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        connectionClass = new ConnectionClass();

        setContentView(R.layout.activity_login);

        session = new SessionManagement(getApplication());


        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        privacy = (TextView) findViewById(R.id.privacy);


        session.checkLogin();



        if (session.isLoggedIn()){
            Intent i = new Intent(LoginActivity.this, MainPrecense.class);
            startActivity(i);
            finish();
        }

        login = (Button) findViewById(R.id.login_button);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                /*DoLogin doLogin = new DoLogin();
                doLogin.execute("");*/


                // TODO Auto-generated method stub
                String usernam = username.getText().toString();
                String passwordd = password.getText().toString();

                if(usernam.trim().length() > 0 && passwordd.trim().length() > 0){

                    if (usernam.equals("test") && passwordd.equals("test")){
                        session.createLoginSession("test","test");

                        /*ProgressDialog pd = new ProgressDialog(context);
                        pd.setTitle("Loading...");
                        pd.setMessage("Please wait.");
                        pd.setCancelable(false);

                        pd.show();*/

                        Intent i = new Intent(LoginActivity.this, MainPrecense.class);
                        startActivity(i);
                        finish();

                    }else{
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                        alertDialogBuilder.setTitle(getString(R.string.title_login_gagal));
                        alertDialogBuilder
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setMessage(getString(R.string.pwd_belum_benar))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.ok),new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int id) {
                                        dialogInterface.cancel();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }
                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle(getString(R.string.title_login_gagal));
                    alertDialogBuilder
                            .setMessage(getString(R.string.pwd_harus_isi))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.ok),new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    dialogInterface.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
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
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


        StringBuilder html = new StringBuilder();
        html.append("LOGIN berarti Anda Setuju dengan <br><a href='lauch.TermOfService://Kode?param1=isi-param'>Persaratan Layanan</a>");
        html.append(" dan ");
        html.append("<a href='lauch.PrivacyPolicy://Kode?param2=isi-param'>Kebijakan Privacy</a>");
        html.append("<br><br>&copy; 2016, <a href='http://bsi.umy.ac.id'>Biro Sistem Iinformasi UMY</a>");

        privacy.setText( Html.fromHtml( html.toString() ));
        privacy.setClickable(true);
        privacy.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
