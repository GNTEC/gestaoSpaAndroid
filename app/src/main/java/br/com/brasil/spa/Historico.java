package br.com.brasil.spa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import br.com.brasil.spa.Adapters.AgendamentoAdapter;
import br.com.brasil.spa.Entidades.Agendamento;
import br.com.brasil.spa.Entidades.Cliente;
import br.com.brasil.spa.Entidades.Profissionais;
import br.com.brasil.spa.Entidades.Servicos;
import br.com.brasil.spa.Utils.Eventos;
import br.com.brasil.spa.Utils.Sessao;

/**
 * Created by Anderson on 30/10/2016.
 */

public class Historico extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Integer COD_EMPRESA;
    private List<Agendamento> lstAgendamento;
    private RecyclerView mRecyclerView;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private ProgressDialog progressDialog;

    //header
    private NavigationView navigationView;
    private TextView txv_header_nome;
    private TextView txv_header_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        EventBus.getDefault().register(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_resultado);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Historico");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);

        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);

        }*/
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.mobile_nav);
        upArrow.setColorFilter(Color.parseColor("#d0d0d0"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("Historico");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4DB6AC")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //header navigation view
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View v = navigationView.getHeaderView(0);
        txv_header_nome = (TextView) v.findViewById(R.id.txv_header_nome);
        txv_header_email = (TextView) v.findViewById(R.id.txv_header_email);
        txv_header_nome.setText(Sessao.getNomeCliente());
        txv_header_email.setText(Sessao.getEMAIL());

        COD_EMPRESA = Sessao.COD_EMPRESA;

        getAgendamento();

    }

    @Subscribe
    public void onEvent(Eventos.atualizaHolder atualizar){
        getAgendamento();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        getAgendamento();
    }

    public void onClickNovoAgendamento(View v){

        Intent intent = new Intent(this, MenuInicial.class);
        startActivity(intent);

    }

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
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            finish();
            Intent intent = new Intent(this, Historico.class);
            startActivity(intent);
        } else if (id == R.id.nav_agendar) {
            finish();
            Intent intent = new Intent(this, MenuInicial.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            finish();
            Intent intent = new Intent(this, Localizacao.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            finish();
            Intent intent = new Intent(this, Promocoes.class);
            startActivity(intent);
        } else if (id == R.id.nav_unidade) {
            finish();
            Intent intent = new Intent(this, SelecaoUnidade.class);
            startActivity(intent);
        } else if (id == R.id.nav_la) {
            finish();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
