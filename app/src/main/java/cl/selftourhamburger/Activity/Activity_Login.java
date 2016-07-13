package cl.selftourhamburger.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cl.selftourhamburger.DataBase.DataBaseHelper;
import cl.selftourhamburger.R;
import cl.selftourhamburger.RestClient.RestClient;
import cl.selftourhamburger.Util.AlertUtils;
import cl.selftourhamburger.model.pojo.Recorrido;
import cl.selftourhamburger.model.pojo.UsuarioIngresado;

public class Activity_Login extends Activity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private RestClient restClient;
    private SharedPreferences sp;
    private String personName;
    private String email;
    private String personPhotoUrl;
    private String personGooglePlusProfile;
    private String nombreUsuarioRegistrado;

    /*VARIABLES LOGIN CON GOOGLE*/
    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "Activity_Login";
    private static final int PROFILE_PIC_SIZE = 400;

    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private SignInButton btnSignIn;

    private Button btnSignOut, btnRevokeAccess;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private LinearLayout llProfileLayout;
    /*VARIABLES LOGIN CON GOOGLE*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cl.selftourhamburger.R.layout.activity_login);

        sp = getApplicationContext().getSharedPreferences("cl.selftourhamburger", Context.MODE_MULTI_PROCESS);
        boolean login = sp.getBoolean("login", false);
        String user = sp.getString("login_user","Vacio");
        String destinoSeleccionado = getDestinoSelecionado(user);

        if (login) {
            Intent intent = new Intent(Activity_Login.this, Activity_Pantalla_Principal.class);
            intent.putExtra("destinoSeleccionado",destinoSeleccionado);
            startActivity(intent);
        }

        sp = getSharedPreferences("cl.selftourhamburger", Context.MODE_PRIVATE);

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);

        // Button click listeners
        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        //}

    }

    public void onClickLoginEnter(View view) {

        switch (view.getId()) {
            case cl.selftourhamburger.R.id.login_enter:

                TextView user = (TextView) findViewById(cl.selftourhamburger.R.id.login_user);
                TextView pass = (TextView) findViewById(cl.selftourhamburger.R.id.login_password);

                new LoginTask().execute(user.getText().toString(), pass.getText().toString(), "selftour");
                break;
        }
    }

    private class LoginTask extends AsyncTask<String, Void, UsuarioIngresado> {

        String logeado;

        @Override
        protected UsuarioIngresado doInBackground(String... params) {
            logeado = params[2].toString();

            UsuarioIngresado usuarioIngresado = restClient.login(params[0], params[1], params[2]);
            List<Recorrido> listRecorridos = restClient.getRecorridoDesdeServidor();
            setUsuarioIngrsadoABD(usuarioIngresado);
            setRecorridoABD(listRecorridos);

            if (usuarioIngresado.getCanLogin()) {

                nombreUsuarioRegistrado = params[0];

                sp.edit().putString("login_user", params[0]).apply();
                sp.edit().putString("login_pass", params[1]).apply();
                sp.edit().putBoolean("login", true).apply();
                return usuarioIngresado;
            } else {
                return usuarioIngresado;
            }

        }

        @Override
        protected void onPostExecute(UsuarioIngresado usuarioIngresado) {

            if (logeado.equalsIgnoreCase("selftour")) {
                if (!usuarioIngresado.getCanLogin()) {

                    AlertUtils.showErrorAlert(Activity_Login.this, "Error de Ingreso", "Credenciales Invalidas");
                } else {

                    Log.i("Activity_Login", "ActivityLogin OK...");

                    String destinoSeleccionado = getDestinoSelecionado(usuarioIngresado.getNombre());

                    if(destinoSeleccionado == null){
                        showRadioButtonDialog(Activity_Login.this);
                    }else{
                        new MaterialDialog.Builder(Activity_Login.this)
                                .title("Conectado!!")
                                .content("Dirigiendo a tu Destino")
                                .progress(true, 0)
                                .show();

                        Intent intent = new Intent(Activity_Login.this, Activity_Pantalla_Principal.class);
                        intent.putExtra("destinoSeleccionado",destinoSeleccionado);
                        startActivity(intent);
                    }
                }
            } else {
                if (!usuarioIngresado.getHaveLogon()) {

                    AlertUtils.showErrorAlert(Activity_Login.this, "Error de Ingreso", "Credenciales Invalidas");
                } else {
                    sp = getSharedPreferences("cl.selftourhamburger", Context.MODE_PRIVATE);
                    sp.edit().putString("login_user", email).apply();

                    String destinoSeleccionado = getDestinoSelecionado(email);

                    if(destinoSeleccionado == null){
                        showRadioButtonDialog(Activity_Login.this);
                    }else{
                        new MaterialDialog.Builder(Activity_Login.this)
                                .title("Conectado!!")
                                .content("Dirigiendo a tu Destino")
                                .progress(true, 0)
                                .show();

                        Intent intent = new Intent(Activity_Login.this, Activity_Pantalla_Principal.class);
                        intent.putExtra("destinoSeleccionado",destinoSeleccionado);
                        startActivity(intent);
                    }
                }
            }


        }
    }

    private void showRadioButtonDialog(final Activity_Login activity_login) {

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);
        List<String> listDestinos;

        listDestinos = getListDestinos();

        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for (int i = 0; i < listDestinos.size(); i++) {
            RadioButton rb = new RadioButton(this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(listDestinos.get(i));
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
                        activity_login.setTitle(btn.getText().toString());

                        guardarDestinoUsuario(btn.getText().toString());

                        new MaterialDialog.Builder(activity_login)
                                    .title("Conectado!!")
                                    .content("Dirigiendo a tu Destino")
                                    .progress(true, 0)
                                    .show();

                            Intent intent = new Intent(Activity_Login.this, Activity_Pantalla_Principal.class);
                            intent.putExtra("destinoSeleccionado",btn.getText().toString());
                            startActivity(intent);

                        //dialog.dismiss();
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

    private String getDestinoSelecionado(String nombre) {

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();
        String destino = null;
        String[] column = {"NOMBRE_DESTINO"};

        String where = "NOMBRE_USUARIO = "+"'"+nombre+"'";

        Cursor cursor = database.query("usuario_destino", column, where, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                destino = cursor.getString(cursor.getColumnIndex("NOMBRE_DESTINO"));
            }
        }

        return destino;
    }

    private void setUsuarioIngrsadoABD(UsuarioIngresado usuarioIngrsado) {

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();

        try {
            database.delete("login", null, null);
            ContentValues contentValues = new ContentValues();
            database.beginTransaction();

            contentValues.put("NOMBRE", usuarioIngrsado.getNombre());
            contentValues.put("APELLIDO", usuarioIngrsado.getApellido());
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

    /*METODOS DE LOGIN GOOGLE*/
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Method to resolve any signin errors
     */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        new MaterialDialog.Builder(this)
                .title("Conectado!!")
                .content("Dirigiendo a tu Destino")
                .progress(true, 0)
                .show();

        getProfileInformation();

        updateUI(true);

    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);

            new LoginTask().execute(email, "redSocial", "google");

        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                personName = currentPerson.getDisplayName();
                personPhotoUrl = currentPerson.getImage().getUrl();
                personGooglePlusProfile = currentPerson.getUrl();
                email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                nombreUsuarioRegistrado = email;

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                txtName.setText(personName);
                txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Button on click listener
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                // Signin button clicked
                signInWithGplus();
                break;
            case R.id.btn_sign_out:
                // Signout button clicked
                signOutFromGplus();
                break;
            case R.id.btn_revoke_access:
                // Revoke access button clicked
                revokeGplusAccess();
                break;
        }
    }

    /**
     * Sign-in into google
     */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Sign-out from google
     */
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateUI(false);
        }
    }

    /**
     * Revoking access from google
     */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(false);
                        }

                    });
        }
    }

    /**
     * Background Async task to load user profile picture from url
     */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    /*METODOS DE LOGIN GOOGLE*/

}
