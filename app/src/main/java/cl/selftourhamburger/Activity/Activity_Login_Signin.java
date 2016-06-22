package cl.selftourhamburger.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;

import cl.selftourhamburger.DataBase.DataBaseHelper;
import cl.selftourhamburger.R;
import cl.selftourhamburger.RestClient.RestClient;
import cl.selftourhamburger.model.pojo.Nacionalidad;

public class Activity_Login_Signin extends AppCompatActivity {

    CarouselView carouselView;
    GoogleApiClient mGoogleApiClient;
    private RestClient restClient;

    private static final int MY_WRITE_EXTERNAL_STORAGE = 0;
    private static final int MY_FINE_LOCATION = 0;
    private View mLayout;

    int[] sampleImages = {R.drawable.fondo1, R.drawable.fondo2, R.drawable.fondo3, R.drawable.fondo2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_activity_login_sign);

        verifyPermission();

        new guardarNacionalidadesEnDBTask().execute();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        mGoogleApiClient.connect();

        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            imageView.setImageResource(sampleImages[position]);
        }
    };

    public void onClickLogin(View view) {

        switch (view.getId()) {
            case R.id.entrar_login:

                if (mGoogleApiClient.isConnected()) {
                    Intent intent = new Intent(Activity_Login_Signin.this, Activity_Pantalla_Principal.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Activity_Login_Signin.this, Activity_Login.class);
                    startActivity(intent);
                }

                break;
        }

    }

    public void onClickRegistrarse(View view) {

        switch (view.getId()) {
            case R.id.entrar_registrarse:

                Intent intent = new Intent(Activity_Login_Signin.this, Activity_Registrarse.class);
                startActivity(intent);

                break;
        }
    }

    private class guardarNacionalidadesEnDBTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            restClient = new RestClient();
            List<Nacionalidad> listNacionalidades = restClient.getNacionalidad();

            guardarNacionalidadesEnBD(listNacionalidades);

            return null;
        }
    }

    private void guardarNacionalidadesEnBD(List<Nacionalidad> listNacionalidades) {

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();

        try {
            database.delete("nacionalidad", null, null);
            ContentValues contentValues = new ContentValues();
            database.beginTransaction();

            for (int i = 0; i < listNacionalidades.size(); i++) {
                contentValues.put("ID_NACIONALIDAD", listNacionalidades.get(i).getId());
                contentValues.put("NACIONALIDAD", listNacionalidades.get(i).getNacionalidad());
                database.insert("nacionalidad", null, contentValues);
            }

            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (SQLException e) {
            Log.e("Err guardarNacionalid ", e.getMessage());
        } finally {
            db.close();
            database.close();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void verifyPermission() {

        int writePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int findPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        if (writePermission != PackageManager.PERMISSION_GRANTED || findPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showSnackBar();
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            showSnackBar();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_WRITE_EXTERNAL_STORAGE);

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //saveComments();
            } else {
                showSnackBar();
            }
        }
        if (requestCode == MY_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //saveComments();
            } else {
                showSnackBar();
            }
        }
    }

    private void showSnackBar() {
        Snackbar.make(mLayout, R.string.permission_write_storage,
                Snackbar.LENGTH_LONG)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openSettings();
                    }
                })
                .show();
    }

    public void openSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
