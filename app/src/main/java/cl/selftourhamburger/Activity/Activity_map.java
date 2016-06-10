package cl.selftourhamburger.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cl.selftourhamburger.DataBase.DataBaseHelper;
import cl.selftourhamburger.R;
import cl.selftourhamburger.model.pojo.Puntos;

public class Activity_map extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private String nomRecorrido;
    private HashMap<Integer,Puntos> listPuntos;
    protected Location mLastLocation;
    protected double mLatitude;
    protected double mLongitude;
    protected LocationRequest mLocationRequest;
    protected LocationManager manager;
    protected PolylineOptions polylineOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        this.nomRecorrido = bundle.getString("nomRecorrido");
        listPuntos = getPuntos(this.nomRecorrido);

        manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        buildGoogleApiClient();
    }

     protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        mMap.setMyLocationEnabled(true);

        LatLng inicioRecorrido = new LatLng(listPuntos.get(1).getLatitud(), listPuntos.get(1).getLongitud());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicioRecorrido, 13f));

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                mMapClear();
                createPolyline();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Aqui Estoy!"));
                polylineOptions.add(latLng);
                polylineOptions.color(R.color.colorLogoSelfTour);
                mMap.addPolyline(polylineOptions);

            }
        });
    }

    private void mMapClear() {
        mMap.clear();
    }

    private void createPolyline() {

        polylineOptions = new PolylineOptions();

        for(int i =  1; i<listPuntos.size()+1; i++){
            LatLng latLng = new LatLng(listPuntos.get(i).getLatitud(), listPuntos.get(i).getLongitud());
            polylineOptions.add(latLng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(listPuntos.get(i).getNombreLugar());
            markerOptions.snippet(listPuntos.get(i).getDescLugar());
            int idMarca = listPuntos.get(i).getIdMarca();
            if(idMarca == 1){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_directions_walk_black_36dp));
            }else if(idMarca == 2){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_beenhere_black_36dp));
            }
            mMap.addMarker(markerOptions);
           //mMap.addMarker(new MarkerOptions().position(latLng).title(listPuntos.get(i).getNombreLugar()));
        }
    }

    public void setLocation(Location loc) {

        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<android.location.Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    android.location.Address address = list.get(0);
                    Toast.makeText(getApplicationContext(), "Estoy en: \n" + address.getAddressLine(0), Toast.LENGTH_SHORT).show();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.d("ACTIVITY", "ApiClient: OnConnected");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation == null) {
            startLocationUpdates();
        }

        if (mLastLocation != null) {
            while (mLatitude == 0 || mLongitude == 0) {
                Toast.makeText(getApplicationContext(), "Getting Location", Toast.LENGTH_SHORT).show();

                setLocation(mLastLocation);

                mLatitude = mLastLocation.getLatitude();
                mLongitude = mLastLocation.getLongitude();

                if (mLatitude != 0 && mLongitude != 0) {
                    stopLocationUpdates();
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("TAG", "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("TAG", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {


    }

    private HashMap<Integer, Puntos> getPuntos(String nomRecorrido) {

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();
        listPuntos = new HashMap<>();
        String[] columns = {"POSICION",
                "NOMBRE_LUGAR",
                "DESCRIPCION_LUGAR",
                "LATITUD",
                "LONGITUD",
                "ID_MARCA",
                "NOMBRE_TIPO_MARCA"};

        String where = "NOMBRE_RECORRIDO = \""+nomRecorrido+"\"";
        Cursor cursor = database.query("puntos_de_recorridos", columns, where, null,null, null, "POSICION");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Puntos puntos = new Puntos();
                puntos.setPos(cursor.getInt(cursor.getColumnIndex("POSICION")));
                puntos.setNombreLugar(cursor.getString(cursor.getColumnIndex("NOMBRE_LUGAR")));
                puntos.setDescLugar(cursor.getString(cursor.getColumnIndex("DESCRIPCION_LUGAR")));
                puntos.setLatitud(cursor.getDouble(cursor.getColumnIndex("LATITUD")));
                puntos.setLongitud(cursor.getDouble(cursor.getColumnIndex("LONGITUD")));
                puntos.setIdMarca(cursor.getInt(cursor.getColumnIndex("ID_MARCA")));
                puntos.setNombreTmarca(cursor.getString(cursor.getColumnIndex("NOMBRE_TIPO_MARCA")));
                listPuntos.put(puntos.getPos(), puntos);
            }
        }

        return listPuntos;
    }




}
