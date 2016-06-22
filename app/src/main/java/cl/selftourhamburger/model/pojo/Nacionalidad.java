package cl.selftourhamburger.model.pojo;

/**
 * Created by Alejandro on 21-06-2016.
 */
public class Nacionalidad {

    private int id;
    private String nacionalidad;

    public Nacionalidad() {
    }

    public Nacionalidad(int id, String nacionalidad) {
        this.id = id;
        this.nacionalidad = nacionalidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    @Override
    public String toString() {
        if (id == 0)
            return nacionalidad;
        else
            return id + " " + nacionalidad;
    }
}
