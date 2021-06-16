package com.example.micovid.asincronico;

import android.os.AsyncTask;

import com.example.micovid.juego.GameActivity;
import com.example.micovid.pantallaprincipal.PantallaInicioActivity;

public class AsincroTaskVerificarTimeout extends AsyncTask<Object, Void, Boolean> {
    private PantallaInicioActivity pantallaInicioActivity;
    public static final long tiempoMax = 1000 /* 1 Seg*/ * 60 /* 1 Min*/ * 5/*Cant minutos*/ ;

    public AsincroTaskVerificarTimeout(PantallaInicioActivity pantallaInicioActivity) {
        this.pantallaInicioActivity = pantallaInicioActivity;
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        long tiempoActual = Long.parseLong(objects[0].toString());

        while (System.currentTimeMillis() - tiempoActual < tiempoMax && (!isCancelled()) ) {
            //zzzzz
        }

        if(isCancelled()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean) {
            this.pantallaInicioActivity.preguntarRefresh();
        }
        super.onPostExecute(aBoolean);
    }
}
