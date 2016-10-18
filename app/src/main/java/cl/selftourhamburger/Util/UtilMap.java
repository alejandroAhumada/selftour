package cl.selftourhamburger.Util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cl.selftourhamburger.DataBase.DataBaseHelper;
import cl.selftourhamburger.R;
import cl.selftourhamburger.model.pojo.Puntos;

/**
 * Created by Alejandro on 21-09-2016.
 */
public class UtilMap {

    public static void saberSiEstaActivoElGPS(LocationManager manager, Context context) {

        boolean isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGPSEnabled) {
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            Toast.makeText(context, "Favor Habilitar GPS", Toast.LENGTH_SHORT).show();
            context.startActivity(gpsOptionsIntent);
        }
    }

    public static void audiosDePrueba(Marker marker, Button play, Button stop, final Context context){

        if(marker.getTitle().equalsIgnoreCase("Plaza Italia")){

            final MediaPlayer mp = MediaPlayer.create(context, R.raw.uno);
            play.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.create(context, R.raw.uno);
                    mp.start();
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.pause();
                }
            });

        }else if(marker.getTitle().equalsIgnoreCase("Parque forestal- Fuente alemana")){

            final MediaPlayer mp = MediaPlayer.create(context, R.raw.dos);
            play.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.create(context, R.raw.dos);
                    mp.start();
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.pause();
                }
            });

        }else if(marker.getTitle().equalsIgnoreCase("Museo de Bellas Artes")){

            final MediaPlayer mp = MediaPlayer.create(context, R.raw.tres);
            play.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.create(context, R.raw.tres);
                    mp.start();
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.pause();
                }
            });

        }else if(marker.getTitle().equalsIgnoreCase("Barrio Patronato")){

            final MediaPlayer mp = MediaPlayer.create(context, R.raw.cuatro);
            play.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.create(context, R.raw.cuatro);
                    mp.start();
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.pause();
                }
            });

        }else if(marker.getTitle().equalsIgnoreCase("La Vega")){

            final MediaPlayer mp = MediaPlayer.create(context, R.raw.cinco);
            play.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.create(context, R.raw.cinco);
                    mp.start();
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.pause();
                }
            });

        }else if(marker.getTitle().equalsIgnoreCase("Mercado central")){

            final MediaPlayer mp = MediaPlayer.create(context, R.raw.seis);
            play.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.create(context, R.raw.seis);
                    mp.start();
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.pause();
                }
            });

        }else if(marker.getTitle().equalsIgnoreCase("La Piojera")){

            final MediaPlayer mp = MediaPlayer.create(context, R.raw.siete);
            play.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.create(context, R.raw.siete);
                    mp.start();
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.pause();
                }
            });

        }else if(marker.getTitle().equalsIgnoreCase("Centro Cultural Estación Mapocho")){

            final MediaPlayer mp = MediaPlayer.create(context, R.raw.ocho);
            play.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.create(context, R.raw.ocho);
                    mp.start();
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mp.pause();
                }
            });

        }
    }

    public static MarkerOptions createPolyLine(LatLng latLng, HashMap<Integer,Puntos> listPuntos, int i, GoogleMap mMap){

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(listPuntos.get(i).getNombreLugar());
        markerOptions.snippet(listPuntos.get(i).getDescLugar());
        int idMarca = listPuntos.get(i).getIdMarca();
        if (idMarca == 1) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_directions_walk_black_36dp));
        } else if (idMarca == 2) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_beenhere_black_36dp));
        }else if (idMarca == 3) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.atractivo_24dpi));
        }else if (idMarca == 4) {
            markerOptions.visible(false);
        }else if (idMarca == 5) {
            markerOptions.visible(false);
        }else if (idMarca == 6) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_restaurant_menu_black_36dp));
        }else if (idMarca == 7) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_account_balance_black_36dp));
        }else if (idMarca == 8) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_perm_identity_black_36dp));
        }else if (idMarca == 9) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_airline_seat_individual_suite_black_36dp));
        }else if (idMarca == 10) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_city_black_36dp));
        }else if (idMarca == 11) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_flare_black_36dp));
        }else if (idMarca == 12) {
            //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_beenhere_black_36dp));
        }
        return markerOptions;
    }

    public static HashMap<Integer, Puntos> getPuntos(String nomRecorrido, Context context) {
        List<MediaPlayer> mediaPlayers = listaAudios(context);
        int i = 0;
        DataBaseHelper db = new DataBaseHelper(context);
        SQLiteDatabase database = db.getWritableDatabase();
        HashMap<Integer,Puntos> listPuntos = new HashMap<>();
        String[] columns = {"POSICION",
                "NOMBRE_LUGAR",
                "DESCRIPCION_LUGAR",
                "LATITUD",
                "LONGITUD",
                "ID_MARCA",
                "NOMBRE_TIPO_MARCA"};

        String where = "NOMBRE_RECORRIDO = \"" + nomRecorrido + "\"";
        Cursor cursor = database.query("puntos_de_recorridos", columns, where, null, null, null, "POSICION");

        if(nomRecorrido.equalsIgnoreCase("Santiago Historico")){
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
                    puntos.setMediaPlayer(mediaPlayers.get(i));
                    listPuntos.put(puntos.getPos(), puntos);
                    i++;
                }
            }
        }else {
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
        }

        return listPuntos;
    }

    private static List<MediaPlayer> listaAudios(Context context){

        List<MediaPlayer> lisAudios = new ArrayList<>();

        lisAudios.add(MediaPlayer.create(context ,R.raw.uno));
        lisAudios.add(MediaPlayer.create(context ,R.raw.dos));
        lisAudios.add(MediaPlayer.create(context ,R.raw.tres));
        lisAudios.add(MediaPlayer.create(context ,R.raw.cuatro));
        lisAudios.add(MediaPlayer.create(context ,R.raw.cinco));
        lisAudios.add(MediaPlayer.create(context ,R.raw.seis));
        lisAudios.add(MediaPlayer.create(context ,R.raw.siete));
        lisAudios.add(MediaPlayer.create(context ,R.raw.ocho));

        return lisAudios;
    }

}
