package br.com.brasil.spa;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Anderson on 29/10/2016.
 */

public class Promocoes extends AppCompatActivity {

    private String resultado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promocoes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_promo);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("PROMOÇÕES");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //CHAMA FUNÇÃO QUE CARREGA AS PROMOÇOES
        //getPromocoes();

    }


//    public void getPromocoes (){
//
//        String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/LOGIN_2";
//        String OPERATION_NAME = "LOGIN_2";
//        String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
//        String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";
//
//        try {
//
//            WebService objWs = new WebService();
//
//            objWs.setSOAP_ACTION(SOAP_ACTION);
//            objWs.setOPERATION_NAME(OPERATION_NAME);
//            objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
//            objWs.setSOAP_ADDRESS(SOAP_ADDRESS);
//
//            resultado = objWs.login(COD_EMPRESA,usuario,senha);
//            resultado= resultado.replace("\"", "");
//            Log.e("LOGIN", resultado.toString());
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(Login.this, resultado, Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//
//                }
//            });
//
//        } catch (IOException e) {
//            Log.e("MainActivity: ", e.toString());
//            e.printStackTrace();
//
//        } catch (XmlPullParserException ex){
//            Log.e("MainAcvitity xml ex: " , ex.toString());
//            ex.printStackTrace();
//        }
//
//        logou();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
