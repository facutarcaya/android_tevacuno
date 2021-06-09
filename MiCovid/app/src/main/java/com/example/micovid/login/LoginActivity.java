package com.example.micovid.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.micovid.DisplayMessageActivity;
import com.example.micovid.R;
import com.example.micovid.actividadprincipal.MainActivity;
import com.example.micovid.registrar.RegistrarActivity;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "com.example.micovid.EMAIL_LOGIN";

    private static final int CREDENTIALS_RESULT = 4342;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    protected void loginCorrecto(String email) {
        Context context = getApplicationContext();
        CharSequence text = "Logueado correctamente";
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, text, duration).show();

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        String message = email;
        intent.putExtra(EXTRA_EMAIL, message);
        startActivity(intent);

    }

    public void inicioRegistrar(View view) {
        Intent intent = new Intent(this, RegistrarActivity.class);
        EditText email = (EditText) findViewById(R.id.editTextLoginEmail);
        intent.putExtra(EXTRA_EMAIL, email.getText().toString());
        startActivity(intent);
    }

    /*
    public void alertarRegistrar(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Registrarse");
        alertDialogBuilder.setMessage("¿Está seguro que desea registrarse?");
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
                checkCredentials();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void checkCredentials() {
        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        Intent credentialsIntent = keyguardManager.createConfirmDeviceCredentialIntent("Confirmá para continuar", "");
        if (credentialsIntent != null) {
            startActivityForResult(credentialsIntent, CREDENTIALS_RESULT);
        } else {
            //no password needed
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setTitle("No contra");
            alertDialogBuilder.setMessage("¿Está seguro que desea registrarse?");
            alertDialogBuilder.setCancelable(true);

            alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIALS_RESULT) {
            if(resultCode == RESULT_OK) {
                //hoorray!
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                alertDialogBuilder.setTitle("Contra bien");
                alertDialogBuilder.setMessage("¿Está seguro que desea registrarse?");
                alertDialogBuilder.setCancelable(true);

                alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                //uh-oh
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                alertDialogBuilder.setTitle("ERROR");
                alertDialogBuilder.setMessage("¿Está seguro que desea registrarse?");
                alertDialogBuilder.setCancelable(true);

                alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }*/
}