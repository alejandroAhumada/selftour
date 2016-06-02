package cl.selftourhamburger.model.pojo;

/**
 * Created by Alejandro on 02-06-2016.
 */
public class UsuarioIngresado {

    String username;
    String password;
    String nombre;
    String mail;
    Boolean loginCorrecto;

    public UsuarioIngresado() {
    }

    public UsuarioIngresado(String username, String password, String nombre, String mail, Boolean loginCorrecto) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.mail = mail;
        this.loginCorrecto = loginCorrecto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Boolean getLoginCorrecto() {
        return loginCorrecto;
    }

    public void setLoginCorrecto(Boolean loginCorrecto) {
        this.loginCorrecto = loginCorrecto;
    }
}
