package cl.selftourhamburger.model.pojo;

import android.media.MediaPlayer;

/**
 * Created by Alejandro on 09-06-2016.
 */
public class Puntos {
    int pos;
    String nombreDestino;
    String nombreLugar;
    String descLugar;
    double longitud;
    double latitud;
    int idMarca;
    String nombreTmarca;
    MediaPlayer mediaPlayer;

    public Puntos() {
    }

    public Puntos(int pos, String nombreDestino, String nombreLugar, String descLugar, double longitud, double latitud, int idMarca, String nombreTmarca, MediaPlayer mediaPlayer) {
        this.pos = pos;
        this.nombreDestino = nombreDestino;
        this.nombreLugar = nombreLugar;
        this.descLugar = descLugar;
        this.longitud = longitud;
        this.latitud = latitud;
        this.idMarca = idMarca;
        this.nombreTmarca = nombreTmarca;
        this.mediaPlayer = mediaPlayer;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public void setNombreLugar(String nombreLugar) {
        this.nombreLugar = nombreLugar;
    }

    public String getDescLugar() {
        return descLugar;
    }

    public void setDescLugar(String descLugar) {
        this.descLugar = descLugar;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombreTmarca() {
        return nombreTmarca;
    }

    public void setNombreTmarca(String nombreTmarca) {
        this.nombreTmarca = nombreTmarca;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
}