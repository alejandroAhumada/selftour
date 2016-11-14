package cl.selftourhamburger.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Alejandro on 14-11-2016.
 */
public class Activity_Cambio_Destino extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);

    }

    public void cambioDestino(){

        String destinoSeleccionado = getIntent().getStringExtra("destinoSeleccionado");

        Intent intent = new Intent(Activity_Cambio_Destino.this, Activity_Pantalla_Principal.class);
        intent.putExtra("destinoSeleccionado", destinoSeleccionado);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cambioDestino();
    }
}
