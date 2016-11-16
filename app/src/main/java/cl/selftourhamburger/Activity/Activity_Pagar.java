package cl.selftourhamburger.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cl.selftourhamburger.R;

/**
 * Created by Alejandro on 16-11-2016.
 */

public class Activity_Pagar extends Activity{

    EditText pagoNombre;
    EditText pagoMail;
    EditText pagoTarjetaCredito;

    EditText pagoMes;
    EditText pagoAño;
    EditText pagoCvv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pago);

        findViewsById();

    }

    private void findViewsById() {
        pagoNombre = (EditText) findViewById(R.id.pagoNombre);
        pagoMail = (EditText) findViewById(R.id.pagoMail);
        pagoTarjetaCredito = (EditText) findViewById(R.id.pagoNumTarjeta);
        pagoMes = (EditText) findViewById(R.id.pagoMes);
        pagoAño = (EditText) findViewById(R.id.pagoAño);
        pagoCvv = (EditText) findViewById(R.id.pagoCvv);
    }

    public void onClickPagoCancelar(View view) {
        switch (view.getId()) {
            case R.id.btnPagoCancelar:
                onBackPressed();
                break;
        }

    }

    public void onClickPagoPagar(View view){
        StringBuilder vacio = new StringBuilder();
        switch (view.getId()){
            case R.id.btnPagoPagar:

                String LpagoNombre = pagoNombre.getText().toString();
                String LpagoMail = pagoMail.getText().toString();
                String LpagoTarjetaCredito = pagoTarjetaCredito.getText().toString();
                String LpagoMes = pagoMes.getText().toString();
                String LpagoAño = pagoAño.getText().toString();
                String LpagoCvv = pagoCvv.getText().toString();

                if(LpagoNombre.isEmpty()){
                    vacio.append("-Nombre ");
                }if(LpagoMail.isEmpty()) {
                vacio.append("-Mail");
                }if(LpagoTarjetaCredito.isEmpty()) {
                vacio.append("-Tarjeta de Credito");
                }if(LpagoMes.isEmpty()) {
                    vacio.append("-Username");
                }if(LpagoAño.isEmpty()) {
                    vacio.append("-Password");
                }if(LpagoCvv.isEmpty()) {
                    vacio.append("-Fecha de Nacimiento");
                }

                boolean mailValid = isValidEmail(LpagoMail);

                if(vacio.toString().length() <= 0) {
                    if (mailValid) {
                        Toast.makeText(this,"Pago realizado con Exito",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(getApplicationContext(), "Mail incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Favor Completar Todos Los Campos", Toast.LENGTH_SHORT).show();
                }

            break;
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
