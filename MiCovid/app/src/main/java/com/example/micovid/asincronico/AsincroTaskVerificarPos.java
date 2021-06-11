package com.example.micovid.asincronico;

import android.os.AsyncTask;
import android.util.Log;

import com.example.micovid.juego.GameActivity;
import com.example.micovid.login.LoginActivity;
import com.example.micovid.pantallaprincipal.PantallaInicioActivity;

public class AsincroTaskVerificarPos extends AsyncTask<Object, Void, Boolean>{
    private GameActivity gameActivity;
    private long segundoInicial;

    public AsincroTaskVerificarPos(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    @Override
    protected void onPreExecute() {
        this.gameActivity.validando = true;
        this.segundoInicial = System.currentTimeMillis();
        Log.d("Angulos", "Validando pos");
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        while( System.currentTimeMillis() - segundoInicial <= 1000 ) {
            if(!this.gameActivity.verificarPos()) {
                Log.d("Angulos", "Se perdio pos");
                return false;
            } else {
                //Log.d("Angulos", "SIGUE");
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        this.gameActivity.validando = false;
        if(aBoolean) {
            this.gameActivity.sumarPunto();
        }
    }
}
