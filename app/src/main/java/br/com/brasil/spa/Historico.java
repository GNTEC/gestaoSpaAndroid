package br.com.brasil.spa;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.brasil.spa.Adapters.AgendamentoAdapter;
import br.com.brasil.spa.Entidades.Agendamento;
import br.com.brasil.spa.Entidades.Cliente;
import br.com.brasil.spa.Entidades.Profissionais;
import br.com.brasil.spa.Entidades.Servicos;
import br.com.brasil.spa.Utils.Sessao;

/**
 * Created by Anderson on 30/10/2016.
 */

public class Historico extends AppCompatActivity {

    private Integer COD_EMPRESA = 58;
    private List<Agendamento> lstAgendamento;
    private RecyclerView mRecyclerView;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_agendamento);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_resultado);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Historico");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);

        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        getAgendamento();

        /*mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                AgendamentoAdapter agendamentoAdapter = (AgendamentoAdapter) mRecyclerView.getAdapter();

            }
        });*/

    }

    public void onClickNovoAgendamento(View v){

        Intent intent = new Intent(this, MenuInicial.class);
        startActivity(intent);

        //Toast.makeText(this, "Realizar outro agendameto", Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }*/


    public void getAgendamento() {

        new Thread() {
            @Override
            public void run() {
                try {

                    //constantes webservice
                    String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/GET_AGENDAMENTOS_2";
                    String OPERATION_NAME = "GET_AGENDAMENTOS_2";
                    String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
                    String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";

                    //configuração da request
                    WebService objWs = new WebService();
                    objWs.setSOAP_ACTION(SOAP_ACTION);
                    objWs.setOPERATION_NAME(OPERATION_NAME);
                    objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
                    objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

                    //pega as constantes da Sessao
                    Integer cod_filial = Sessao.getCodFilial();
                    Integer cod_cliente = Sessao.getCodCliente();

                    //resultado da soap
                    String resultadoAgendamentos = objWs.getAgendamentos(COD_EMPRESA, cod_filial, cod_cliente);
                    //pega o array devolvido
                    JSONArray arrAgendamento = new JSONArray(resultadoAgendamentos);
                    //listagem que vai receber tudo
                    lstAgendamento = new ArrayList<Agendamento>();

                    for(int i = 0; i < arrAgendamento.length(); i++){

                        //instancia a classe agendamento
                        Agendamento agendamento = new Agendamento();

                        //converte para jsonObject
                        JSONObject obj = arrAgendamento.getJSONObject(i);

                        //seta os dados locais na classe agendamento
                        agendamento.setCOD_AGENDAMENTO(obj.getInt("COD_AGENDAMENTO"));
                        agendamento.setDATA(obj.getString("DATA"));
                        agendamento.setHORA(obj.getString("HORA"));
                        agendamento.setSTATUS(obj.getString("STATUS"));

                        //manda o objeto preenchido
                        JSONObject objP = obj.getJSONObject("PROFISSIONAL");
                        agendamento.setPROFISSIONAIS(new Profissionais(objP.getInt("COD_PROFISSIONAL"),
                                    objP.getString("NOME")));

                        //manda o objeto preenchido
                        JSONObject objS = obj.getJSONObject("SERVICO");
                        agendamento.setSERVICOS(new Servicos(objS.getInt("COD_SERVICO"),
                                objS.getString("DSC_SERVICO"), objS.getDouble("VALOR")));

                        //manda o objeto preenchido
                        JSONObject objC = obj.getJSONObject("CLIENTE");
                        agendamento.setCLIENTE(new Cliente(objC.getInt("COD_CLIENTE"),
                                objC.getString("NOME"), objC.getString("MSG_RETORNO")));

                        //adição na listagem do objeto completo
                        lstAgendamento.add(agendamento);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.setHasFixedSize(true);
                                LinearLayoutManager llm = new LinearLayoutManager(Historico.this);
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                mRecyclerView.setLayoutManager(llm);
                                AgendamentoAdapter agendamentoAdapter = new AgendamentoAdapter(Historico.this, lstAgendamento);
                                //atualiza o adapter
                                agendamentoAdapter.notifyDataSetChanged();
                                //seta o adapter
                                mRecyclerView.setAdapter(agendamentoAdapter);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
