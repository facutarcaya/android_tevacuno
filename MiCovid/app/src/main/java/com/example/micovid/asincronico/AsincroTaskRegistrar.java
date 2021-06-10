package com.example.micovid.asincronico;

import android.os.AsyncTask;

import com.example.micovid.actividadprincipal.MainActivity;
import com.example.micovid.pantallaprincipal.PantallaInicioActivity;
import com.example.micovid.registrar.RegistrarActivity;

public class AsincroTaskRegistrar extends AsyncTask<Object, Void, Boolean> {
    private RegistrarActivity registrarActivity;


    public AsincroTaskRegistrar(RegistrarActivity registrarActivity) {
        this.registrarActivity = registrarActivity;
    }

    @Override
    protected void onPreExecute() {
        this.registrarActivity.toggleProgressBar(true);
        this.registrarActivity.habilitarBotones(false);
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        try {
            Thread.sleep(2000);
            String nombre = (String) objects[0];
            String apellido = (String) objects[1];
            String dni = (String) objects[2];
            String email = (String) objects[3];
            String password = (String) objects[4];
            return true;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        this.registrarActivity.toggleProgressBar(false);
        if(aBoolean) {
            this.registrarActivity.lanzarActivity(PantallaInicioActivity.class);
            this.registrarActivity.showMessage("Datos correctos");
        } else {
            this.registrarActivity.showMessage("Datos incorrectos");
            this.registrarActivity.habilitarBotones(true);
        }
        super.onPostExecute(aBoolean);
    }
}
