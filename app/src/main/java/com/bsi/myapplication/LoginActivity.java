package com.bsi.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Connection;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    final Context context = this;
    public TextView textView;

    Button login;
    TextView username, password, forgotPassword, privacy;
    ProgressBar progressBar;


    /*koneksi db*/
    Connection con;
    String un,pass,db,ip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login_button);
        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        privacy = (TextView) findViewById(R.id.privacy);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        /*deklarasi*/
        ip = "localhost/login.php";
        db = "payroll_web";
        un = "sa";
        pass= "83AG18SAMALILI";



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(LoginActivity.this, MainPrecense.class);
                startActivity(i);
                finish();
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

        privacy.setText( Html.fromHtml( html.toString() ) );
        privacy.setClickable(true);
        privacy.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
