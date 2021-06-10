package com.example.micovid.registrar;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.micovid.R;
import com.example.micovid.asincronico.AsincroTaskRegistrar;
import com.example.micovid.login.LoginActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrarActivity extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "com.example.micovid.EMAIL_REGISTRAR";
    private static final int CREDENTIALS_RESULT = 4342;

    private ProgressBar progressBar;
    private EditText editTextNombre;
    private EditText editTextApellido;
    private EditText editTextDni;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegistrarse;
    private Button buttonRegistrarCancelar;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        checkCredentials();

        //Inicio de variables
        progressBar = findViewById(R.id.progressBar);

        toggleProgressBar(false);

        editTextNombre = (EditText) findViewById(R.id.editTextNombre);
        editTextApellido = (EditText) findViewById(R.id.editTextApellido);
        editTextDni = (EditText) findViewById(R.id.editTextDni);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonRegistrarse = (Button) findViewById(R.id.buttonRegistrarse);
        buttonRegistrarCancelar = (Button) findViewById(R.id.buttonRegistrarCancelar);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_EMAIL);

        // Capture the layout's TextView and set the string as its text
        editTextEmail.setText(message);
    }

    public void habilitarBotones(boolean status) {
        this.buttonRegistrarCancelar.setEnabled(status);
        this.buttonRegistrarse.setEnabled(status);
    }

    public void cancelar(View view) {
        finish();
    }

    public void registrarUsuario(View view){

        boolean camposCorrectos;

        camposCorrectos = validarCamposRegistrar();

        if (camposCorrectos) {
            new AsincroTaskRegistrar(RegistrarActivity.this).execute(this.nombre,this.apellido,this.dni,this.email,this.password);
        }

    }

    public void toggleProgressBar(boolean status) {
        if (status) {
            this.progressBar.setVisibility(View.VISIBLE);
        } else {
            this.progressBar.setVisibility(View.GONE);
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

    private boolean validarCamposRegistrar() {

        boolean camposValidos = true;


        this.nombre = editTextNombre.getText().toString();

        if (this.nombre.trim().equals("")) {
            editTextNombre.setError( "El nombre es requerido" );
            camposValidos = false;
        }

        this.apellido = editTextApellido.getText().toString();

        if (this.apellido.isEmpty()) {
            editTextApellido.setError( "El apellido es requerido" );
            camposValidos = false;
        }

        int dniNum = 0;

        this.dni = editTextDni.getText().toString();

        if(this.dni.isEmpty()) {
            editTextDni.setError( "El DNI es requerido" );
            camposValidos = false;
        } else {
            try {
                dniNum = Integer.parseInt(editTextDni.getText().toString());
                if (dniNum < 10000000 || dniNum > 99999999) {
                    editTextDni.setError( "El DNI no es válido" );
                    camposValidos = false;
                }
            } catch (NumberFormatException numberFormatException) {
                editTextDni.setError( "El DNI no es válido" );
                camposValidos = false;
            }
        }

        this.email = editTextEmail.getText().toString();

        if (email.isEmpty()) {
            editTextEmail.setError( "El email es requerido" );
            camposValidos = false;
        } else {
            String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
            Pattern r = Pattern.compile(pattern);

            Matcher m = r.matcher(email);
            if (!m.find()) {
                editTextEmail.setError( "El email no es válido");
                camposValidos = false;
            }
        }

        this.password = editTextPassword.getText().toString();

        if (this.password.length() < 8) {
            editTextPassword.setError( "La contraseña debe ser de mínimo 8 caracteres" );
            camposValidos = false;
        }

        return camposValidos;
    }

    public void checkCredentials() {
        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        Intent credentialsIntent = keyguardManager.createConfirmDeviceCredentialIntent("Confirmá para continuar", "");
        if (credentialsIntent != null) {
            startActivityForResult(credentialsIntent, CREDENTIALS_RESULT);
        } else {
            //no password needed
            Toast.makeText(this,"Credenciales correctas",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIALS_RESULT) {
            if(resultCode == RESULT_OK) {
                //hoorray!
                Toast.makeText(this,"Credenciales correctas",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,"Credenciales incorrectas",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}