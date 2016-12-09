package br.com.brasil.spa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.brasil.spa.Utils.Sessao;

/**
 * Created by Anderson on 30/10/2016.
 */

public class ResultadoAgendamento extends AppCompatActivity {

    private Integer COD_EMPRESA = 58;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_agendamento);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_resultado);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Resultado do Agendamento");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getAgendamento();

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


    public void getAgendamento() {
        new Thread() {
            @Override
            public void run() {
                try {

                    String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/GET_AGENDAMENTOS_2";
                    String OPERATION_NAME = "GET_AGENDAMENTOS_2";
                    String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
                    String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";
                    //String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao_2.asmx";

                    WebService objWs = new WebService();
                    objWs.setSOAP_ACTION(SOAP_ACTION);
                    objWs.setOPERATION_NAME(OPERATION_NAME);
                    objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
                    objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

                    Integer cod_filial = Sessao.getCodFilial();
                    Integer cod_cliente = Sessao.getCodCliente();

                    String resultadoAgendamentos = objWs.getAgendamentos(COD_EMPRESA, cod_filial, cod_cliente);
                    //Log.e("Resultado Agendamentos: ", resultadoAgendamentos);

                    JSONObject obj = new JSONObject(resultadoAgendamentos);
                    JSONArray arrFiliais = obj.getJSONArray("FILIAL");

                   /* if (obj.length() != 0) {
                        lstUnidades = new ArrayList<Unidade>();
                        auxLstUnidades = new ArrayList<Filial>();
                        for (int i = 0; i < obj.length(); i++) {

                            lstUnidades.add(new Unidade(obj.getInt("COD_EMPRESA"),
                                    obj.getString("NOME_EMPRESA")));

                            for (int g = 0; g < arrFiliais.length(); g++) {

                                JSONObject jsonResposta = arrFiliais.getJSONObject(g);

                                auxLstUnidades.add(new Filial(jsonResposta.getInt("COD_EMPRESA_FILIAL"),
                                        jsonResposta.getInt("COD_FILIAL"),
                                        jsonResposta.getString("NOME_FILIAL"),
                                        jsonResposta.getString("ENDERECO")));

                            }
                        }
                    }

                    lstSpnUnidade = new ArrayList<String>();
                    lstSpnUnidade.add("Selecione");

                    for (int j = 0; j < auxLstUnidades.size(); j++) {
                        lstSpnUnidade.add(auxLstUnidades.get(j).getNOME_FILIAL());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            preencheSpinnerUnidade();
                            COD_FILIAL = auxLstUnidades.get(posicao).getCOD_FILIAL();
                            spnU.setSelection(posicao);
                            getServicos(COD_FILIAL);
                        }
                    });*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
