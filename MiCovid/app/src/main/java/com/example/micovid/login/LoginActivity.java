package com.example.micovid.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.micovid.DisplayMessageActivity;
import com.example.micovid.R;
import com.example.micovid.actividadprincipal.MainActivity;
import com.example.micovid.asincronico.AsincroTaskLogin;
import com.example.micovid.asincronico.AsincroTaskRegistrar;
import com.example.micovid.registrar.RegistrarActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "com.example.micovid.EMAIL_LOGIN";
    private static final int CREDENTIALS_RESULT = 4342;

    private ProgressBar progressBarLogin;
    private EditText editTextLoginEmail;
    private EditText editTextLoginPassword;
    private Button buttonLoginCancelar;
    private Button buttonLoginLogin;
    private Button buttonRegistrarInicio;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicio de variables
        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);
        editTextLoginEmail = (EditText) findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = (EditText) findViewById(R.id.editTextLoginPassword);
        buttonLoginCancelar = (Button) findViewById(R.id.buttonLoginCancelar);
        buttonLoginLogin = (Button) findViewById(R.id.buttonLoginLogin);
        buttonRegistrarInicio = (Button) findViewById(R.id.buttonRegistrarInicio);

        toggleProgressBar(false);
    }

    public void habilitarBotones(boolean status) {
        this.buttonLoginCancelar.setEnabled(status);
        this.buttonLoginLogin.setEnabled(status);
        this.buttonRegistrarInicio.setEnabled(status);
    }

    public void cancelar(View view) {
        finish();
    }

    public void  loginUsuario(View view) {
        boolean camposCorrectos;


        camposCorrectos = validarCamposRegistrar();

        if (camposCorrectos) {
            new AsincroTaskLogin(LoginActivity.this).execute();
        }
    }

    public void toggleProgressBar(boolean status) {
        if (status) {
            this.progressBarLogin.setVisibility(View.VISIBLE);
        } else {
            this.progressBarLogin.setVisibility(View.GONE);
        }
    }

    public void lanzarActivity(Class<?> tipoActividad) {
        Intent intent = new Intent(this,tipoActividad);
        intent.putExtra(EXTRA_EMAIL, this.email);
        startActivity(intent);
    }

    public void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }


    public void inicioRegistrar(View view) {
        Intent intent = new Intent(this, RegistrarActivity.class);
        EditText email = (EditText) findViewById(R.id.editTextLoginEmail);
        intent.putExtra(EXTRA_EMAIL, email.getText().toString());
        startActivity(intent);
    }

    private boolean validarCamposRegistrar() {

        boolean camposValidos = true;

        this.email = editTextLoginEmail.getText().toString();

        if (email.isEmpty()) {
            editTextLoginEmail.setError( "El email es requerido" );
            camposValidos = false;
        } else {
            String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
            Pattern r = Pattern.compile(pattern);

            Matcher m = r.matcher(email);
            if (!m.find()) {
                editTextLoginEmail.setError( "El email no es válido");
                camposValidos = false;
            }
        }

        this.password = editTextLoginPassword.getText().toString();

        if (password.length() < 8) {
            editTextLoginPassword.setError( "La contraseña debe ser de mínimo 8 caracteres" );
            camposValidos = false;
        }

        return camposValidos;
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