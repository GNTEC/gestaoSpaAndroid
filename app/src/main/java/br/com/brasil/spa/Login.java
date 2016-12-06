package br.com.brasil.spa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abadari on 27/10/2016.
 */

public class Login extends AppCompatActivity implements Runnable{

    private EditText login_email;
    private EditText login_senha;
    private Button btn_login_entrar;
    private String login;
    private String senha;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private static Integer COD_EMPRESA = 58;
    private List<String> lstParamentros;
    private String resultado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        // instancia widgets
        cast();

        //permissoes marshmallow
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

           if (PermissionsMarshmallow.temPermissaoInternet(Login.this)) {

           } else {
                PermissionsMarshmallow.solicitaPermissaoInternet(Login.this);
           }

           if (PermissionsMarshmallow.temPermissaoNetworkState(Login.this)) {

           } else {
                PermissionsMarshmallow.solicitaPermissaoNetworkState(Login.this);
           }
        }

        btn_login_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setTitle("Aguarde...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                login = login_email.getText().toString().trim();
                senha = login_senha.getText().toString().trim();

                Thread thread = new Thread(Login.this);
                thread.start();

                /*Intent intent = new Intent(Login.this, MenuInicial.class);
                startActivity(intent);*/

            }
        });
    }

    //está no xml...
  /*  public void login(View v){
        Intent intent = new Intent(this, MenuInicial.class);
        startActivity(intent);
    }*/

    //commit

    public void cast(){

        login_email = (EditText) findViewById(R.id.login_email);
        login_senha = (EditText) findViewById(R.id.login_senha);
        btn_login_entrar = (Button) findViewById(R.id.btn_login_entrar);
    }


    @Override
    public void run() {

        String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/LOGIN_2";
        String OPERATION_NAME = "LOGIN_2";
        String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
        String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";

        /*String usuario= "claudio@dgm.com.br";
        String senha = "aaa@123";*/

        String usuario = login_email.getText().toString().trim();
        String senha = login_senha.getText().toString();

        //TODO- Pegar os dados dos textviews e passar na função

        try {

            WebService objWs = new WebService();

            objWs.setSOAP_ACTION(SOAP_ACTION);
            objWs.setOPERATION_NAME(OPERATION_NAME);
            objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
            objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

            resultado = objWs.login(COD_EMPRESA,usuario,senha);
            resultado= resultado.replace("\"", "");
            Log.e("LOGIN", resultado.toString());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(Login.this, resultado, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            });

        } catch (IOException e) {
            Log.e("MainActivity: ", e.toString());
            e.printStackTrace();

        } catch (XmlPullParserException ex){
            Log.e("MainAcvitity xml ex: " , ex.toString());
            ex.printStackTrace();
        }

        logou();
    }

    public void logou(){
        if(resultado.equals("OK")){
            Intent intent = new Intent(Login.this, MenuInicial.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this ,"Permissão ok" , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this ,"Permissão nescessaria" , Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
