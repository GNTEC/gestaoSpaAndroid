package br.com.brasil.spa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by Anderson on 30/10/2016.
 */

public class LoadingScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_loading);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        loading();
    }

    // método para criar um loading falso

    public void loading(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LoadingScreen.this, Login.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
