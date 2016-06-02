package cl.selftourhamburger.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cl.selftourhamburger.RestClient.RestClient;
import cl.selftourhamburger.Util.AlertUtils;
import cl.selftourhamburger.model.pojo.UsuarioIngresado;

public class Activity_Login extends Activity {

    private RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cl.selftourhamburger.R.layout.activity_login);

    }

    public void onClick(View view){

        switch(view.getId()) {
            case cl.selftourhamburger.R.id.login_enter:

                TextView user = (TextView) findViewById(cl.selftourhamburger.R.id.login_user);
                TextView pass = (TextView) findViewById(cl.selftourhamburger.R.id.login_password);

                new LoginTask().execute(user.getText().toString(), pass.getText().toString());
                break;
        }
    }

    private class LoginTask extends AsyncTask<String, Void, UsuarioIngresado> {

        @Override
        protected UsuarioIngresado doInBackground(String... params) {
            UsuarioIngresado usuarioIngresado = restClient.login(params[0], params[1]);
            if(usuarioIngresado.getLoginCorrecto()){
                return usuarioIngresado;
            }else {
                return usuarioIngresado;
            }

        }

        @Override
        protected void onPostExecute(UsuarioIngresado usuarioIngresado) {
            if (!usuarioIngresado.getLoginCorrecto()) {
                //loader.cancel();
                AlertUtils.showErrorAlert(Activity_Login.this, "Error de Ingreso", "Credenciales Invalidas");
            }else {
                //login OK y token obtenido, guardamos everywhere.
                Log.i("Activity_Login", "ActivityLogin OK...");
                //loader.cancel();
                Intent intent = new Intent(Activity_Login.this, Activity_Pantalla_Principal.class);
                startActivity(intent);
            }
        }
    }



}
