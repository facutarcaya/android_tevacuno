package com.example.micovid.comm;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class Communication {
    private static final String LOGIN_URL = "http://so-unlam.net.ar/api/api/login";
    private static final String REGISTER_URL = "http://so-unlam.net.ar/api/api/register";
    private static final String REFRESH_URL = "http://so-unlam.net.ar/api/api/refresh";
    private static  final String SEND_AMBIENTE = "PROD";
    private static  final String SEND_COMMISSION = "3900";
    private static  final String SEND_GROUP = "5";

    public static final String MSG_ERROR = "ERROR_CONNECTION";

    public String loginUsuario(String email, String password) {
        String result = null;
        JSONObject object = new JSONObject();

        try {
            object.put("email", email);
            object.put("password", password);

            Log.i("debug104", "Se envia al servidor " + object.toString());

            URL url = new URL(LOGIN_URL);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(object.toString().getBytes("UTF-8"));

            dataOutputStream.flush();
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK
                    ||responseCode == HttpURLConnection.HTTP_CREATED) {

                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                result = convertInputStreamToString(inputStreamReader).toString();


            } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getErrorStream());
                result = convertInputStreamToString(inputStreamReader).toString();
            } else {
                result = MSG_ERROR;
                return result;
            }

            dataOutputStream.close();
            connection.disconnect();

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            result = MSG_ERROR;
        }

        Log.i("debug104", "Recibo " + result);

        return result;
    }

    public String registrarUsuario(String name, String lastname, String dni, String email, String password) {
        String result = null;
        JSONObject object = new JSONObject();

        try {
            object.put("env",SEND_AMBIENTE);
            object.put("name",name);
            object.put("lastname",lastname);
            object.put("dni",dni);
            object.put("email", email);
            object.put("password", password);
            object.put("commission",SEND_COMMISSION);
            object.put("group",SEND_GROUP);

            Log.i("debug104", "Se envia al servidor " + object.toString());

            URL url = new URL(REGISTER_URL);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(object.toString().getBytes("UTF-8"));

            dataOutputStream.flush();
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK
                    ||responseCode == HttpURLConnection.HTTP_CREATED) {

                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                result = convertInputStreamToString(inputStreamReader).toString();


            } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getErrorStream());
                result = convertInputStreamToString(inputStreamReader).toString();
            } else {
                result = MSG_ERROR;
                return result;
            }

            dataOutputStream.close();
            connection.disconnect();

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            result = MSG_ERROR;
        }

        Log.i("debug104", "Recibo " + result);

        return result;
    }

    public String refrescarToken(String token_refresh) {
        String result = null;
        try {

            URL url = new URL(REFRESH_URL);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization","Bearer " + new String(Base64.getEncoder().encode(token_refresh.getBytes())));
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("PUT");

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());

            dataOutputStream.flush();
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK
                    ||responseCode == HttpURLConnection.HTTP_CREATED) {

                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                result = convertInputStreamToString(inputStreamReader).toString();


            } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getErrorStream());
                result = convertInputStreamToString(inputStreamReader).toString();
            } else {
                result = MSG_ERROR;
                return result;
            }

            dataOutputStream.close();
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            result = MSG_ERROR;
        }

        Log.i("debug104", "Recibo " + result);

        return result;
    }

    private static StringBuilder convertInputStreamToString(InputStreamReader inputStreamReader) throws IOException {
        BufferedReader br = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ( (line = br.readLine()) != null ){
            stringBuilder.append(line + "\n");
        }
        br.close();
        return stringBuilder;
    }
}
