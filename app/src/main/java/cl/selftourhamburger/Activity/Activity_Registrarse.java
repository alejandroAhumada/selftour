package cl.selftourhamburger.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cl.selftourhamburger.DataBase.DataBaseHelper;
import cl.selftourhamburger.Listeners.ListenerSpnNacionalidad;
import cl.selftourhamburger.R;
import cl.selftourhamburger.RestClient.RestClient;
import cl.selftourhamburger.model.pojo.Nacionalidad;
import cl.selftourhamburger.model.pojo.Recorrido;
import cl.selftourhamburger.model.pojo.RegistroUsuario;

public class Activity_Registrarse extends Activity {

    private RestClient restClient;
    Spinner spinnerNacionalidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_activity__registrarse);

        spinnerNacionalidades = (Spinner) findViewById(R.id.spnNacionalidad);
        spinnerNacionalidades.setAdapter(getSpinnerAdapterNacionalidad());

        ListenerSpnNacionalidad listenerSpnNacionalidad = new ListenerSpnNacionalidad();

        spinnerNacionalidades.setOnItemSelectedListener(listenerSpnNacionalidad);
    }

    public void onClickRegistrarse(View view) {

        switch (view.getId()) {
            case R.id.registrarse_enter:

                RegistroUsuario registroUsuario = new RegistroUsuario();

                EditText nombre = (EditText) findViewById(R.id.login_nombre);
                EditText apellido = (EditText) findViewById(R.id.login_apellido);
                EditText mail = (EditText) findViewById(R.id.login_mail);
                EditText genero = (EditText) findViewById(R.id.login_genero);
                EditText nombreUsuario = (EditText) findViewById(R.id.login_user);
                EditText password = (EditText) findViewById(R.id.login_password);
                EditText fechaNacimiento = (EditText) findViewById(R.id.login_fechaNacimiento);


                registroUsuario.setNombre(nombre.getText().toString());
                registroUsuario.setApellido(apellido.getText().toString());
                registroUsuario.setMail(mail.getText().toString());
                registroUsuario.setGenero(genero.getText().toString());
                registroUsuario.setNombreUsuario(nombreUsuario.getText().toString());
                registroUsuario.setPassword(password.getText().toString());
                registroUsuario.setFechaNacimiento(fechaNacimiento.getText().toString());
                registroUsuario.setNacionalidad(spinnerNacionalidades.getSelectedItemPosition());


                new SigninTask().execute(registroUsuario);

                break;
        }

    }

    private SpinnerAdapter getSpinnerAdapterNacionalidad() {
        List<Nacionalidad> nacionalidad = new ArrayList<>();

        nacionalidad = getListNacionalidadesDeBD();

        ArrayAdapter<Nacionalidad> dataAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_btn, nacionalidad);
        dataAdapter.setDropDownViewResource(R.layout.spinner_prod);

        return dataAdapter;
    }

    private List<Nacionalidad> getListNacionalidadesDeBD() {
        List<Nacionalidad> listNacionalidades = new ArrayList<>();
        Nacionalidad nacionalidads = new Nacionalidad();

        nacionalidads.setId(0);
        nacionalidads.setNacionalidad("Nacionalidad");

        listNacionalidades.add(nacionalidads);

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();

        String[] columns = {"ID_NACIONALIDAD", "NACIONALIDAD"};

        Cursor cursor = database.query("nacionalidad", columns, null, null, null, null, "ID_NACIONALIDAD");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Nacionalidad nacionalidad = new Nacionalidad();

                nacionalidad.setId(cursor.getInt(cursor.getColumnIndex("ID_NACIONALIDAD")));
                nacionalidad.setNacionalidad(cursor.getString(cursor.getColumnIndex("NACIONALIDAD")));

                listNacionalidades.add(nacionalidad);
            }
        }

        return listNacionalidades;
    }

    private class SigninTask extends AsyncTask<RegistroUsuario, Void, Integer> {


        @Override
        protected Integer doInBackground(RegistroUsuario... params) {
            int nextToLogin = 0;
            try {
                RegistroUsuario registroUsuario = params[0];
                restClient = new RestClient();

                nextToLogin = restClient.registrarse(registroUsuario);

                List<Recorrido> listRecorridos = restClient.getRecorridoDesdeServidor();
                setRecorridoABD(listRecorridos);

                this.finalize();

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return nextToLogin;
        }

        @Override
        protected void onPostExecute(Integer nextToLogin) {

            if (nextToLogin == 1) {

                System.out.println("REGISTRADO!!");

                Intent intent = new Intent(Activity_Registrarse.this, Activity_Pantalla_Principal.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Registro fallido", Toast.LENGTH_SHORT).show();
            }
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
