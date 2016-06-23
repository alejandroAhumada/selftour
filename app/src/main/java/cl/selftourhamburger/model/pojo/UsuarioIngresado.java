package cl.selftourhamburger.model.pojo;

/**
 * Created by Alejandro on 02-06-2016.
 */
public class UsuarioIngresado {

    String nombre;
    String apellido;
    String mail;
    Boolean canLogin;
    Boolean haveLogon;

    public UsuarioIngresado() {
    }

    public UsuarioIngresado(String nombre, String apellido,String mail, Boolean canLogin, Boolean haveLogon) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.canLogin = canLogin;
        this.haveLogon = haveLogon;
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

    public Boolean getCanLogin() {
        return canLogin;
    }

    public void setCanLogin(Boolean canLogin) {
        this.canLogin = canLogin;
    }

    public Boolean getHaveLogon() {
        return haveLogon;
    }

    public void setHaveLogon(Boolean haveLogon) {
        this.haveLogon = haveLogon;
    }
}
