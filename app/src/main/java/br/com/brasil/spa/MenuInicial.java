package br.com.brasil.spa;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import br.com.brasil.spa.Entidades.Filial;
import br.com.brasil.spa.Entidades.HorasProfissional;
import br.com.brasil.spa.Entidades.Profissionais;
import br.com.brasil.spa.Entidades.ProfissionaisHorario;
import br.com.brasil.spa.Entidades.Servicos;
import br.com.brasil.spa.Entidades.Unidade;
import br.com.brasil.spa.Utils.DateDialog;
import br.com.brasil.spa.Utils.Eventos;
import br.com.brasil.spa.Utils.Sessao;


public class MenuInicial extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Variaveis estáticas
    private static Integer COD_EMPRESA = 58;
    private Integer posicao;

    //Listas
    private List<Unidade> lstUnidades;
    private List<Servicos> lstServicos;
    private List<Profissionais> lstProfissionais;
    private List<ProfissionaisHorario> lstProfissionaisHorario;
    private List<ResultadoAgendamento> lstResultadoAgendamento;
    private List<Filial> auxLstUnidades;
    private List<String> lstSpnUnidade;
    private List<String> auxLstServicos;
    private List<String> auxLstProfissionais;
    private List<String> auxLstProfissionaisHorario;

    //Entidades
    private Filial filial;
    private Profissionais profissionais;
    private Servicos servicos;
    private Unidade unidade;
    private HorasProfissional horasProfissional;

    //Spinners
    private Spinner spnU;
    private Spinner spnS;
    private Spinner spnP;
    private Spinner spnH;
    private TextView txv_data;
    private TextView txt_calendario;

    //seleções spinners
    private String filialSelecionada;
    private String servicoSelecionado;
    private Integer codServicoSelecionado;
    private String profissionalSelecionado;
    private String horarioSelecionado;
    private Integer COD_FILIAL;
    private String dataRecebida = "a";
    private Integer codProfissionalSelecionado;

    //AGENDAMENTO
    private String resultadoAgendamento;
    private String horaAgendamento;
    private String dateString;
    private String timeString;

    //Dados Login
    private Integer cod_cliente;
    private String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);

        }
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.mobile_nav);
        upArrow.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        //getSupportActionBar().setTitle("Spa Dona Beleza");
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#232323'>Spa Dona Beleza </font>"));
        //#4DB6AC
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4DB6AC")));
        Drawable d =getResources().getDrawable(R.drawable.toolbar);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EventBus.getDefault().register(this);

        //Recebe os dados do cliente
        cod_cliente = Sessao.getCodCliente();
        nome = Sessao.getNomeCliente();

        //Recebe os parametros de unidade
        Bundle extras = getIntent().getExtras();
        posicao = extras.getInt("pUnidade");

        spnU = (Spinner) findViewById(R.id.spn_unidade);
        spnS = (Spinner) findViewById(R.id.spn_servico);
        spnP = (Spinner) findViewById(R.id.spn_profissional);
        spnH = (Spinner) findViewById(R.id.spn_horario);

        txv_data = (TextView) findViewById(R.id.txv_data);
        txv_data.setText("");

        txt_calendario = (TextView) findViewById(R.id.txtCalendario);
        txt_calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = MenuInicial.this.getSupportFragmentManager();
                DateDialog dateDialog = new DateDialog();
                dateDialog.show(fm, "TAG");
            }
        });

        travaSpinners();

        getUnidade();
        spnU.setEnabled(false);
        //Seta serviços de acordo com o parametro recebido
        spnS.setEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //clicks nos spinners
        /*spnU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                filialSelecionada = lstSpnUnidade.get(i);

                if (!filialSelecionada.equals("Selecione")) {

                    filialSelecionada = lstSpnUnidade.get(i);
                    for (int x = 0; i < auxLstUnidades.size(); i++) {
                        if (filialSelecionada.equals(auxLstUnidades.get(x).getNOME_FILIAL())) {
                            COD_FILIAL = auxLstUnidades.get(x).getCOD_FILIAL();
                        }
                    }

                    getServicos(COD_FILIAL);
                    spnS.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                //NADA

            }
        });*/

        spnS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!auxLstServicos.get(i).equals("Selecione") && !auxLstServicos.get(i).equals("Sem servicos para exibição")) {
                    servicoSelecionado = auxLstServicos.get(i);
                    codServicoSelecionado = lstServicos.get(i).getCodServico();
                    //Toast.makeText(MenuInicial.this, servicoSelecionado, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                //NADA

            }
        });

        spnP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!auxLstProfissionais.get(i).equals("Selecione") && !auxLstProfissionais.get(i).equals("Sem profissionais para exibição")) {

                    profissionalSelecionado = auxLstProfissionais.get(i);
                    codProfissionalSelecionado = lstProfissionais.get(i).getCOD_PROFISSIONAL();
                    getHorario();
                    spnH.setEnabled(true);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                //NADA

            }
        });

        spnH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!auxLstProfissionaisHorario.get(i).equals("Selecione") &&
                        !auxLstProfissionaisHorario.get(i).equals("Sem horários para exibição")) {

                    horaAgendamento = auxLstProfissionaisHorario.get(i);

                   // Date date = toDate(horaAgendamento);


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


                    /*DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                    formatter.format(new Date());
                    DateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = null;
                    try {
                        date = parser.parse(horaAgendamento);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int h = date.getHours();
                    int m = date.getMinutes();
                    int s = date.getSeconds();
                    timeAsString = String.valueOf( h + ":" + m + ":" + s);*/


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void formatarDataTimePicker(){

        DateFormat formato1 = new SimpleDateFormat("dd/MM/yyyy");
        dateString = "";

        try {
            Date date1 = formato1.parse(dataRecebida);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat1.applyPattern("dd/MM/yyyy");
            dateString = dateFormat1.format(date1);
                        /*dateFormat.applyPattern("HH:mm:ss");
                        timeString = dateFormat.format(date);*/
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void travaSpinners() {

        spnP.setEnabled(false);
        spnS.setEnabled(false);
        spnH.setEnabled(false);
    }

    //funções que populacionam os spinners

    public void preencheSpinnerUnidade() {


        ArrayAdapter<String> adapterUnidade = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, lstSpnUnidade);
        adapterUnidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnU.setAdapter(adapterUnidade);

        //Esconde dados do textView e Spinner
        TextView txtProf = (TextView) findViewById(R.id.txv_profissional);
        txtProf.setVisibility(View.GONE);
        spnP.setVisibility(View.GONE);

    }

    public void preencheSpinnerServicos() {

        ArrayAdapter<String> adapterServicos = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, auxLstServicos);
        adapterServicos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnS.setAdapter(adapterServicos);

    }

    public void preencheSpinnerProfissional() {

        ArrayAdapter<String> adapterProfissionais = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, auxLstProfissionais);
        adapterProfissionais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(!auxLstProfissionais.equals(null)) {
            spnP.setAdapter(adapterProfissionais);
        }

    }

    public void preencheSpinnerProfissionalHorarios() {

        ArrayAdapter<String> adapterProfissionaisHorario = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, auxLstProfissionaisHorario);
        adapterProfissionaisHorario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(auxLstProfissionais.size()>0) {
            spnH.setAdapter(adapterProfissionaisHorario);
        }
    }

    public void onRadioButtonClicked(View v) {
        //require to import the RadioButton class
        RadioButton rb1 = (RadioButton) findViewById(R.id.rdnSim);
        RadioButton rb2 = (RadioButton) findViewById(R.id.rdnNao);
        Spinner spnP = (Spinner) findViewById(R.id.spn_profissional);
        TextView txtProf = (TextView) findViewById(R.id.txv_profissional);

        //is the current radio button now checked?
        boolean checked = ((RadioButton) v).isChecked();

        switch (v.getId()) {

            case R.id.rdnSim:
                if (checked)
                    txtProf.setVisibility(View.VISIBLE);
                    spnP.setVisibility(View.VISIBLE);
                    //preencheSpinnerProfissional();
                    spnP.setEnabled(true);
                if (dataRecebida.equals("a")) {
                    rb2.setChecked(true);
                    //Toast.makeText(MenuInicial.this, "Selecione uma data", Toast.LENGTH_SHORT).show();
                    txtProf.setVisibility(View.GONE);
                    spnP.setVisibility(View.GONE);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MenuInicial.this);
                    dlg.setMessage("Você deve selecionar uma data para escolher o profissional");
                    dlg.setNeutralButton("OK", null);
                    dlg.show();

                } else {

                    getProfissionais(COD_EMPRESA, codServicoSelecionado, dataRecebida);
                }
                break;

            case R.id.rdnNao:
                if (checked)
                    txtProf.setVisibility(View.GONE);
                spnP.setVisibility(View.GONE);

                break;
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
            Intent intent = new Intent(this, Historico.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, MenuInicial.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, Localizacao.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, Promocoes.class);
            startActivity(intent);
        }else if(id == R.id.nav_unidade){
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
                            COD_FILIAL = auxLstUnidades.get(posicao).getCOD_FILIAL();
                            spnU.setSelection(posicao);
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

    public void getProfissionais(final Integer COD_EMPRESA, final Integer COD_SERVICO, final String DATA) {
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

                    String resultadoFuncProfissionais = objWs.getProfissionais(COD_EMPRESA, COD_SERVICO, DATA);

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

                    String resultadoHorarios = objWs.getHorario(COD_EMPRESA, COD_FILIAL, dataRecebida, codProfissionalSelecionado);

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
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void onclicAgendar(View v) {

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

                    resultadoAgendamento = objWs.setAgendamento(COD_EMPRESA, COD_FILIAL, 0, cod_cliente,
                            codServicoSelecionado,codProfissionalSelecionado,dateString, timeString);

                    resultadoAgendamento = resultadoAgendamento.replace("\"","");

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

    public  void gotoResultadoAgendamento(){

        if (resultadoAgendamento.equals("Agendamento realizado com sucesso.")){

            Toast.makeText(this, resultadoAgendamento, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, ResultadoAgendamento.class);
            startActivity(intent);
        }
        else {

            Toast.makeText(this ,"Erro no Agendamento" , Toast.LENGTH_SHORT).show();
        }
    }

    //Eventos do event bus

    //Recebe data do datePicker
    @Subscribe
    public void onEventRecebeData(Eventos.RecebeData data) {

        dataRecebida = data.getData();
        txv_data.setText("Data selecionada: " + dataRecebida);

    }
}
