package cl.selftourhamburger.model.pojo;

import java.util.List;

/**
 * Created by Alejandro on 08-07-2016.
 */
public class Destino {
    int idDestino;
    String nombreDestino;
    String descripcionDelDestino;
    List<Puntos> listaPuntos;

    public Destino() {
    }

    public Destino(int idDestino, String nombreDestino, String descripcionDelDestino, List<Puntos> listaPuntos) {
        this.idDestino = idDestino;
        this.nombreDestino = nombreDestino;
        this.descripcionDelDestino = descripcionDelDestino;
        this.listaPuntos = listaPuntos;
    }

    public int getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(int idDestino) {
        this.idDestino = idDestino;
    }

    public List<Puntos> getListaPuntos() {
        return listaPuntos;
    }

    public void setListaPuntos(List<Puntos> listaPuntos) {
        this.listaPuntos = listaPuntos;
    }

    public String getDescripcionDelDestino() {
        return descripcionDelDestino;
    }

    public void setDescripcionDelDestino(String descripcionDelDestino) {
        this.descripcionDelDestino = descripcionDelDestino;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }
}
