package com.example.micovid.registrar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.micovid.R;
import com.example.micovid.login.LoginActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrarActivity extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "com.example.micovid.EMAIL_REGISTRAR";

    private ProgressBar progressBar;
    private EditText editTextNombre;
    private EditText editTextApellido;
    private EditText editTextDni;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //Inicio de variables
        progressBar = findViewById(R.id.progressBar);

        toggleProgressBar(false);

        editTextNombre = (EditText) findViewById(R.id.editTextNombre);
        editTextApellido = (EditText) findViewById(R.id.editTextApellido);
        editTextDni = (EditText) findViewById(R.id.editTextDni);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_EMAIL);

        // Capture the layout's TextView and set the string as its text
        editTextEmail.setText(message);
    }

    public void registrarUsuario(View view){

        boolean camposCorrectos;


        camposCorrectos = validarCamposRegistrar();

        if (camposCorrectos) {
            //Pedir patron
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


        String nombre = editTextNombre.getText().toString();

        if (nombre.trim().equals("")) {
            editTextNombre.setError( "El nombre es requerido" );
            camposValidos = false;
        }

        String apellido = editTextApellido.getText().toString();

        if (apellido.isEmpty()) {
            editTextApellido.setError( "El apellido es requerido" );
            camposValidos = false;
        }

        int dni = 0;

        if(editTextDni.getText().toString().isEmpty()) {
            editTextDni.setError( "El DNI es requerido" );
            camposValidos = false;
        } else {
            try {
                dni = Integer.parseInt(editTextDni.getText().toString());
                if (dni < 10000000 || dni > 99999999) {
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
            String pattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
            Pattern r = Pattern.compile(pattern);

            Matcher m = r.matcher(email);
            if (!m.find()) {
                editTextEmail.setError( "El email no es válido" );
                camposValidos = false;
            }
        }

        String password = editTextPassword.getText().toString();

        if (password.length() < 8) {
            editTextPassword.setError( "La contraseña debe ser de mínimo 8 caracteres" );
            camposValidos = false;
        }

        return camposValidos;
    }
}