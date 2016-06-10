package cl.selftourhamburger.model.pojo;

/**
 * Created by Alejandro on 09-06-2016.
 */
public class Puntos {
    int pos;
    String nombreLugar;
    String descLugar;
    double longitud;
    double latitud;
    int idMarca;
    String nombreTmarca;

    public Puntos() {
    }

    public Puntos(int pos, String nombreLugar, String descLugar, double longitud, double latitud, int idMarca, String nombreTmarca) {
        this.pos = pos;
        this.nombreLugar = nombreLugar;
        this.descLugar = descLugar;
        this.longitud = longitud;
        this.latitud = latitud;
        this.idMarca = idMarca;
        this.nombreTmarca = nombreTmarca;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
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
}
