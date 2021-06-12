package com.example.micovid.asincronico;

import android.os.AsyncTask;
import android.util.Log;

import com.example.micovid.actividadprincipal.MainActivity;
import com.example.micovid.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        JSONObject object = new JSONObject();
        String result = null;
        try {
            object.put("email", "snalbarracin@gmail.com");
            object.put("password", "Hola1234");


            URL url = new URL("http://so-unlam.net.ar/api/api/login");
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(object.toString().getBytes("UTF-8"));

            Log.i("debug104", "Se envia al servidor " + object.toString());

            dataOutputStream.flush();
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {

                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                result = convertInputStreamToString(inputStreamReader).toString();

            } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {

                InputStreamReader inputStreamReader = new InputStreamReader(connection.getErrorStream());
                result = convertInputStreamToString(inputStreamReader).toString();

            } else {
                result = "NOT_OK";
            }

            dataOutputStream.close();
            connection.disconnect();

            JSONObject answer = new JSONObject(result);

            result = answer.get("success").toString();

            Log.i("debug166", result);
        } catch (JSONException | IOException e) {
            Log.i("debug104", String.valueOf(e));
            e.printStackTrace();
            result = "false";
        }

        if (result.matches("true")) {
            return true;
        } else {
            return false;
        }

    }
    public static StringBuilder convertInputStreamToString(InputStreamReader inputStreamReader) throws IOException {
        BufferedReader br = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ( (line = br.readLine()) != null ){
            stringBuilder.append(line + "\n");
        }
        br.close();
        return stringBuilder;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        this.loginActivity.toggleProgressBar(false);
        if(aBoolean) {
            this.loginActivity.lanzarActivity(MainActivity.class);
            this.loginActivity.showMessage("Datos correctos");
        } else {
            this.loginActivity.showMessage("Datos incorrectos");
            this.loginActivity.habilitarBotones(true);
        }
        super.onPostExecute(aBoolean);
    }
}
