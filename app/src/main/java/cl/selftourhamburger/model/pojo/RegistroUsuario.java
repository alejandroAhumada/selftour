package cl.selftourhamburger.model.pojo;

import java.util.Date;

/**
 * Created by Alejandro on 21-06-2016.
 */
public class RegistroUsuario {

    String nombre;
    String apellido;
    String mail;
    String genero;
    String nombreUsuario;
    String password;
    String fechaNacimiento;
    int nacionalidad;

    public RegistroUsuario() {
    }

    public RegistroUsuario(String nombre, String apellido, String mail, String genero, String nombreUsuario, String password, String fechaNacimiento, int nacionalidad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.genero = genero;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(int nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
}
