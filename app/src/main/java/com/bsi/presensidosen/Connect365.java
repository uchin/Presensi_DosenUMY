package com.bsi.presensidosen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.bsi.presensidosen.helper.SessionManagement;

public class Connect365 extends AppCompatActivity {

    Button login_manual, login_365;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new SessionManagement(getApplicationContext());

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_connect365);



        login_manual = (Button) findViewById(R.id.login_manual);
        login_365 = (Button) findViewById(R.id.login_365);

        if (session.isLoggedIn()) {
            login_manual.setVisibility(View.GONE);
            login_365.setVisibility(View.GONE);
        }


        login_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Connect365.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }



}