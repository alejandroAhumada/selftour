package cl.selftourhamburger.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cl.selftourhamburger.DataBase.DataBaseHelper;
import cl.selftourhamburger.Listeners.ListenerSpnGenero;
import cl.selftourhamburger.Listeners.ListenerSpnNacionalidad;
import cl.selftourhamburger.R;
import cl.selftourhamburger.RestClient.RestClient;
import cl.selftourhamburger.model.pojo.Destino;
import cl.selftourhamburger.model.pojo.Nacionalidad;
import cl.selftourhamburger.model.pojo.Recorrido;
import cl.selftourhamburger.model.pojo.RegistroUsuario;
import android.view.View.OnClickListener;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;

public class Activity_Registrarse extends Activity implements OnClickListener{

    private RestClient restClient;
    Spinner spinnerNacionalidades;
    Spinner spinnerGenero;
    private SharedPreferences sp;
    private String nombreUsuarioRegistrado;
    private SimpleDateFormat dateFormatter;
    private EditText fromDateEtxt;
    private DatePickerDialog fromDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_activity__registrarse);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        findViewsById();
        setDateTimeField();

        sp = getSharedPreferences("cl.selftourhamburger", Context.MODE_PRIVATE);

        spinnerNacionalidades = (Spinner) findViewById(R.id.spnNacionalidad);
        spinnerNacionalidades.setAdapter(getSpinnerAdapterNacionalidad());
        ListenerSpnNacionalidad listenerSpnNacionalidad = new ListenerSpnNacionalidad();
        spinnerNacionalidades.setOnItemSelectedListener(listenerSpnNacionalidad);

        spinnerGenero = (Spinner) findViewById(R.id.spnGenero);
        spinnerGenero.setAdapter(getSpinnerAdapterGenero());
        ListenerSpnGenero listenerSpnGenero = new ListenerSpnGenero();
        spinnerGenero.setOnItemSelectedListener(listenerSpnGenero);
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.login_fechaNacimiento);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();
    }

    public void onClickRegistrarse(View view) {
        StringBuilder vacio = new StringBuilder();
        switch (view.getId()) {
            case R.id.registrarse_enter:

                RegistroUsuario registroUsuario = new RegistroUsuario();

                EditText nombre = (EditText) findViewById(R.id.login_nombre);
                EditText apellido = (EditText) findViewById(R.id.login_apellido);
                EditText mail = (EditText) findViewById(R.id.login_mail);
                EditText nombreUsuario = (EditText) findViewById(R.id.login_user);
                EditText password = (EditText) findViewById(R.id.login_password);
                EditText fechaNacimiento = (EditText) findViewById(R.id.login_fechaNacimiento);

                String name = nombre.getText().toString();
                String firstName = apellido.getText().toString();
                String mails = mail.getText().toString();
                String username = nombreUsuario.getText().toString();
                String pass = password.getText().toString();
                boolean mailValid = isValidEmail(mails);
                String dateN = fechaNacimiento.getText().toString();

                if(name.isEmpty()){
                    vacio.append("-Nombre ");
                }if(firstName.isEmpty()) {
                    vacio.append("-Apellido");
                }if(mails.isEmpty()) {
                    vacio.append("-Mail");
                }if(username.isEmpty()) {
                    vacio.append("-Username");
                }if(pass.isEmpty()) {
                    vacio.append("-Password");
                }if(dateN.isEmpty()) {
                    vacio.append("-Fecha de Nacimiento");
                }


                registroUsuario.setNombre(name);
                registroUsuario.setApellido(firstName);
                registroUsuario.setMail(mails);
                registroUsuario.setNombreUsuario(username);
                registroUsuario.setPassword(pass);
                registroUsuario.setFechaNacimiento(dateN);
                registroUsuario.setNacionalidad(spinnerNacionalidades.getSelectedItemPosition());
                registroUsuario.setGenero(spinnerGenero.getSelectedItem().toString());

                if(vacio.toString().length() <= 0) {
                    if (username.length() >= 6) {
                        if (pass.length() >= 6) {
                            if (mailValid) {
                                new SigninTask().execute(registroUsuario);
                            } else {
                                Toast.makeText(getApplicationContext(), "Mail incorrecto", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Password debe tener mas de 6 caracteres", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Username debe tener mas de 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Favor Completar Todos Los Campos", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private SpinnerAdapter getSpinnerAdapterNacionalidad() {
        List<Nacionalidad> nacionalidad = new ArrayList<>();

        nacionalidad = getListNacionalidadesDeBD();

        ArrayAdapter<Nacionalidad> dataAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_nacionalidad_btn, nacionalidad);
        dataAdapter.setDropDownViewResource(R.layout.spinner_nacionalidad_prod);

        return dataAdapter;
    }

    private SpinnerAdapter getSpinnerAdapterGenero() {
        List<String> listGenero = new ArrayList<>();

        listGenero.add("Genero");
        listGenero.add("Masculino");
        listGenero.add("Femenino");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_genero_btn, listGenero);
        dataAdapter.setDropDownViewResource(R.layout.spinner_genero_prod);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        }
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
                List<Destino> listDestino = restClient.getDestino();
                setDestinoABD(listDestino);

                if(nextToLogin == 1){
                    nombreUsuarioRegistrado = registroUsuario.getNombreUsuario();
                    sp.edit().putString("login_user", registroUsuario.getNombreUsuario()).apply();
                    sp.edit().putString("login_pass", registroUsuario.getPassword()).apply();
                    sp.edit().putBoolean("login", true).apply();

                    guararLoginEnDB(registroUsuario);
                }

                this.finalize();

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return nextToLogin;
        }

        @Override
        protected void onPostExecute(Integer nextToLogin) {

            if (nextToLogin == 1) {

                showRadioButtonDialog();

            } else {
                Toast.makeText(getApplicationContext(), "Registro fallido", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void guararLoginEnDB(RegistroUsuario registroUsuario) {

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();
        database.delete("login", null, null);
        try {


            ContentValues contentValues = new ContentValues();
            database.beginTransaction();

            contentValues.put("NOMBRE", registroUsuario.getNombre());
            contentValues.put("APELLIDO", registroUsuario.getApellido());
            contentValues.put("MAIL", registroUsuario.getMail());

            database.insert("login", null, contentValues);
            database.setTransactionSuccessful();
            database.endTransaction();


        } catch (SQLException e) {
            Log.e("Err setDestinoABD ", e.getMessage());
        } finally {
            db.close();
            database.close();
        }
    }

    private void setDestinoABD(List<Destino> listDestino) {

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();
        database.delete("destino_punto_interes", null, null);
        try {
            for (int i = 0; i < listDestino.size(); i++) {

                for (int ii = 0; ii < listDestino.get(i).getListaPuntos().size(); ii++) {

                    ContentValues contentValues = new ContentValues();
                    database.beginTransaction();

                    contentValues.put("NOMBRE_DESTINO", listDestino.get(i).getNombreDestino());
                    contentValues.put("DESCRIPCION_DESTINO", listDestino.get(i).getDescripcionDelDestino());

                    contentValues.put("NOMBRE_LUGAR", listDestino.get(i).getListaPuntos().get(ii).getNombreLugar());
                    contentValues.put("DESCRIPCION_LUGAR", listDestino.get(i).getListaPuntos().get(ii).getDescLugar());
                    contentValues.put("LATITUD", listDestino.get(i).getListaPuntos().get(ii).getLatitud());
                    contentValues.put("LONGITUD", listDestino.get(i).getListaPuntos().get(ii).getLongitud());
                    contentValues.put("ID_MARCA", listDestino.get(i).getListaPuntos().get(ii).getIdMarca());
                    contentValues.put("NOMBRE_TIPO_MARCA", listDestino.get(i).getListaPuntos().get(ii).getNombreTmarca());

                    database.insert("destino_punto_interes", null, contentValues);
                    database.setTransactionSuccessful();
                    database.endTransaction();
                }

            }

        } catch (SQLException e) {
            Log.e("Err setDestinoABD ", e.getMessage());
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

    private void showRadioButtonDialog() {

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);
        List<String> listDestinos;

        listDestinos = getListDestinos();

        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for(int i=0;i<listDestinos.size();i++){
            RadioButton rb=new RadioButton(this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(listDestinos.get(i));
            rg.addView(rb);
        }
        Toast.makeText(this,"Selecciones su Destino",Toast.LENGTH_SHORT).show();
        dialog.show();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        Log.e("selected RadioButton->",btn.getText().toString());

                        guardarDestinoUsuario(btn.getText().toString());

                        Intent intent = new Intent(Activity_Registrarse.this, Activity_Pantalla_Principal.class);
                        intent.putExtra("destinoSeleccionado", btn.getText().toString());
                        startActivity(intent);

                    }
                }
            }
        });

    }

    private void guardarDestinoUsuario(String destinoSeleccionado) {
        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();

        try {
            ContentValues contentValues = new ContentValues();
            database.beginTransaction();

            contentValues.put("NOMBRE_USUARIO", nombreUsuarioRegistrado);
            contentValues.put("NOMBRE_DESTINO", destinoSeleccionado);

            database.insert("usuario_destino", null, contentValues);
            database.setTransactionSuccessful();
            database.endTransaction();

        }catch (SQLiteException e){
            e.printStackTrace();
        }finally {
            db.close();
            database.close();
        }

    }

    private List<String> getListDestinos() {

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();

        List<String> listDestinos = new ArrayList<>();
        String[] columns = {"NOMBRE_DESTINO"};

        Cursor cursor = database.query("destino_punto_interes", columns, null, null, "NOMBRE_DESTINO", null,null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                listDestinos.add(cursor.getString(cursor.getColumnIndex("NOMBRE_DESTINO")));
            }
        }

        return listDestinos;
    }
}
