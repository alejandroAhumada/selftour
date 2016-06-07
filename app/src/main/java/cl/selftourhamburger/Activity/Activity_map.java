package cl.selftourhamburger.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import cl.selftourhamburger.R;

public class Activity_map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        mMap.moveCamera(CameraUpdateFactory.newLatLng(MetroVicenteValdez));

    }

    private void createPolyline(List<LatLng> listLatLng) {

        PolylineOptions polylineOptions = new PolylineOptions();

        for (int i = 0; i <listLatLng.size(); i++){
            polylineOptions.add(listLatLng.get(i));
        }

        mMap.addPolyline(polylineOptions);
    }


}
