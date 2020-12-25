package com.moadab.colornotesapp.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.moadab.colornotesapp.MainActivity;
import com.moadab.colornotesapp.R;

public class SplashScreenActivity extends AppCompatActivity {

    /* Initialize variables */
    Animation appNameAnimation;
    ImageView splashTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* Access to activity elements */
        splashTitle = findViewById(R.id.splash_title);

        /* set app Name Animation */
        appNameAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        splashTitle.setAnimation(appNameAnimation);

        /* Method Time for SplashScreen Activity */
        Thread splashTime=new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(4500);
                } catch (Exception e) {
                }
                finally {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        /* Start MainActivity if User was Login in FireBase */
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        finish();

                    }
                    else {
                        /* Start LoginActivity if User didn't Login in FireBase */
                        startActivity(new Intent(SplashScreenActivity.this, OnBoardingActivity.class));
                        finish();
                    }
                }
            }
        };
        splashTime.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}