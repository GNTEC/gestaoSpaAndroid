package br.com.brasil.spa;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompatSideChannelService;
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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import br.com.brasil.spa.Entidades.Filial;
import br.com.brasil.spa.Entidades.HorasProfissional;
import br.com.brasil.spa.Entidades.Profissionais;
import br.com.brasil.spa.Entidades.ProfissionaisHorario;
import br.com.brasil.spa.Entidades.Servicos;
import br.com.brasil.spa.Entidades.Unidade;
import br.com.brasil.spa.Utils.DateDialog;
import br.com.brasil.spa.Utils.Eventos;


public class MenuInicial extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Integer COD_EMPRESA = 58;
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

    private Filial filial;
    private Profissionais profissionais;
    private Servicos servicos;
    private Unidade unidade;
    private HorasProfissional horasProfissional;

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
        getSupportActionBar().setTitle("Spa BUDDHA");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4DB6AC")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EventBus.getDefault().register(this);

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

        //preencheSpinnerUnidade();

        getUnidade();

        //spinners();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //clicks nos spinners
        spnU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });

        spnS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!auxLstServicos.get(i).equals("Selecione")) {
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

                if (!auxLstProfissionais.get(i).equals("Selecione")) {
                    profissionalSelecionado = auxLstProfissionais.get(i);
                    codProfissionalSelecionado = lstProfissionais.get(i).getCOD_PROFISSIONAL();
                    getHorario();
                    //Toast.makeText(MenuInicial.this, profissionalSelecionado, Toast.LENGTH_SHORT).show();
                    //preencheSpinnerProfissionalHorarios();
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

                /*
                * Quando for banco de dados setar variaveis para a proxima query
                * Quando mudar alguma opção do spinner zerar os proximos spinners
                * para mandar a nova query atualizada
                */

                /*if(!auxLstHorasProfissional.get(i).equals("Selecione")){
                    horarioSelecionado = auxLstHorasProfissional.get(i);
                    Toast.makeText(MenuInicial.this, horarioSelecionado, Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
        spnP.setAdapter(adapterProfissionais);

    }

    public void preencheSpinnerProfissionalHorarios() {

        ArrayAdapter<String> adapterProfissionaisHorario = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, auxLstProfissionaisHorario);
        adapterProfissionaisHorario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnH.setAdapter(adapterProfissionaisHorario);
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
                    Toast.makeText(MenuInicial.this, "Selecione uma data", Toast.LENGTH_SHORT).show();
                    rb2.setSelected(true);
                    rb1.setSelected(false);
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

        dataRecebida = data.getData();
        txv_data.setText("Data selecionada: " + dataRecebida);

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

                    String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/GET_SERVICOS_2";
                    String OPERATION_NAME = "GET_SERVICOS_2";
                    String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
                    String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";

                    WebService objWs = new WebService();
                    objWs.setSOAP_ACTION(SOAP_ACTION);
                    objWs.setOPERATION_NAME(OPERATION_NAME);
                    objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
                    objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

                    //Integer cod_filial = filial.getCodEmpresa();
                    String resultadoFuncServicos = objWs.getServicos(COD_EMPRESA, filial);
                    JSONArray jsonArray = new JSONArray(resultadoFuncServicos);

                    lstServicos = new ArrayList<Servicos>();
                    auxLstServicos = new ArrayList<String>();

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

                    String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/GET_PROFISSIONAIS_2";
                    String OPERATION_NAME = "GET_PROFISSIONAIS_2";
                    String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
                    String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";

                    WebService objWs = new WebService();
                    objWs.setSOAP_ACTION(SOAP_ACTION);
                    objWs.setOPERATION_NAME(OPERATION_NAME);
                    objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
                    objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

                    String resultadoFuncProfissionais = objWs.getProfissionais(COD_EMPRESA, COD_SERVICO, DATA);

                    JSONArray jsonArray = new JSONArray(resultadoFuncProfissionais);

                    if (jsonArray.length() > 0) {

                        lstProfissionais = new ArrayList<Profissionais>();
                        auxLstProfissionais = new ArrayList<String>();

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
                            preencheSpinnerProfissional();
                        }
                    });

                    //TODO- carregar o spinner correspondente com os dados e setar a entidadade com opc selecionada spinnner anterior
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

                    String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/GET_HORARIO_LIVRE_PROFISSIONAL_2";
                    String OPERATION_NAME = "GET_HORARIO_LIVRE_PROFISSIONAL_2";
                    String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
                    String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";

                    WebService objWs = new WebService();
                    objWs.setSOAP_ACTION(SOAP_ACTION);
                    objWs.setOPERATION_NAME(OPERATION_NAME);
                    objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
                    objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

                    String resultadoHorarios = objWs.getHorario(COD_EMPRESA, COD_FILIAL, dataRecebida, codProfissionalSelecionado);

                    JSONArray jsonArray = new JSONArray(resultadoHorarios);

                    if (jsonArray.length() > 0) {

                        lstProfissionaisHorario = new ArrayList<ProfissionaisHorario>();
                        auxLstProfissionaisHorario = new ArrayList<String>();

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
                            preencheSpinnerProfissionalHorarios();
                        }
                    });

                    //TODO- carregar o spinner correspondente com os dados e setar a entidadade com opc selecionada spinnner anterior
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

                    resultadoAgendamento = objWs.setAgendamento(COD_EMPRESA, COD_FILIAL, 0, COD_EMPRESA, codServicoSelecionado, codProfissionalSelecionado,
                            dataRecebida, dataRecebida);

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

        if (resultadoAgendamento.equals("OK")){

            Intent intent = new Intent(this, ResultadoAgendamento.class);
            startActivity(intent);
        }
        else {

            Toast.makeText(this ,"Erro no Agendamento" , Toast.LENGTH_SHORT).show();
        }
    }
}
