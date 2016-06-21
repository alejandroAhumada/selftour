package cl.selftourhamburger.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import cl.selftourhamburger.R;

public class Activity_Login_Signin extends AppCompatActivity {

    CarouselView carouselView;
    GoogleApiClient mGoogleApiClient;

    int[] sampleImages = {R.drawable.fondo1, R.drawable.fondo2, R.drawable.fondo3, R.drawable.fondo2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_activity_login_sign);

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        mGoogleApiClient.connect();

        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            imageView.setImageResource(sampleImages[position]);
        }
    };

    public void onClickLogin(View view) {

        switch (view.getId()) {
            case R.id.entrar_login:

                if (mGoogleApiClient.isConnected()) {
                    Intent intent = new Intent(Activity_Login_Signin.this, Activity_Pantalla_Principal.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Activity_Login_Signin.this, Activity_Login.class);
                    startActivity(intent);
                }

                break;
        }

    }

    public void onClickRegistrarse(View view) {

        switch (view.getId()) {
            case R.id.entrar_registrarse:

                Intent intent = new Intent(Activity_Login_Signin.this, Activity_Registrarse.class);
                startActivity(intent);

                break;
        }
    }

    private void signInFromGplus() {

    }

}
