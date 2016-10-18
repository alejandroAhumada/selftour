package cl.selftourhamburger.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cl.selftourhamburger.model.pojo.Destino;
import cl.selftourhamburger.model.pojo.Nacionalidad;
import cl.selftourhamburger.model.pojo.Puntos;
import cl.selftourhamburger.model.pojo.Recorrido;
import cl.selftourhamburger.model.pojo.RegistroUsuario;
import cl.selftourhamburger.model.pojo.UsuarioIngresado;

/**
 * Created by Alejandro on 01-06-2016.
 */
public class RestClient {

    public static UsuarioIngresado login(String username, String password, String tipoLogin) {

        Map<String, Object> params = new LinkedHashMap<>();
        StringBuilder sb;
        UsuarioIngresado usuarioIngresado = new UsuarioIngresado();
        try {
            if(tipoLogin.equalsIgnoreCase("selftour")){
                params.put("user", username);
                params.put("pass", password);
                params.put("social", tipoLogin);
            }else{
                params.put("user", username);
                params.put("social", tipoLogin);
            }


            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
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

        if (usuarioIngresado.getNombre() == null) {
            usuarioIngresado.setCanLogin(false);
        }

        return usuarioIngresado;
    }

    private static StringBuilder getStringParaJson(byte[] postDataBytes) {

        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {

            URL url = new URL("http://avedex.org/user/login");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            String responseMessage = conn.getResponseMessage();

            int status = conn.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return sb;
    }

    private static UsuarioIngresado getDatosUsuarioIngresado(StringBuilder sb) {

        UsuarioIngresado usuarioIngresado = new UsuarioIngresado();

        JSONObject json;
        try {
            json = new JSONObject(sb.toString());

            usuarioIngresado.setCanLogin(json.getBoolean("canLogin"));
            usuarioIngresado.setHaveLogon(json.getBoolean("haveLogon"));

            if (usuarioIngresado.getCanLogin()) {

                JSONObject usuario = json.getJSONObject("user");
                usuarioIngresado.setNombre(usuario.getString("nombre"));
                usuarioIngresado.setApellido(usuario.getString("apellido"));
                usuarioIngresado.setMail(usuario.getString("email"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return usuarioIngresado;
    }

    public static List<Recorrido> getRecorridoDesdeServidor() {
        List<Recorrido> listRecorrido = new ArrayList<>();
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL("http://avedex.org/route");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String responseMessage = conn.getResponseMessage();

            int status = conn.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }

            listRecorrido = getRecorrido(sb);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return listRecorrido;
    }

    private static List<Recorrido> getRecorrido(StringBuilder sb) {

        List<Recorrido> listRecorrido = new ArrayList<>();
        try {

            JSONArray array = new JSONArray(sb.toString());

            for (int i = 0; i < array.length(); i++) {
                Recorrido recorrido = new Recorrido();
                List<Puntos> puntosList = new ArrayList<>();

                JSONObject objectRecorrido = array.getJSONObject(i);
                recorrido.setNombreRecorrido(objectRecorrido.getString("nombreRecorrido"));
                recorrido.setDescripcionRecorrido(objectRecorrido.getString("descripcionRecorrido"));
                recorrido.setDuracion(objectRecorrido.getString("duracion"));
                JSONArray puntos = objectRecorrido.getJSONArray("puntos");

                for (int ii = 0; ii < puntos.length(); ii++) {
                    JSONObject objectPuntos = puntos.getJSONObject(ii);
                    Puntos puntos1 = new Puntos();
                    puntos1.setPos(objectPuntos.getInt("pos"));
                    puntos1.setNombreLugar(objectPuntos.getString("nombreLugar"));
                    puntos1.setDescLugar(objectPuntos.getString("descLugar"));
                    puntos1.setLongitud(objectPuntos.getDouble("lng"));
                    puntos1.setLatitud(objectPuntos.getDouble("lat"));
                    puntos1.setIdMarca(objectPuntos.getInt("idMarca"));
                    puntos1.setNombreTmarca(objectPuntos.getString("nombreTmarca"));

                    puntosList.add(puntos1);

                }

                recorrido.setListPuntos(puntosList);
                listRecorrido.add(recorrido);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listRecorrido;
    }

    public int registrarse(RegistroUsuario registroUsuario) {

        Map<String, Object> params = new LinkedHashMap<>();
        StringBuilder sb;
        int nextToLogin = 0;

        try {
            params.put("name", registroUsuario.getNombre());
            params.put("lastname", registroUsuario.getApellido());
            params.put("email", registroUsuario.getMail());
            params.put("gender", registroUsuario.getGenero());
            params.put("user", registroUsuario.getNombreUsuario());
            params.put("pass", registroUsuario.getPassword());
            params.put("birth", registroUsuario.getFechaNacimiento());
            params.put("social", "selftour");
            params.put("nation", registroUsuario.getNacionalidad());

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            sb = getStringParaJsonRegistrar(postDataBytes);
            nextToLogin = getDatosRegistroUsuario(sb);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return nextToLogin;
    }

    private static StringBuilder getStringParaJsonRegistrar(byte[] postDataBytes) {

        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL("http://avedex.org/user/new");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            String responseMessage = conn.getResponseMessage();

            int status = conn.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return sb;
    }

    private static int getDatosRegistroUsuario(StringBuilder sb) {

        int nextToLogin = 0;
        JSONObject json;
        try {
            json = new JSONObject(sb.toString());

            nextToLogin = json.getInt("nextToLogin");
            String mensaje = json.getString("mensaje");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*1= se puede logear, 0= no se puede logear */
        return nextToLogin;
    }

    public List<Nacionalidad> getNacionalidad() {
        List<Nacionalidad> nacionalidad = new ArrayList<>();

        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL("http://avedex.org/user/nacionalidad");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String responseMessage = conn.getResponseMessage();

            int status = conn.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }

            nacionalidad = getNacionalidades(sb);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return nacionalidad;
    }

    private static List<Nacionalidad> getNacionalidades(StringBuilder sb) {

        List<Nacionalidad> listNacionalidades = new ArrayList<>();
        try {

            JSONArray array = new JSONArray(sb.toString());

            for (int i = 0; i < array.length(); i++) {
                Nacionalidad nacionalidad = new Nacionalidad();

                JSONObject objectRecorrido = array.getJSONObject(i);
                nacionalidad.setId(objectRecorrido.getInt("id"));
                nacionalidad.setNacionalidad(objectRecorrido.getString("nombre"));

                listNacionalidades.add(nacionalidad);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listNacionalidades;
    }

    public List<Destino> getDestino() {

        List<Destino> destinos = new ArrayList<>();

        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL("http://avedex.org/openmap");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }

            destinos = getDestinos(sb);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return destinos;
    }

    private static List<Destino> getDestinos(StringBuilder sb) {
        System.out.println("SB: "+sb.toString());
        List<Destino> listDestinos = new ArrayList<>();

        try {

            JSONArray array = new JSONArray(sb.toString());

            for (int i = 0; i < array.length(); i++) {
                Destino destino = new Destino();
                List<Puntos> puntosList = new ArrayList<>();

                JSONObject objectDestino = array.getJSONObject(i);
                destino.setNombreDestino(objectDestino.getString("nombreDestino"));
                destino.setDescripcionDelDestino(objectDestino.getString("descripcionDestino"));

                JSONArray puntos = objectDestino.getJSONArray("puntos");

                for (int ii = 0; ii < puntos.length(); ii++) {
                    JSONObject objectPuntos = puntos.getJSONObject(ii);
                    Puntos puntos1 = new Puntos();
                    puntos1.setNombreLugar(objectPuntos.getString("nombreLugar"));
                    puntos1.setDescLugar(objectPuntos.getString("descLugar"));
                    puntos1.setLongitud(objectPuntos.getDouble("lng"));
                    puntos1.setLatitud(objectPuntos.getDouble("lat"));
                    puntos1.setIdMarca(objectPuntos.getInt("idMarca"));
                    puntos1.setNombreTmarca(objectPuntos.getString("nombreTmarca"));

                    puntosList.add(puntos1);

                }

                destino.setListaPuntos(puntosList);
                listDestinos.add(destino);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listDestinos;
    }

    public static int updatePassword (String username, String passwordActual, String passwordNueva) {

        Map<String, Object> params = new LinkedHashMap<>();
        StringBuilder sb;
        int resultadoCambioDePass = 0;
        try {

            params.put("user", username);
            params.put("pass", passwordActual);
            params.put("newpass", passwordNueva);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            sb = getStringParaJsonCambioPass(postDataBytes);
            resultadoCambioDePass = getResultadoCambioDePass(sb);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultadoCambioDePass;
    }

    private static StringBuilder getStringParaJsonCambioPass(byte[] postDataBytes) {

        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {

            URL url = new URL("http://avedex.org/user/pass/change");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            String responseMessage = conn.getResponseMessage();
            int status = conn.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return sb;
    }

    private static int getResultadoCambioDePass(StringBuilder sb) {

        int getResultadoCambioDePass = 0;

        JSONObject json;
        try {
            json = new JSONObject(sb.toString());

            getResultadoCambioDePass = json.getInt("success");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getResultadoCambioDePass;
    }

}
