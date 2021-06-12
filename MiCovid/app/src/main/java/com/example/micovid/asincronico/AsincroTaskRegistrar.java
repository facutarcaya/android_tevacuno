package com.example.micovid.asincronico;

import android.os.AsyncTask;
import android.util.Log;

import com.example.micovid.pantallaprincipal.PantallaInicioActivity;
import com.example.micovid.registrar.RegistrarActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        String result;
        JSONObject object = new JSONObject();
        try {
            //Thread.sleep(2000);
            String nombre = (String) objects[0];
            String apellido = (String) objects[1];
            String dni = (String) objects[2];
            String email = (String) objects[3];
            String password = (String) objects[4];
            object.put("nombre",nombre);
            object.put("nombre",apellido);
            object.put("nombre",dni);
            object.put("nombre",email);
            object.put("nombre",password);
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
        }}
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
