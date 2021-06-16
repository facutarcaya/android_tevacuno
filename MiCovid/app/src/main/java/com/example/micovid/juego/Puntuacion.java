package com.example.micovid.juego;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Puntuacion {
    String nombre;
    String puntuacion;
    String fecha;

    public Puntuacion(String nombre, int puntuacion) {
        this.nombre = nombre;
        this.puntuacion = String.valueOf(puntuacion);
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        this.fecha =    calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                        (calendar.get(Calendar.MONTH) + 1) +  "/" +
                        calendar.get(Calendar.YEAR) + " " +
                        calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                        calendar.get(Calendar.MINUTE) + ":" +
                        calendar.get(Calendar.SECOND);
    }

    public Puntuacion(String nombre, String puntuacion, String fecha) {
        this.nombre = nombre;
        this.puntuacion = puntuacion;
        this.fecha = fecha;
    }

    public String toJson() {
        return "{ \"nombre\": \"" + nombre + "\"," +
                " \"fecha\": \"" + fecha + "\"," +
                " \"puntuacion\": \"" + puntuacion + "\"}";
    }

    public String getNombre() {
        return nombre;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public String getFecha() {
        return fecha;
    }
}
