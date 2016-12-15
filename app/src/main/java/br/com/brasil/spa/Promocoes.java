package br.com.brasil.spa;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import br.com.brasil.spa.Entidades.ProfissionaisHorario;
import br.com.brasil.spa.Utils.Eventos;
import br.com.brasil.spa.Utils.Sessao;

/**
 * Created by Anderson on 29/10/2016.
 */

public class Promocoes extends AppCompatActivity {

    private String resultado;
    private Integer filial;
    private Integer COD_EMPRESA = 58;
    private TextView txv_promo_2;
    private String promocao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promocoes);

        //EventBus.getDefault().register(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_promo);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("PROMOÇÕES");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        txv_promo_2 = (TextView) findViewById(R.id.txv_promo_2);
        filial = Sessao.getCodFilial();
        getPromocoes();

    }


    public void getPromocoes() {
        new Thread() {
            @Override
            public void run() {
                try {

                    String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/GET_PROMOCOES_2";
                    String OPERATION_NAME = "GET_PROMOCOES_2";
                    String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
                    String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";
                    //String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao_2.asmx";

                    WebService objWs = new WebService();
                    objWs.setSOAP_ACTION(SOAP_ACTION);
                    objWs.setOPERATION_NAME(OPERATION_NAME);
                    objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
                    objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

                    String resultadPromocoes = objWs.getPromocoes(COD_EMPRESA, filial);

                    //JSONObject obj = new JSONObject(resultadPromocoes);
                    promocao =resultadPromocoes.replaceAll("\"", "");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            txv_promo_2.setText(promocao);

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
