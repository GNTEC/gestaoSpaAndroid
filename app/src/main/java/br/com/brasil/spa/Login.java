package br.com.brasil.spa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.brasil.spa.Utils.Eventos;
import br.com.brasil.spa.Utils.Sessao;

import static com.loopj.android.http.AsyncHttpClient.log;

public class Login extends AppCompatActivity implements Runnable, CompoundButton.OnCheckedChangeListener {

    private EditText login_email;
    private EditText login_senha;
    private Button btn_login_entrar;
    private String login;
    private String senha;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private static Integer COD_EMPRESA;
    private List<String> lstParamentros;
    private String resultado;

    //dados login
    private Integer COD_CLIENTE;
    private String NOME;
    private String MSG_RETORNO;

    //SharedPreferences
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox cb_remember;
    private Boolean checkFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // instancia widgets
        cast();

        COD_EMPRESA = Sessao.COD_EMPRESA;

        //instancia o arquivo que salva as preferencias
        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        editor = pref.edit();

        //recabe os valores da SharedPreferences
        String user = pref.getString("username", "");
        String pass = pref.getString("password", "");

        //seta login e senha se tiverem sidos salvos
        if(!user.equals("") && !pass.equals("")){
            login_email.setText(user);
            login_senha.setText(pass);
        }

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

        checkFlag = cb_remember.isChecked();
        cb_remember.setOnCheckedChangeListener(this);

        btn_login_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkFlag == false){
                    editor.putString("username", "");
                    editor.putString("password", "");
                    editor.apply();
                }

                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setTitle("Aguarde...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                login = login_email.getText().toString().trim();
                senha = login_senha.getText().toString().trim();

                Thread thread = new Thread(Login.this);
                thread.start();

            }
        });
    }


    public void cast(){

        cb_remember = (CheckBox) findViewById(R.id.cb_remember);
        login_email = (EditText) findViewById(R.id.login_email);
        login_senha = (EditText) findViewById(R.id.login_senha);
        btn_login_entrar = (Button) findViewById(R.id.btn_login_entrar);
    }


    @Override
    public void run() {

        String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/LOGIN_3";
        String OPERATION_NAME = "LOGIN_3";
        String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
        String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";
        String usuario = login_email.getText().toString().trim();
        String senha = login_senha.getText().toString();

        try {

            WebService objWs = new WebService();

            objWs.setSOAP_ACTION(SOAP_ACTION);
            objWs.setOPERATION_NAME(OPERATION_NAME);
            objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
            objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

            resultado = objWs.login(COD_EMPRESA,usuario,senha);
            JSONObject obj = new JSONObject(resultado);
            COD_CLIENTE = obj.getInt("COD_CLIENTE");
            NOME = obj.getString("NOME");
            MSG_RETORNO = obj.getString("MSG_RETORNO");

            //Passa os dados do cliente para realizar agendamento
            Sessao.setCodCliente(COD_CLIENTE);
            Sessao.setNomeCliente(NOME);

            Log.e("LOGIN", resultado.toString());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    logou();
                }
            });

        } catch (IOException e) {
            Log.e("MainActivity: ", e.toString());
            e.printStackTrace();

        } catch (XmlPullParserException ex){
            Log.e("MainAcvitity xml ex: " , ex.toString());
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void logou(){

        if(MSG_RETORNO.equals("Senha incorreta")){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Senha incorreta");
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }

        if(MSG_RETORNO.equals("Login não cadastrado")){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Login não cadastrado");
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }

        if(MSG_RETORNO.equals("OK")){

            //se estiver checado para salvaer usuario e senha salva na SharedPreferences
            if(checkFlag){
                editor.putString("username", login_email.getText().toString());
                editor.putString("password", login_senha.getText().toString());
                editor.apply();
            }

            Sessao.setEMAIL(login_email.getText().toString());
            Intent intent = new Intent(Login.this, SelecaoUnidade.class);
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        //recebe o status do lembrar usuario e senha
        checkFlag = b;
    }
}
