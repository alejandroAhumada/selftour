package cl.selftourhamburger.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cl.selftourhamburger.DataBase.DataBaseHelper;
import cl.selftourhamburger.R;
import cl.selftourhamburger.model.pojo.Puntos;

/**
 * A fragment that launches other parts of the demo application.
 */
public class FragmentExplora extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;
    private PolylineOptions polylineOptions;
    private List<Puntos> listPuntos;

    private SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_explora, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return false;
            }
        });

        sp = getActivity().getApplicationContext().getSharedPreferences("cl.selftourhamburger", Context.MODE_MULTI_PROCESS);
        String user = sp.getString("login_user","Vacio");
        String destinoSeleccionado = getDestinoSelecionado(user);


        listPuntos = getPuntos(destinoSeleccionado);

        createPolyline();

        return v;
    }

    private String getDestinoSelecionado(String nombre) {

        DataBaseHelper db = new DataBaseHelper(getContext());
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

    private List<Puntos> getPuntos(String nomDestio) {

        DataBaseHelper db = new DataBaseHelper(getContext());
        SQLiteDatabase database = db.getWritableDatabase();
        listPuntos = new ArrayList<>();
        String[] columns = {"NOMBRE_LUGAR",
                "DESCRIPCION_LUGAR",
                "LATITUD",
                "LONGITUD",
                "ID_MARCA",
                "NOMBRE_TIPO_MARCA"};

        //String where = "NOMBRE_DESTINO = \"" + nomDestio + "\"";
        Cursor cursor = database.query("destino_punto_interes", columns, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Puntos puntos = new Puntos();
                puntos.setNombreLugar(cursor.getString(cursor.getColumnIndex("NOMBRE_LUGAR")));
                puntos.setDescLugar(cursor.getString(cursor.getColumnIndex("DESCRIPCION_LUGAR")));
                puntos.setLatitud(cursor.getDouble(cursor.getColumnIndex("LATITUD")));
                puntos.setLongitud(cursor.getDouble(cursor.getColumnIndex("LONGITUD")));
                puntos.setIdMarca(cursor.getInt(cursor.getColumnIndex("ID_MARCA")));
                puntos.setNombreTmarca(cursor.getString(cursor.getColumnIndex("NOMBRE_TIPO_MARCA")));

                listPuntos.add(puntos);
            }
        }

        return listPuntos;
    }

    private void createPolyline() {

        polylineOptions = new PolylineOptions();

        for (int i = 0; i < listPuntos.size() ; i++) {
            LatLng latLng = new LatLng(listPuntos.get(i).getLatitud(), listPuntos.get(i).getLongitud());
            polylineOptions.add(latLng);
            polylineOptions.visible(true);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(listPuntos.get(i).getNombreLugar());
            //markerOptions.snippet(listPuntos.get(i).getDescLugar());
            int idMarca = listPuntos.get(i).getIdMarca();
            if (idMarca == 1) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_directions_walk_black_36dp));
            } else if (idMarca == 2) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_beenhere_black_36dp));
            }



            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(10).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            googleMap.addMarker(markerOptions);
            //mMap.addMarker(new MarkerOptions().position(latLng).title(listPuntos.get(i).getNombreLugar()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}