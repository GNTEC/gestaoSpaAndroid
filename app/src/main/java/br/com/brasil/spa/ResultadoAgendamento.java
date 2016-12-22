package br.com.brasil.spa;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import br.com.brasil.spa.Adapters.AgendamentoAdapter;
import br.com.brasil.spa.Entidades.Agendamento;
import br.com.brasil.spa.Entidades.Cliente;
import br.com.brasil.spa.Entidades.Profissionais;
import br.com.brasil.spa.Entidades.Servicos;
import br.com.brasil.spa.Utils.Sessao;

/**
 * Created by Anderson on 29/10/2016.
 */

public class ResultadoAgendamento extends AppCompatActivity {

    private TextView txv_resultado_codigo_agendamento1;
    private TextView txv_resultado_nome_profissional;
    private TextView txv_resultado_desc_servico;
    private TextView txv_resultado_valor;
    private TextView txv_resultado_nome_cliente;
    private TextView txv_resultado_data;
    private TextView txv_resultado_resultado_hora;
    private TextView txv_resultado_status;
    private TextView txv_resultado_gerenciar;
    private Spinner spn_resultado_agendamento;
    private Button btn_resultado_gerenciar;
    private Integer COD_EMPRESA;
    private Agendamento agendamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_agendamento);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_historico);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Resultado agendamento");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        COD_EMPRESA = Sessao.COD_EMPRESA;

        cast();
        getAgendamento();

    }

    private void cast(){

        txv_resultado_codigo_agendamento1 = (TextView) findViewById(R.id.txv_resultado_codigo_agendamento1);
        txv_resultado_nome_profissional = (TextView) findViewById(R.id.txv_resultado_nome_profissional);
        txv_resultado_desc_servico = (TextView) findViewById(R.id.txv_resultado_desc_servico);
        txv_resultado_valor = (TextView) findViewById(R.id.txv_resultado_valor);
        txv_resultado_nome_cliente = (TextView) findViewById(R.id.txv_resultado_nome_cliente);
        txv_resultado_data = (TextView) findViewById(R.id.txv_resultado_data);
        txv_resultado_resultado_hora = (TextView) findViewById(R.id.txv_resultado_resultado_hora);
        txv_resultado_status = (TextView) findViewById(R.id.txv_resultado_status);

        /*txv_resultado_gerenciar = (TextView) findViewById(R.id.txv_resultado_gerenciar);
        spn_resultado_agendamento = (Spinner) findViewById(R.id.spn_resultado_agendamento);
        btn_resultado_gerenciar = (Button) findViewById(R.id.btn_resultado_gerenciar);

        txv_resultado_gerenciar.setVisibility(View.GONE);
        spn_resultado_agendamento.setVisibility(View.GONE);
        btn_resultado_gerenciar.setVisibility(View.GONE);*/

    }

    private void getAgendamento(){

        new Thread() {
            @Override
            public void run() {
                try {

                    //constantes webservice
                    String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/GET_AGENDAMENTO_2";
                    String OPERATION_NAME = "GET_AGENDAMENTO_2";
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

                    Integer cod_agendamento = Sessao.getCodAgendamento();
                    //resultado da soap
                    String resultadoAgendamentos = objWs.getAgendamento(COD_EMPRESA, cod_filial, cod_agendamento);

                    JSONObject obj = new JSONObject(resultadoAgendamentos);
                    //instancia a classe agendamento
                    agendamento = new Agendamento();

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


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            configure();

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void configure(){

        txv_resultado_codigo_agendamento1.setText("Codigo agendamento: " + agendamento.getCOD_AGENDAMENTO().toString());
        txv_resultado_nome_profissional.setText("Profissional: " + agendamento.getPROFISSIONAIS().getNOME().toString());
        txv_resultado_desc_servico.setText("Serviço: " + agendamento.getSERVICOS().getDesServico().toString());
        txv_resultado_valor.setText("Valor: R$ " + agendamento.getSERVICOS().getValServico().toString());
        txv_resultado_nome_cliente.setText("Nome cliente: " + agendamento.getCLIENTE().getNOME().toString());
        txv_resultado_data.setText("Data: " + agendamento.getDATA().toString());
        txv_resultado_resultado_hora.setText("Hora: " + agendamento.getHORA().toString());
        txv_resultado_status.setText("Status: " + agendamento.getSTATUS().toString());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
