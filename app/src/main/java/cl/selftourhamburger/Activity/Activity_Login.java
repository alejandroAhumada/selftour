package cl.selftourhamburger.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cl.selftourhamburger.DataBase.DataBaseHelper;
import cl.selftourhamburger.RestClient.RestClient;
import cl.selftourhamburger.Util.AlertUtils;
import cl.selftourhamburger.model.pojo.Recorrido;
import cl.selftourhamburger.model.pojo.UsuarioIngresado;

public class Activity_Login extends Activity {

    private RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cl.selftourhamburger.R.layout.activity_login);

    }

    public void onClick(View view) {

        switch (view.getId()) {
            case cl.selftourhamburger.R.id.login_enter:

                TextView user = (TextView) findViewById(cl.selftourhamburger.R.id.login_user);
                TextView pass = (TextView) findViewById(cl.selftourhamburger.R.id.login_password);

                new LoginTask().execute(user.getText().toString(), pass.getText().toString());
                break;
        }
    }

    private class LoginTask extends AsyncTask<String, Void, UsuarioIngresado> {

        @Override
        protected UsuarioIngresado doInBackground(String... params) {
            UsuarioIngresado usuarioIngresado = restClient.login(params[0], params[1]);
            List<Recorrido> listRecorridos = restClient.getRecorridoDesdeServidor();
            setUsuarioIngrsadoABD(usuarioIngresado);
            setRecorridoABD(listRecorridos);

            if (usuarioIngresado.getLoginCorrecto()) {
                return usuarioIngresado;
            } else {
                return usuarioIngresado;
            }

        }

        @Override
        protected void onPostExecute(UsuarioIngresado usuarioIngresado) {
            if (!usuarioIngresado.getLoginCorrecto()) {

                AlertUtils.showErrorAlert(Activity_Login.this, "Error de Ingreso", "Credenciales Invalidas");
            } else {

                Log.i("Activity_Login", "ActivityLogin OK...");

                Intent intent = new Intent(Activity_Login.this, Activity_Pantalla_Principal.class);
                startActivity(intent);
            }
        }
    }

    private void setUsuarioIngrsadoABD(UsuarioIngresado usuarioIngrsado) {

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();

        try {
            database.delete("login", null, null);
            ContentValues contentValues = new ContentValues();
            database.beginTransaction();

            contentValues.put("USER", usuarioIngrsado.getUsername());
            contentValues.put("PASSWORD", usuarioIngrsado.getPassword());
            contentValues.put("NOMBRE", usuarioIngrsado.getNombre());
            contentValues.put("MAIL", usuarioIngrsado.getMail());

            database.insert("login", null, contentValues);

            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (SQLException e) {
            Log.e("Err setUsuarioIngrsado ", e.getMessage());
        } finally {
            db.close();
            database.close();
        }

    }

    private void setRecorridoABD(List<Recorrido> listRecorridos) {

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();
        database.delete("puntos_de_recorridos", null, null);
        try {
            for (int i = 0; i < listRecorridos.size(); i++) {

                for (int ii = 0; ii < listRecorridos.get(i).getListPuntos().size(); ii++) {

                    ContentValues contentValues = new ContentValues();
                    database.beginTransaction();

                    contentValues.put("NOMBRE_RECORRIDO", listRecorridos.get(i).getNombreRecorrido());
                    contentValues.put("DESCRIPCION_RECORRIDO", listRecorridos.get(i).getDescripcionRecorrido());
                    contentValues.put("DURACION", listRecorridos.get(i).getDuracion());
                    contentValues.put("POSICION", listRecorridos.get(i).getListPuntos().get(ii).getPos());
                    contentValues.put("NOMBRE_LUGAR", listRecorridos.get(i).getListPuntos().get(ii).getNombreLugar());
                    contentValues.put("DESCRIPCION_LUGAR", listRecorridos.get(i).getListPuntos().get(ii).getDescLugar());
                    contentValues.put("LATITUD", listRecorridos.get(i).getListPuntos().get(ii).getLatitud());
                    contentValues.put("LONGITUD", listRecorridos.get(i).getListPuntos().get(ii).getLongitud());
                    contentValues.put("ID_MARCA", listRecorridos.get(i).getListPuntos().get(ii).getIdMarca());
                    contentValues.put("NOMBRE_TIPO_MARCA", listRecorridos.get(i).getListPuntos().get(ii).getNombreTmarca());

                    database.insert("puntos_de_recorridos", null, contentValues);
                    database.setTransactionSuccessful();
                    database.endTransaction();
                }

            }

        } catch (SQLException e) {
            Log.e("Err setRecorridoABD ", e.getMessage());
        } finally {
            db.close();
            database.close();
        }

    }


}
