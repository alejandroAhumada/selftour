package cl.selftourhamburger.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cl.selftourhamburger.R;
import cl.selftourhamburger.Util.UtilMap;
import cl.selftourhamburger.Util.audiosDePruebaListener;
import cl.selftourhamburger.model.pojo.Puntos;

//import com.google.android.gms.location.LocationListener;

public class Activity_map extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private String nomRecorrido;
    private HashMap<Integer, Puntos> listPuntos;
    protected Location mLastLocation;
    protected double mLatitude;
    protected double mLongitude;
    protected LocationRequest mLocationRequest;
    protected LocationManager manager;
    protected PolylineOptions polylineOptions;
    private Criteria req;
    private Button play;
    private Button stop;
    private HashMap<LatLng,Puntos> hashPuntos;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        play = (Button) this.findViewById(R.id.btnAudioPlay);
        stop = (Button) this.findViewById(R.id.btnAudioStop);
        play.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.INVISIBLE);
        mp = new MediaPlayer();
        req = new Criteria();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        this.nomRecorrido = bundle.getString("nomRecorrido");
        listPuntos = UtilMap.getPuntos(this.nomRecorrido, getApplicationContext());
        createHashIdLatLng();

        manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, this);
        //manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);

        saberSiEstaActivoElGPS();

        buildGoogleApiClient();
    }

    private void saberSiEstaActivoElGPS() {

        boolean isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGPSEnabled) {
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            Toast.makeText(getApplicationContext(), "Favor Habilitar GPS", Toast.LENGTH_SHORT).show();
            startActivity(gpsOptionsIntent);
        }
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
        mMap.setMyLocationEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        LatLng inicioRecorrido = new LatLng(listPuntos.get(1).getLatitud(), listPuntos.get(1).getLongitud());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicioRecorrido, 13f));

        createPolyline();

        mMap.addPolyline(polylineOptions);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
            @Override
            public void onInfoWindowClose(Marker marker) {
                play.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.INVISIBLE);
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                if(marker.isInfoWindowShown()){
                    play.setVisibility(View.VISIBLE);
                    stop.setVisibility(View.VISIBLE);

                    UtilMap.audiosDePrueba(marker, play, stop, getApplicationContext());
                }
            }
        });
    }

    private void createPolyline() {

        polylineOptions = new PolylineOptions();

        for (int i = 1; i < listPuntos.size() + 1; i++) {
            LatLng latLng = new LatLng(listPuntos.get(i).getLatitud(), listPuntos.get(i).getLongitud());
            polylineOptions.add(latLng);
            polylineOptions.visible(true);

            mMap.addMarker(UtilMap.createPolyLine(latLng, listPuntos, i, mMap));
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

    public void onClickModoLibre(View view) {

        switch (view.getId()) {
            case cl.selftourhamburger.R.id.btnModoLibre:
                onBackPressed();
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
            //startLocationUpdates();
        } else if (mLastLocation != null) {

            while (mLatitude == 0 || mLongitude == 0) {
                setLocation(mLastLocation);
                mLastLocation.setAccuracy(2000);

                mLatitude = mLastLocation.getLatitude();
                mLongitude = mLastLocation.getLongitude();

                if (mLatitude != 0 && mLongitude != 0) {

                    //stopLocationUpdates();
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
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
                mLocationRequest, (com.google.android.gms.location.LocationListener) this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    public void onLocationChanged(Location location) {

        System.out.println("LATITUD NUEVA: "+location.getLatitude());
        System.out.println("LONGITUD NUEVA: "+location.getLongitude());

        Location target = new Location("target");
        for(int i=1; i<listPuntos.size() +1; i++) {
            target.setLatitude(listPuntos.get(i).getLatitud());
            target.setLongitude(listPuntos.get(i).getLongitud());
                if(location.distanceTo(target) <= 20) {
                Toast.makeText(getApplicationContext(),"A 20 metros de "+listPuntos.get(i).getNombreLugar(),Toast.LENGTH_SHORT).show();
                    if(!mp.isPlaying()){
                        System.out.println("ENTRA A REPRODUCIR");;
                        mp = audiosDePruebaListener.pruebaDeAudio(new LatLng(target.getLatitude(), target.getLongitude()), hashPuntos, mp);
                    }
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.stop();
                        }
                    });

                }
        }



    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public LatLngBounds toBounds(LatLng center, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }

    private void createHashIdLatLng(){

        hashPuntos = new HashMap<>();

        for (int i=1; i<listPuntos.size() +1;i++){
            hashPuntos.put(new LatLng(listPuntos.get(i).getLatitud(),listPuntos.get(i).getLongitud()), listPuntos.get(i));
        }

    }


}
