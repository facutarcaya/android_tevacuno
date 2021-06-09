package com.example.micovid.asincronico;

import android.os.AsyncTask;

import com.example.micovid.registrar.RegistrarActivity;

public class AsincroTask extends AsyncTask<Object, Void, Boolean> {
    private RegistrarActivity registrarActivity;

    public void TareaAsincro (RegistrarActivity registrarActivity) {
        this.registrarActivity = registrarActivity;
    }

    @Override
    protected void onPreExecute() {
        registrarActivity.toggleProgressBar(true);
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        return null;
    }
}
