package mhandharbeni.com.trackmenuandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

/**
 * Created by root on 11/06/17.
 */

public class MySplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View easySplashScreenView = new EasySplashScreen(MySplashActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(4000)
                .withBackgroundResource(R.color.colorAccent)
                .withBeforeLogoText("Burger Tahu")
                .withLogo(R.drawable.ic_home_black)
                .create();

        setContentView(easySplashScreenView);
    }
}
