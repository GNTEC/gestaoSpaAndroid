package br.com.brasil.spa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Anderson on 30/10/2016.
 */

public class ResultadoAgendamento extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_agendamento);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_resultado);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Resultado do Agendamento");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void onClickNovoAgendamento(View v){
        Intent intent = new Intent(this, MenuInicial.class);
        startActivity(intent);

        //Toast.makeText(this, "Realizar outro agendameto", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
