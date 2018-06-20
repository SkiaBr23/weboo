package unb.com.br.booksblank.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import unb.com.br.booksblank.R;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        int SPLASH_SCREEN_TIMEOUT = 3000;
        new Handler().postDelayed(new Runnable() {
            //Exibicao da splash screen durante o tempo predefinido
            @Override
            public void run() {
                    Intent mainIntent = new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.fade_in_one,R.anim.fade_out);
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
}
