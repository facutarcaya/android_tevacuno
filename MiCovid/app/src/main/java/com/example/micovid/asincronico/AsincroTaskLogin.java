package com.example.micovid.asincronico;

import android.os.AsyncTask;

import com.example.micovid.actividadprincipal.MainActivity;
import com.example.micovid.login.LoginActivity;
import com.example.micovid.pantallaprincipal.PantallaInicioActivity;
import com.example.micovid.registrar.RegistrarActivity;

public class AsincroTaskLogin extends AsyncTask<Object, Void, Boolean> {
    private LoginActivity loginActivity;


    public AsincroTaskLogin(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    @Override
    protected void onPreExecute() {
        this.loginActivity.toggleProgressBar(true);
        this.loginActivity.habilitarBotones(false);
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        try {
            Thread.sleep(2000);
            String email = (String) objects[0];
            String password = (String) objects[1];

            //LLAMAR API

            //SI ESTA BIEN DEVOLVER TRUE SINO FALSE

            return true;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        this.loginActivity.toggleProgressBar(false);
        if(aBoolean) {
            this.loginActivity.lanzarActivity(PantallaInicioActivity.class);
            this.loginActivity.showMessage("Datos correctos");
        } else {
            this.loginActivity.showMessage("Datos incorrectos");
            this.loginActivity.habilitarBotones(true);
        }
        super.onPostExecute(aBoolean);
    }
}
