package cl.selftourhamburger.model.pojo;

import java.util.Date;
import java.util.List;

/**
 * Created by Alejandro on 09-06-2016.
 */
public class Recorrido {

    int idDestino;
    String nombreDestino;
    String nombreRecorrido;
    String descripcionRecorrido;
    String duracion;
    List<Puntos> listPuntos;

    public Recorrido() {
    }

    public int getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(int idDestino) {
        this.idDestino = idDestino;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }

    public String getNombreRecorrido() {
        return nombreRecorrido;
    }

    public void setNombreRecorrido(String nombreRecorrido) {
        this.nombreRecorrido = nombreRecorrido;
    }

    public String getDescripcionRecorrido() {
        return descripcionRecorrido;
    }

    public void setDescripcionRecorrido(String descripcionRecorrido) {
        this.descripcionRecorrido = descripcionRecorrido;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public List<Puntos> getListPuntos() {
        return listPuntos;
    }

    public void setListPuntos(List<Puntos> listPuntos) {
        this.listPuntos = listPuntos;
    }
}
