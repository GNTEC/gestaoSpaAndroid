package br.com.brasil.spa;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import br.com.brasil.spa.Entidades.Filial;
import br.com.brasil.spa.Entidades.Unidade;
import br.com.brasil.spa.Utils.Eventos;
import br.com.brasil.spa.Utils.Sessao;

public class SelecaoUnidade extends AppCompatActivity {

    private Spinner spn_escolha_unidade;
    private Button btn_selecionar_unidade;

    private List<Unidade> lstUnidades;
    private List<Filial> auxLstUnidades;
    private List<String> lstSpnUnidade;

    private static Integer COD_EMPRESA;
    private String unidadeSelecionada;
    private Integer posicao;
    private Integer COD_FILIAL;
    private String enderecoFilial;

    //http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao_2.asmx

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_unidade);

        spn_escolha_unidade = (Spinner) findViewById(R.id.spn_escolha_unidade);
        btn_selecionar_unidade = (Button) findViewById(R.id.btn_selecionar_unidade);

        //EventBus.getDefault().register(this);

        COD_EMPRESA = Sessao.COD_EMPRESA;

        getUnidade();

        spn_escolha_unidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                unidadeSelecionada = lstSpnUnidade.get(i).toString();
                posicao = i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_selecionar_unidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(unidadeSelecionada.equals("Selecione")){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(SelecaoUnidade.this);
                    //dlg.setTitle("Selecione unidade");
                    dlg.setMessage("Selecione uma unidade antes de continuar");
                    dlg.setNeutralButton("OK", null);
                    dlg.show();
                    //Toast.makeText(SelecaoUnidade.this, "Selecione a unidade para seu atendimento", Toast.LENGTH_SHORT).show();
                }else{

                    enderecoFilial = auxLstUnidades.get(posicao).getENDERECO();
                    //EventBus.getDefault().post(new Eventos.RecebeEnderecoFilial(enderecoFilial));
                    Sessao.setENDERECO(enderecoFilial);

                    COD_FILIAL = auxLstUnidades.get(posicao).getCOD_FILIAL();
                    //EventBus.getDefault().post(new Eventos.RecebeCodFilial(COD_FILIAL));

                    Sessao.setCodFilial(COD_FILIAL);
                    Sessao.setPOSICAO(posicao);

                    Intent intent = new Intent(SelecaoUnidade.this, MenuInicial.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void getUnidade() {
        new Thread() {
            @Override
            public void run() {
                try {

                    Log.e("getUnidade", "getUnidade");

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

                        for (int i = 0; i < 1; i++) {

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

    public void preencheSpinnerUnidade() {


        ArrayAdapter<String> adapterUnidade = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, lstSpnUnidade);
        adapterUnidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_escolha_unidade.setAdapter(adapterUnidade);

    }
}