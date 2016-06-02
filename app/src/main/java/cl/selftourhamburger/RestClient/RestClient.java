package cl.selftourhamburger.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import cl.selftourhamburger.model.pojo.UsuarioIngresado;

/**
 * Created by Alejandro on 01-06-2016.
 */
public class RestClient {

    public static UsuarioIngresado login(String username, String password){

        Map<String,Object> params = new LinkedHashMap<>();
        StringBuilder sb;
        UsuarioIngresado usuarioIngresado = new UsuarioIngresado();
        try {
            params.put("user", username);
            params.put("pass", password);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            sb = getStringParaJson(postDataBytes);
            usuarioIngresado = getDatosUsuarioIngresado(sb);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(usuarioIngresado.getUsername() == null){
            usuarioIngresado.setLoginCorrecto(false);
        }

        return usuarioIngresado;
    }

    private static StringBuilder getStringParaJson(byte[] postDataBytes) {

        HttpURLConnection conn;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL("http://selftour.xcloud.cl/login.php");
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            String responseMessage = conn.getResponseMessage();
            System.out.printf("RESPONSE: "+responseMessage);
            int status = conn.getResponseCode();
            System.out.println(" STATUS "+status);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb;
    }

    private static UsuarioIngresado getDatosUsuarioIngresado(StringBuilder sb) {

        UsuarioIngresado usuarioIngresado = new UsuarioIngresado();

        JSONObject json;
        try {
            json = new JSONObject(sb.toString());
            System.out.println("JSON: "+json);
            usuarioIngresado.setLoginCorrecto(json.getBoolean("acceso"));

            if(usuarioIngresado.getLoginCorrecto()){

                JSONObject usuario = json.getJSONObject("usuario");
                usuarioIngresado.setUsername(usuario.getString("username"));
                usuarioIngresado.setPassword(usuario.getString("password"));
                usuarioIngresado.setNombre(usuario.getString("nombre"));
                usuarioIngresado.setMail(usuario.getString("mail"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return usuarioIngresado;
    }


}
