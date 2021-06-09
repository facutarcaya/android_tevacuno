package com.example.micovid.actividadprincipal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.micovid.DisplayMessageActivity;
import com.example.micovid.R;
import com.example.micovid.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.micovid.MESSAGE";

    public boolean logueado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!logueado) {
            abrirLogin();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!logueado) {
            abrirLogin();
        }
    }

    /** Se llama cuando se le da a Enviar */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    /** Se llama cuando se le da a Enviar */
    public void abrirLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}