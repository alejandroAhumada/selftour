package cl.selftourhamburger.Util;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import cl.selftourhamburger.R;
import cl.selftourhamburger.model.pojo.Puntos;

/**
 * Created by Alejandro on 22-09-2016.
 */
public class audiosDePruebaListener {

    public static MediaPlayer pruebaDeAudio(LatLng latLng, HashMap<LatLng,Puntos> hashPuntos, MediaPlayer mp){

        if(hashPuntos.containsKey(latLng)){

            if(!mp.isPlaying()){
                mp = hashPuntos.get(latLng).getMediaPlayer();
                mp.start();
            }

        }

        return mp;
    }

    /*public static void audiosDePrueba(Marker marker, Button play, Button stop, final Context context){

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
    }*/



}
