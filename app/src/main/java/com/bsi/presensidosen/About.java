package com.bsi.presensidosen;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class About extends AppCompatActivity {

    //private String TextBantu;
    TextView Bantuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle("Tentang Aplikasi");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bantuan = (TextView) findViewById(R.id.bantuanA);

        String html = "&copy; 2016 - 2017, Version 2.0.1<br>" +
                "Dikembangkan oleh Biro Sistem Informasi<br>" +
                "<a href='lauch.TermOfService://Kode?param1=isi-param'>Ketentuan Layanan</a>" +
                " | " +
                "<a href='lauch.PrivacyPolicy://Kode?param2=isi-param'>Kebijakan Privasi</a>" + " | " + "<a href='lauch.HelpActivity://Kode?param4=isi-param'>FAQ</a>" +
                "<br><br>All Rights reserved" +
                "<br><br><br><a href='launch://http://www.umy.ac.id'>www.umy.ac.id</a>" + " | " +
                "<a href='lauch.WebViewActivity://Kode?param4=isi-param'>www.bsi.umy.ac.id</a>";


        /*StringBuilder html = new StringBuilder();
        html.append("LOGIN berarti Anda Setuju dengan <br><a href='lauch.TermOfService://Kode?param1=isi-param'>Persaratan Layanan</a>");
        html.append(" dan ");
        html.append("<a href='lauch.PrivacyPolicy://Kode?param2=isi-param'>Kebijakan Privacy</a>");
        html.append("<br><br><br> <a href='http://bsi.umy.ac.id'>By BSI UMY,</a> Presensi Dosen v.2.0 &copy; 2016");*/

        Bantuan.setText(Html.fromHtml(html));
        Bantuan.setClickable(true);
        Bantuan.setMovementMethod(LinkMovementMethod.getInstance());
        stripUnderlines(Bantuan);
        Bantuan.setLinkTextColor(Color.argb(200, 200, 200, 200));
    }

    private void stripUnderlines(TextView bantuan) {
        Spannable s = new SpannableString(Bantuan.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        Bantuan.setText(s);
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
}
