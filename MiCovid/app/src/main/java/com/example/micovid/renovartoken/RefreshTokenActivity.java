package com.example.micovid.renovartoken;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.micovid.R;
import com.example.micovid.asincronico.AsincroTaskLogin;
import com.example.micovid.asincronico.AsincroTaskRefresh;
import com.example.micovid.login.LoginActivity;

public class RefreshTokenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_token);
    }

    private void RefrescarToken(View view) {
        new AsincroTaskRefresh(RefreshTokenActivity.this).execute();
    }

    public void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}