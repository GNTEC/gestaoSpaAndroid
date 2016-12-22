package br.com.brasil.spa;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.brasil.spa.Entidades.Filial;
import br.com.brasil.spa.Entidades.Profissionais;
import br.com.brasil.spa.Entidades.ProfissionaisHorario;
import br.com.brasil.spa.Entidades.Servicos;
import br.com.brasil.spa.Entidades.Unidade;
import br.com.brasil.spa.Utils.AgendamentoDados;
import br.com.brasil.spa.Utils.DateDialog;
import br.com.brasil.spa.Utils.Eventos;
import br.com.brasil.spa.Utils.Sessao;

public class AlterarAgendamento extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Spinners
    private Spinner spn_alterar_unidade;
    private Spinner spn_alterar_servico;
    private Spinner spn_alterar_profissional;
    private Spinner spn_alterar_horario;

    //TextView Data
    private TextView txv_alterar_data;
    private TextView txt_alterar_calendario;
    private TextView txv_alterar_profissional;

    //radio buttons
    private RadioButton rdnSim_alterar;
    private RadioButton rdnNao_alterar;

    //Buttons
    private Button btn_alterar_agendar;

    //Dados Agendamento
    private Integer COD_EMPRESA;
    private Integer COD_FILIAL;
    private Integer COD_AGENDAMENTO;
    private Integer COD_CLIENTE;
    private Integer COD_SERVICO;
    private Integer COD_PROFISSIONAL;
    private String DATA_SELECIONADA = "a";
    private String dateString;
    private String timeString;
    private String horaAgendamento;
    private String servicoSelecionado;
    private Integer codServicoSelecionado;
    private String profissionalSelecionado;
    private Integer codProfissionalSelecionado;
    private String result;
    private String resultadoAgendamento;

    //sem profissional
    private boolean semProfissional = false;

    //posição Spinners
    private Integer posicaoUnidade;
    private Integer posicaoServico;
    private Integer posicaoProfissional;

    //Listas
    private List<Unidade> lstUnidades;
    private List<Servicos> lstServicos;
    private List<Profissionais> lstProfissionais;
    private List<ProfissionaisHorario> lstProfissionaisHorario;
    private List<Historico> lstResultadoAgendamento;
    private List<Filial> auxLstUnidades;
    private List<String> lstSpnUnidade;
    private List<String> auxLstServicos;
    private List<String> auxLstProfissionais;
    private List<String> auxLstProfissionaisHorario;

    //header
    private NavigationView navigationView;
    private TextView txv_header_nome;
    private TextView txv_header_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_agendamento);

        EventBus.getDefault().register(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);

        }
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.mobile_nav);
        upArrow.setColorFilter(Color.parseColor("#d0d0d0"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("Alterar agendamento");
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

        //Cast
        spn_alterar_unidade = (Spinner) findViewById(R.id.spn_alterar_unidade);
        spn_alterar_servico = (Spinner) findViewById(R.id.spn_alterar_servico);
        spn_alterar_profissional = (Spinner) findViewById(R.id.spn_alterar_profissional);
        spn_alterar_horario = (Spinner) findViewById(R.id.spn_alterar_horario);
        txv_alterar_data = (TextView) findViewById(R.id.txv_alterar_data);
        btn_alterar_agendar = (Button) findViewById(R.id.btn_alterar_agendar);
        txt_alterar_calendario = (TextView) findViewById(R.id.txt_alterar_calendario);
        rdnSim_alterar = (RadioButton) findViewById(R.id.rdnSim_alterar);
        rdnNao_alterar = (RadioButton) findViewById(R.id.rdnNao_alterar);
        txv_alterar_profissional = (TextView) findViewById(R.id.txv_alterar_profissional);

        //Recebe os dados do Agendamento
        COD_EMPRESA = Sessao.COD_EMPRESA;
        COD_FILIAL = AgendamentoDados.getCodFilial();
        COD_AGENDAMENTO = AgendamentoDados.getCodAgendamento();
        COD_CLIENTE = AgendamentoDados.getCodCliente();
        COD_SERVICO = AgendamentoDados.getCodServico();
        COD_PROFISSIONAL = AgendamentoDados.getCodProfissional();
        DATA_SELECIONADA = AgendamentoDados.getDataSelecionada();
        formatarDataTimePicker();

        //Recebe posicao
        posicaoUnidade = Sessao.getPOSICAO();

        //seta a data
        txv_alterar_data.setText(DATA_SELECIONADA);

        txt_alterar_calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = AlterarAgendamento.this.getSupportFragmentManager();
                DateDialog dateDialog = new DateDialog();
                dateDialog.show(fm, "TAG");

            }
        });

        btn_alterar_agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agendar();
            }
        });

        spn_alterar_servico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!auxLstServicos.get(i).equals("Selecione") && !auxLstServicos.get(i).equals("Sem servicos para exibição")) {

                    servicoSelecionado = auxLstServicos.get(i);
                    codServicoSelecionado = lstServicos.get(i-1).getCodServico();
                    Log.e("SERVICO: ", codServicoSelecionado + " " + servicoSelecionado);
                    //Toast.makeText(MenuInicial.this, servicoSelecionado, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                //NADA

            }
        });

        spn_alterar_profissional.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!auxLstProfissionais.get(i).equals("Selecione") && !auxLstProfissionais.get(i).equals("Sem profissionais para exibição")
                && !auxLstProfissionais.equals(null) && auxLstProfissionais.size() > 0) {

                    profissionalSelecionado = auxLstProfissionais.get(i);
                    codProfissionalSelecionado = lstProfissionais.get(i-1).getCOD_PROFISSIONAL();
                    getHorario();
                    spn_alterar_horario.setEnabled(true);

                }

                //pensar em else para setar o profissional selecionado

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                //NADA

            }
        });

        spn_alterar_horario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!auxLstProfissionaisHorario.get(i).equals("Selecione") &&
                        !auxLstProfissionaisHorario.get(i).equals("Sem horários para exibição") && auxLstProfissionaisHorario.size() > 0) {

                    horaAgendamento = auxLstProfissionaisHorario.get(i);

                    DateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    try {
                        Date date = formato.parse(horaAgendamento);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        dateString = dateFormat.format(date);
                        dateFormat.applyPattern("HH:mm:ss");
                        timeString = dateFormat.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    formatarDataTimePicker();

                    if(semProfissional == true){
                        codProfissionalSelecionado =lstProfissionaisHorario.get(i-1).getCOD_PROFISSIONAL();
                    }
                }else{

                    horaAgendamento = auxLstProfissionaisHorario.get(i);

                    DateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    try {
                        Date date = formato.parse(horaAgendamento);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        dateString = dateFormat.format(date);
                        dateFormat.applyPattern("HH:mm:ss");
                        timeString = dateFormat.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    formatarDataTimePicker();

                    if(semProfissional == true){
                        codProfissionalSelecionado =lstProfissionaisHorario.get(i).getCOD_PROFISSIONAL();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        travaSpinners();
        getUnidade();
        spn_alterar_unidade.setEnabled(false);

    }

    //trava os spinners
    public void travaSpinners() {

        spn_alterar_profissional.setEnabled(false);
        spn_alterar_servico.setEnabled(false);
        spn_alterar_horario.setEnabled(false);
    }

    //Preenche Spinners
    public void preencheSpinnerUnidade() {

        ArrayAdapter<String> adapterUnidade = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, lstSpnUnidade);
        adapterUnidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_alterar_unidade.setAdapter(adapterUnidade);

        //Esconde dados do textView e Spinner
        TextView txtProf = (TextView) findViewById(R.id.txv_alterar_profissional);
        txtProf.setVisibility(View.GONE);
        spn_alterar_profissional.setVisibility(View.GONE);

    }

    public void preencheSpinnerServicos() {

        ArrayAdapter<String> adapterServicos = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, auxLstServicos);
        adapterServicos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_alterar_servico.setAdapter(adapterServicos);

        selecionaServicoSpinner();
        if(posicaoServico != null) {
            spn_alterar_servico.setSelection(posicaoServico + 1);
        }
        spn_alterar_servico.setEnabled(true);

    }

    public void preencheSpinnerProfissional() {

        ArrayAdapter<String> adapterProfissionais = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, auxLstProfissionais);
        adapterProfissionais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(!auxLstProfissionais.equals(null) && auxLstProfissionais.size() > 0 && auxLstProfissionais != null) {
            spn_alterar_profissional.setAdapter(adapterProfissionais);
        }
    }

    public void preencheSpinnerProfissionalHorarios() {

        ArrayAdapter<String> adapterProfissionaisHorario = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, auxLstProfissionaisHorario);
        adapterProfissionaisHorario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_alterar_horario.setAdapter(adapterProfissionaisHorario);
        /*if(posicaoProfissional != null){
            spn_alterar_profissional.setSelection(posicaoProfissional + 1);
            //selecionaProfissionalSpinner();
        }*/
        spn_alterar_profissional.setEnabled(true);
    }


    //Funções de auto seleção
    public void selecionaServicoSpinner(){
        if(lstServicos.size() > 0) {
            for (int i = 0; i < lstServicos.size(); i++) {
                if (COD_SERVICO.equals(lstServicos.get(i).getCodServico())) {
                    codServicoSelecionado = lstServicos.get(i).getCodServico();
                    posicaoServico = i;
                }
            }
        }
    }

    /*public void selecionaProfissionalSpinner(){
        for (int i =0; i< lstProfissionais.size(); i++){
            if(COD_PROFISSIONAL.equals(lstServicos.get(i).getCodServico())){
                posicaoProfissional = i;
            }
        }
    }*/


    //RadioButtons
    public void onRadioButtonClicked(View v) {

        boolean checked = ((RadioButton) v).isChecked();

        switch (v.getId()) {

            case R.id.rdnSim_alterar:
                if (checked) {

                    if (DATA_SELECIONADA.equals("a") || servicoSelecionado.equals("Selecione")) {

                        rdnSim_alterar.setChecked(false);
                        rdnNao_alterar.setChecked(false);
                        txv_alterar_profissional.setVisibility(View.GONE);
                        spn_alterar_profissional.setVisibility(View.GONE);
                        AlertDialog.Builder dlg = new AlertDialog.Builder(AlterarAgendamento.this);
                        dlg.setMessage("Você deve selecionar uma data e um serviço");
                        dlg.setNeutralButton("OK", null);
                        dlg.show();

                    } else {

                        txv_alterar_profissional.setVisibility(View.VISIBLE);
                        spn_alterar_profissional.setVisibility(View.VISIBLE);
                        spn_alterar_profissional.setEnabled(true);
                        getProfissionais(COD_EMPRESA, COD_FILIAL ,codServicoSelecionado, dateString);
                        spn_alterar_horario.setSelection(0);

                    }
                }
                break;

            case R.id.rdnNao_alterar:
                if (checked) {

                    if (DATA_SELECIONADA.equals("a") || servicoSelecionado.equals("Selecione")) {

                        rdnSim_alterar.setChecked(false);
                        rdnNao_alterar.setChecked(false);
                        txv_alterar_profissional.setVisibility(View.GONE);
                        spn_alterar_profissional.setVisibility(View.GONE);

                        AlertDialog.Builder dlg = new AlertDialog.Builder(AlterarAgendamento.this);
                        dlg.setMessage("Você deve selecionar uma data e um serviço");
                        dlg.setNeutralButton("OK", null);
                        dlg.show();

                    }else {

                        txv_alterar_profissional.setVisibility(View.GONE);
                        spn_alterar_profissional.setVisibility(View.GONE);
                        spn_alterar_horario.setSelection(0);
                        getProfissionais(COD_EMPRESA, COD_FILIAL, COD_SERVICO, dateString);
                        getHorarioSemProfissional();

                    }
                }

                break;
        }
    }

    //WebServices

    public void getUnidade() {
        new Thread() {
            @Override
            public void run() {
                try {

                    String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/GET_UNIDADES_2";
                    String OPERATION_NAME = "GET_UNIDADES_2";
                    String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
                    String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";
                    //String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao_2.asmx";

                    WebService objWs = new WebService();
                    objWs.setSOAP_ACTION(SOAP_ACTION);
                    objWs.setOPERATION_NAME(OPERATION_NAME);
                    objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
                    objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

                    String resultadoFuncUnidades = objWs.getUnidades(COD_EMPRESA);
                    Log.e("Resultado Unidades: ", resultadoFuncUnidades);

                    JSONObject obj = new JSONObject(resultadoFuncUnidades);
                    JSONArray arrFiliais = obj.getJSONArray("FILIAL");

                    if (obj.length() != 0) {
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
                            COD_FILIAL = auxLstUnidades.get(posicaoUnidade).getCOD_FILIAL();
                            spn_alterar_unidade.setSelection(posicaoUnidade+1);
                            getServicos(COD_FILIAL);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void getServicos(final Integer filial) {
        new Thread() {
            @Override
            public void run() {
                try {

                    lstServicos = new ArrayList<Servicos>();
                    auxLstServicos = new ArrayList<String>();

                    String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/GET_SERVICOS_2";
                    String OPERATION_NAME = "GET_SERVICOS_2";
                    String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
                    String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";
                    //String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao_2.asmx";

                    WebService objWs = new WebService();
                    objWs.setSOAP_ACTION(SOAP_ACTION);
                    objWs.setOPERATION_NAME(OPERATION_NAME);
                    objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
                    objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

                    //Integer cod_filial = filial.getCodEmpresa();
                    String resultadoFuncServicos = objWs.getServicos(COD_EMPRESA, filial);
                    JSONArray jsonArray = new JSONArray(resultadoFuncServicos);

                    if (jsonArray.length() > 0) {

                        auxLstServicos.clear();
                        auxLstServicos.add("Selecione");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectResposta = jsonArray.getJSONObject(i);
                            lstServicos.add(new Servicos(jsonObjectResposta.getInt("COD_SERVICO"),
                                    jsonObjectResposta.getString("DSC_SERVICO"),
                                    jsonObjectResposta.getDouble("VALOR")));

                            //preenche lista auxiliar para o spinner com o serviço
                            auxLstServicos.add(lstServicos.get(i).getDesServico());
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(auxLstServicos.size() == 0){
                                auxLstServicos.add("Sem servicos para exibição");
                            }
                            preencheSpinnerServicos();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void getProfissionais(final Integer COD_EMPRESA, final Integer COD_FILIAL,final Integer COD_SERVICO, final String DATA) {
        new Thread() {

            @Override
            public void run() {
                try {

                    lstProfissionais = new ArrayList<Profissionais>();
                    auxLstProfissionais = new ArrayList<String>();

                    String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/GET_PROFISSIONAIS_2";
                    String OPERATION_NAME = "GET_PROFISSIONAIS_2";
                    String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
                    String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";
                    //String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao_2.asmx";

                    WebService objWs = new WebService();
                    objWs.setSOAP_ACTION(SOAP_ACTION);
                    objWs.setOPERATION_NAME(OPERATION_NAME);
                    objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
                    objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

                    String resultadoFuncProfissionais = objWs.getProfissionais(COD_EMPRESA, COD_FILIAL, COD_SERVICO, DATA);

                    final JSONArray jsonArray = new JSONArray(resultadoFuncProfissionais);

                    if (jsonArray.length() > 0) {

                        auxLstProfissionais.add("Selecione");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectResposta = jsonArray.getJSONObject(i);
                            lstProfissionais.add(new Profissionais(jsonObjectResposta.getInt("COD_PROFISSIONAL"),
                                    jsonObjectResposta.getString("NOME")));

                            //preenche lista auxiliar para o spinner profissionais com o nome do profissional
                            auxLstProfissionais.add(lstProfissionais.get(i).getNOME());
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(auxLstProfissionais.size() == 0){
                                auxLstProfissionais.add("Sem profissionais para exibição");
                            }
                            preencheSpinnerProfissional();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void getHorario() {
        new Thread() {
            @Override
            public void run() {
                try {

                    lstProfissionaisHorario = new ArrayList<ProfissionaisHorario>();
                    auxLstProfissionaisHorario = new ArrayList<String>();

                    String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/GET_HORARIO_LIVRE_PROFISSIONAL_2";
                    String OPERATION_NAME = "GET_HORARIO_LIVRE_PROFISSIONAL_2";
                    String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
                    String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";
                    //String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao_2.asmx";

                    WebService objWs = new WebService();
                    objWs.setSOAP_ACTION(SOAP_ACTION);
                    objWs.setOPERATION_NAME(OPERATION_NAME);
                    objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
                    objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

                    String resultadoHorarios = objWs.getHorario(COD_EMPRESA, COD_FILIAL, dateString, codProfissionalSelecionado);

                    JSONArray jsonArray = new JSONArray(resultadoHorarios);

                    if (jsonArray.length() > 0) {

                        auxLstProfissionaisHorario.add("Selecione");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectResposta = jsonArray.getJSONObject(i);
                            lstProfissionaisHorario.add(new ProfissionaisHorario(jsonObjectResposta.getInt("COD_PROFISSIONAL"),
                                    jsonObjectResposta.getString("DT_INI"),
                                    jsonObjectResposta.getString("DT_FIM")));

                            //preenche lista auxiliar para o spinner profissionais com os horarios do profissional
                            //auxLstProfissionaisHorario.add("De: " + lstProfissionaisHorario.get(i).getDT_INI() + " até " + lstProfissionaisHorario.get(i).getDT_FIM());
                            auxLstProfissionaisHorario.add(lstProfissionaisHorario.get(i).getDT_INI());

                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(auxLstProfissionaisHorario.size() == 0){
                                auxLstProfissionaisHorario.add("Sem horários para exibição");
                            }
                            preencheSpinnerProfissionalHorarios();
                            semProfissional = false;
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void getHorarioSemProfissional(){
        new Thread(){
            @Override
            public void run(){

                try {

                    lstProfissionaisHorario = new ArrayList<ProfissionaisHorario>();
                    auxLstProfissionaisHorario = new ArrayList<String>();

                    String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/GET_HORARIO_LIVRE_2";
                    String OPERATION_NAME = "GET_HORARIO_LIVRE_2";
                    String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
                    String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";
                    //String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao_2.asmx";

                    WebService objWs = new WebService();
                    objWs.setSOAP_ACTION(SOAP_ACTION);
                    objWs.setOPERATION_NAME(OPERATION_NAME);
                    objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
                    objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

                    formatarDataTimePicker();

                    String resultadoHorarioLivre = objWs.getHorarioLivre(COD_EMPRESA, COD_FILIAL, dateString);

                    JSONArray jsonArray = new JSONArray(resultadoHorarioLivre);

                    if (jsonArray.length() > 0) {

                        auxLstProfissionaisHorario.add("Selecione");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectResposta = jsonArray.getJSONObject(i);
                            lstProfissionaisHorario.add(new ProfissionaisHorario(jsonObjectResposta.getInt("COD_PROFISSIONAL"),
                                    jsonObjectResposta.getString("DT_INI"),
                                    jsonObjectResposta.getString("DT_FIM")));

                            //preenche lista auxiliar para o spinner profissionais com os horarios do profissional
                            //auxLstProfissionaisHorario.add("De: " + lstProfissionaisHorario.get(i).getDT_INI() + " até " + lstProfissionaisHorario.get(i).getDT_FIM());
                            auxLstProfissionaisHorario.add(lstProfissionaisHorario.get(i).getDT_INI());

                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(auxLstProfissionaisHorario.size() == 0){
                                auxLstProfissionaisHorario.add("Sem horários para exibição");
                            }
                            preencheSpinnerProfissionalHorarios();
                            spn_alterar_horario.setEnabled(true);
                            semProfissional = true;
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void agendar() {

        if(servicoSelecionado.equals("Selecione") || servicoSelecionado.equals("") || servicoSelecionado == null
                || DATA_SELECIONADA.equals("a") || horaAgendamento.equals("Selecione") || horaAgendamento.equals("")
                || horaAgendamento == null){

            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Antes de agendar você deve selecionar um serviço, selecionar a data, selecionar uma opção para o profissional (Sim ou não) e selecionar um horario");
            dlg.setNeutralButton("OK", null);
            dlg.show();

        }else {
            new Thread() {
                @Override
                public void run() {
                    try {

                        String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/SET_AGENDAMENTO_2";
                        String OPERATION_NAME = "SET_AGENDAMENTO_2";
                        String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
                        String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";

                        WebService objWs = new WebService();
                        objWs.setSOAP_ACTION(SOAP_ACTION);
                        objWs.setOPERATION_NAME(OPERATION_NAME);
                        objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
                        objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

                        resultadoAgendamento = objWs.setAgendamento(COD_EMPRESA, COD_FILIAL, COD_AGENDAMENTO, COD_CLIENTE,
                                codServicoSelecionado, codProfissionalSelecionado, dateString, timeString);

                        JSONObject obj = new JSONObject(resultadoAgendamento);
                        Sessao.setCodAgendamento(obj.getInt("COD_AGENDAMENTO"));
                        result = obj.getString("MSG_RETORNO");

                        //resultadoAgendamento = resultadoAgendamento.replace("\"", "");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gotoResultadoAgendamento();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public  void gotoResultadoAgendamento(){

        if (result.equals("Agendamento realizado com sucesso.")){

            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, ResultadoAgendamento.class);
            startActivity(intent);
        }
        else {

            Toast.makeText(this ,"Erro no Agendamento" , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_inicial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

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

    @Subscribe
    public void onEventRecebeData(Eventos.RecebeData data) {

        DATA_SELECIONADA = data.getData();
        formatarDataTimePicker();
        txv_alterar_data.setText("Data selecionada: " + DATA_SELECIONADA);
        if(auxLstProfissionaisHorario != null && auxLstProfissionaisHorario.size() > 0) {
            horaAgendamento = auxLstProfissionaisHorario.get(0);
        }else{
            horaAgendamento = "";
        }
        spn_alterar_horario.setSelection(0);

    }

    public void formatarDataTimePicker(){

        DateFormat formato1 = new SimpleDateFormat("dd/MM/yyyy");
        dateString = "";

        try {
            Date date1 = formato1.parse(DATA_SELECIONADA);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat1.applyPattern("dd/MM/yyyy");
            dateString = dateFormat1.format(date1);
                        /*dateFormat.applyPattern("HH:mm:ss");
                        timeString = dateFormat.format(date);*/
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
