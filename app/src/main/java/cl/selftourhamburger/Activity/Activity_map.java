package cl.selftourhamburger.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.location.LocationListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cl.selftourhamburger.R;

public class Activity_map extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    protected double mLatitude;
    protected double mLongitude;
    protected LocationRequest mLocationRequest;
    protected LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<LatLng> listLatLng = new ArrayList<>();

        LatLng MetroVicenteValdez = new LatLng(-33.526336, -70.596760);
        LatLng MetroBellavistaLaFlorida = new LatLng(-33.519823, -70.599650);
        LatLng MetroMirador = new LatLng(-33.512940, -70.606583);

        listLatLng.add(MetroVicenteValdez);
        listLatLng.add(MetroBellavistaLaFlorida);
        listLatLng.add(MetroMirador);

        mMap.addMarker(new MarkerOptions().position(MetroVicenteValdez).title("Metro Vicente Valdez"));
        mMap.addMarker(new MarkerOptions().position(MetroBellavistaLaFlorida).title("Metro Bellavista La Florida"));
        mMap.addMarker(new MarkerOptions().position(MetroMirador).title("Metro Mirador"));

        createPolyline(listLatLng);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MetroVicenteValdez, 15f));

    }

    private void createPolyline(List<LatLng> listLatLng) {

        PolylineOptions polylineOptions = new PolylineOptions();

        for (int i = 0; i < listLatLng.size(); i++) {
            polylineOptions.add(listLatLng.get(i));
        }

        mMap.addPolyline(polylineOptions);
    }


    //SE DEJA SOLO POR USO DE EJEMPLO
    public void setLocation(Location loc) {
        //Obtener la direcci—n de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<android.location.Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    android.location.Address address = list.get(0);
                    Toast.makeText(getApplicationContext(), "Mi direcci—n es: \n" + address.getAddressLine(0), Toast.LENGTH_SHORT).show();
                    //messageTextView2.setText("Mi direcci—n es: \n" + address.getAddressLine(0));
                    System.out.println("Mi direcci—n es: \n" + address.getAddressLine(0));
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
            startLocationUpdates(); // bind interface if your are not getting the lastlocation. or bind as per your requirement.
        }

        if (mLastLocation != null) {
            while (mLatitude == 0 || mLongitude == 0) {
                Toast.makeText(getApplicationContext(), "Getting Location", Toast.LENGTH_SHORT).show();


                mLatitude = mLastLocation.getLatitude();
                mLongitude = mLastLocation.getLongitude();

                if (mLatitude != 0 && mLongitude != 0) {
                    stopLocationUpdates(); // unbind the locationlistner here or wherever you want as per your requirement.
                    Toast.makeText(getApplicationContext(), "Listo", Toast.LENGTH_SHORT).show();
                }
            }
        }

        LatLng Actual = new LatLng(mLatitude, mLongitude);
        mMap.addMarker(new MarkerOptions().position(Actual).title("ACTUAL"));
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
        } else {
            // Showyourmesg();
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
        Log.d("SplashAct", "LocatinChngListner, loc: " + location.getLatitude() + "," + location.getLongitude());

        if (mGoogleApiClient != null)
            if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()){
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
            } else if (!mGoogleApiClient.isConnected()){
                mGoogleApiClient.connect();
            }
    }

}
