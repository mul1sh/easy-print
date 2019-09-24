package com.mul1sh.easyprint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.res.ResourcesCompat;
import android.widget.TextView;

import com.mul1sh.easyprint.usersession.UserSession;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SplashActivity extends Activity {

    //to get user session data
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        session =new UserSession(SplashActivity.this);

      Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);

      TextView appname= findViewById(R.id.appname);
      appname.setTypeface(typeface);

        YoYo.with(Techniques.Bounce)
                .duration(7000)
                .playOn(findViewById(R.id.logo));

        YoYo.with(Techniques.FadeInUp)
                .duration(5000)
                .playOn(findViewById(R.id.appname));

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    startActivity(new Intent(SplashActivity.this,WelcomeActivity.class));
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
