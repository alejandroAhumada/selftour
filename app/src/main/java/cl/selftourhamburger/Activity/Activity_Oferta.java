package cl.selftourhamburger.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cl.selftourhamburger.R;
import cl.selftourhamburger.Util.UtilOferta;

/**
 * Created by Alejandro on 15-11-2016.
 */

public class Activity_Oferta extends Activity{

    ImageView imageView;
    TextView textView;
    Button btnComprar;
    Button btnCancelar;

    String punto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta);

        punto = getIntent().getStringExtra("punto");

        findViewsById();
        setData();

    }

    private void findViewsById() {
        imageView = (ImageView) findViewById(R.id.imagenOferta);
        textView = (TextView) findViewById(R.id.descOferta);
        btnComprar = (Button) findViewById(R.id.btnComprar);
        btnCancelar = (Button) findViewById(R.id.btnCancelarCompra);

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Oferta.this, Activity_Pagar.class);
                startActivity(intent);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setData() {

        setImageView();
        setTextoOferta();

    }

    private void setImageView(){
        try {
            String puntoSinEspacio = punto.replace("-","").replace("ó","o").replace(" ", "").toLowerCase();
            String uri = "@drawable/" + puntoSinEspacio;

            int imageResource = getResources().getIdentifier(uri, null, getPackageName());

            Drawable res = getResources().getDrawable(imageResource);
            imageView.setImageDrawable(res);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setTextoOferta() {
        textView.setText(UtilOferta.getOferta(punto));
    }

}
