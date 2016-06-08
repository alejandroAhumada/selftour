package cl.selftourhamburger.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import cl.selftourhamburger.R;

public class Activity_Login_Signin extends AppCompatActivity {

    CarouselView carouselView;

    int[] sampleImages = {R.drawable.cajon1, R.drawable.cajon2, R.drawable.cajon3, R.drawable.cajon4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_activity_login_signin);

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

    public void onClickLogin(View view){

        switch(view.getId()) {
            case R.id.entrar_login:

                //Intent intent = new Intent(Activity_Login_Signin.this, Activity_Login.class);
                Intent intent = new Intent(Activity_Login_Signin.this, Activity_map.class);
                startActivity(intent);

                break;
        }

    }

    public void onClickRegistrarse(View view){

        switch(view.getId()) {
            case R.id.entrar_registrarse:

                Intent intent = new Intent(Activity_Login_Signin.this, Activity_Registrarse.class);
                startActivity(intent);

                break;
        }


    }


}
