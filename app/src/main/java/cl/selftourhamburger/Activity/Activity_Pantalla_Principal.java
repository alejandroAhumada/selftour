package cl.selftourhamburger.Activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.selftourhamburger.DataBase.DataBaseHelper;
import cl.selftourhamburger.Fragment.FragmentConoce;
import cl.selftourhamburger.Fragment.FragmentRecorre;
import cl.selftourhamburger.Fragment.FragmentExplora;
import cl.selftourhamburger.R;
import cl.selftourhamburger.RestClient.RestClient;
import cl.selftourhamburger.Util.AlertUtils;
import cl.selftourhamburger.model.pojo.Recorrido;
import cl.selftourhamburger.model.pojo.UsuarioIngresado;

public class Activity_Pantalla_Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GoogleApiClient mGoogleApiClient;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SharedPreferences sp;
    private int[] tabIcons = {
            R.mipmap.ic_home_white_24dp,
            R.mipmap.ic_map_white_24dp,
            R.mipmap.ic_local_offer_white_24dp,
    };
    private RestClient restClient;
    private Dialog dialog = null;
    HashMap<String, Integer> listDestinos;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cl.selftourhamburger.R.layout.activity_main);

        String destinoSeleccionado = getIntent().getStringExtra("destinoSeleccionado");
        if(destinoSeleccionado != null){
            this.setTitle(destinoSeleccionado);
        }

        restClient = new RestClient();
        sp = getApplicationContext().getSharedPreferences("cl.selftourhamburger", Context.MODE_MULTI_PROCESS);

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        mGoogleApiClient.connect();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        this.context = this;

        //HASTA AQUI
        Toolbar toolbar = (Toolbar) findViewById(cl.selftourhamburger.R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(cl.selftourhamburger.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, cl.selftourhamburger.R.string.navigation_drawer_open, cl.selftourhamburger.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(cl.selftourhamburger.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(cl.selftourhamburger.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(cl.selftourhamburger.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == cl.selftourhamburger.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.destinos) {

            showRadioButtonDialog(this);

            // Handle the camera action
            //} else if (id == cl.selftourhamburger.R.id.nav_gallery) {

            //} else if (id == cl.selftourhamburger.R.id.nav_slideshow) {

            //} else if (id == cl.selftourhamburger.R.id.nav_manage) {

        } else if (id == cl.selftourhamburger.R.id.nav_share) {

            dialog = new Dialog(this);

            dialog.setContentView(R.layout.custom_dialog);
            dialog.setTitle("Custom Alert Dialog");

            final EditText txtContraseñaActual = (EditText) dialog.findViewById(R.id.ContraseñaActual);
            final EditText txtContraseñaNueva = (EditText) dialog.findViewById(R.id.ContraseñaNueva);
            final EditText txtConfirmarNuevaContraseña = (EditText) dialog.findViewById(R.id.ConfirmarNuevaContraseña);

            Button btnSave = (Button) dialog.findViewById(R.id.save);
            Button btnCancel = (Button) dialog.findViewById(R.id.cancel);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String user = sp.getString("login_user", "vacio");

                    final String contraseñaActual = txtContraseñaActual.getText().toString();
                    final String contraseñaNueva = txtContraseñaNueva.getText().toString();
                    final String confirmarNuevaContraseña = txtConfirmarNuevaContraseña.getText().toString();

                    if (contraseñaNueva.equalsIgnoreCase(confirmarNuevaContraseña)) {

                        new CambioDePassTask().execute(user, contraseñaActual, contraseñaNueva);

                    } else {
                        AlertUtils.showErrorAlert(Activity_Pantalla_Principal.this, "Cambio de Password Fallido", "Contraseñas no coinciden,\n Favor volver a intentar");
                    }


                }
            });

            dialog.show();

        } else if (id == cl.selftourhamburger.R.id.cerrarSecion) {

            sp.edit().remove("login_user").commit();
            sp.edit().remove("login_pass").commit();
            sp.edit().remove("login").commit();

            signOutFromGplus();

            Intent intent = new Intent(Activity_Pantalla_Principal.this, Activity_Login_Signin.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(cl.selftourhamburger.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentRecorre(), "Recorre");
        adapter.addFrag(new FragmentExplora(), "Explora");
        adapter.addFrag(new FragmentConoce(), "Conoce");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    private class CambioDePassTask extends AsyncTask<String, Void, Integer> {

        String logeado;

        @Override
        protected Integer doInBackground(String... params) {

            String user = params[0];
            String passActual = params[1];
            String passNueva = params[2];

            int resultado = restClient.updatePassword(user, passActual, passNueva);

            return resultado;
        }

        @Override
        protected void onPostExecute(Integer resultado) {

            switch (resultado) {
                case 0:
                    AlertUtils.showErrorAlert(Activity_Pantalla_Principal.this, "Cambio de Password Fallido", "Error al Intentar Cambiar Password. Volver a Intentar");
                    break;
                case 1:
                    AlertUtils.showErrorAlert(Activity_Pantalla_Principal.this, "Cambio de Password Exitoso", "Contraseñas cambiada Exitosamente");
                    dialog.dismiss();
                    break;
                case 2:
                    AlertUtils.showErrorAlert(Activity_Pantalla_Principal.this, "Cambio de Password Fallido", "Contraseñas Actual Incorrecta, Favor volver a intentar");
                    break;
            }
        }
    }

    private void showRadioButtonDialog(final Activity_Pantalla_Principal activity_pantalla_principal) {

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);

        listDestinos = getListDestinos();

        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for (Map.Entry<String, Integer> entry : listDestinos.entrySet()) {
            RadioButton rb = new RadioButton(this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(entry.getKey());
            rg.addView(rb);
        }
        Toast.makeText(this, "Selecciones su Destino", Toast.LENGTH_SHORT).show();
        dialog.show();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        Log.e("selected RadioButton->", btn.getText().toString());

                        updateDestinoUsuario(btn.getText().toString(), listDestinos);
                        dialog.dismiss();
                        System.out.println("CAMBIO DE DEST");

                        Intent intent = new Intent(Activity_Pantalla_Principal.this, Activity_Cambio_Destino.class);
                        intent.putExtra("destinoSeleccionado", btn.getText().toString());
                        context.startActivity(intent);

                        //activity_pantalla_principal.setTitle(btn.getText().toString());

                    }
                }
            }
        });

    }

    private HashMap<String, Integer> getListDestinos() {

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();

        HashMap<String, Integer> listDestinos = new HashMap<>();
        String[] columns = {"ID_DESTINO","NOMBRE_DESTINO"};

        Cursor cursor = database.query("puntos_de_recorridos", columns, null, null, "NOMBRE_DESTINO", null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Integer idDestino= cursor.getInt(cursor.getColumnIndex("ID_DESTINO"));
                String nombreDestino = cursor.getString(cursor.getColumnIndex("NOMBRE_DESTINO"));
                listDestinos.put(nombreDestino, idDestino);
            }
        }

        return listDestinos;
    }

    private void updateDestinoUsuario(String destinoSeleccionado, HashMap<String, Integer> listDestinos) {
        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();

        try {
            ContentValues contentValues = new ContentValues();
            database.beginTransaction();

            String user = sp.getString("login_user", "Vacio");

            contentValues.put("ID_DESTINO", listDestinos.get(destinoSeleccionado));
            contentValues.put("NOMBRE_DESTINO", destinoSeleccionado);

            String where = "NOMBRE_USUARIO = \""+user+"\"";

            database.update("usuario_destino", contentValues, where, null);
            database.setTransactionSuccessful();
            database.endTransaction();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
            database.close();
        }

    }
}
