package br.com.brasil.spa.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import br.com.brasil.spa.Entidades.Agendamento;
import br.com.brasil.spa.Entidades.HorasProfissional;
import br.com.brasil.spa.Historico;
import br.com.brasil.spa.R;
import br.com.brasil.spa.Utils.Eventos;
import br.com.brasil.spa.Utils.Sessao;
import br.com.brasil.spa.WebService;

/**
 * Created by Ivan on 09/12/2016.
 */

public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoAdapter.mViewHolder>{

    private List<Agendamento> mList;
    private LayoutInflater mLayoutInflater;
    private List<String> lstSpinner;
    private Context context;
    private String selecaoSpinner;

    //Set Agengamento
    private Integer COD_EMPRESA = 58;
    private Integer COD_FILIAL;
    private Integer COD_AGENDAMENTO;
    private Integer status;
    private String resultadoStatus;
    private Handler handler;
    private String res;

    public AgendamentoAdapter(Context context, List<Agendamento> list){
        this.context = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_agendamento, parent, false);
        mViewHolder mvh = new mViewHolder(view);
        lstSpinner = new ArrayList<String>();
        lstSpinner.add("Selecione");
        lstSpinner.add("Cancelar");
        lstSpinner.add("Confirmar");
        lstSpinner.add("Alterar");

        return mvh;
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, final int position) {

        holder.txv_codigo_agendamento1.setText("Código Agendamento: " + String.valueOf(mList.get(position).getCOD_AGENDAMENTO()));
        holder.txv_nome_profissional.setText("Profissional: " + String.valueOf(mList.get(position).getPROFISSIONAIS().getNOME()));
        holder.txv_desc_servico.setText("Descrição serviço: " +String.valueOf(mList.get(position).getSERVICOS().getDesServico()));
        holder.txv_valor.setText("Valor: R$ " + String.valueOf(mList.get(position).getSERVICOS().getValServico()));
        holder.txv_nome_cliente.setText("Nome cliente: "+ String.valueOf(mList.get(position).getCLIENTE().getNOME()));
        holder.txv_data.setText("Data: " + String.valueOf(mList.get(position).getDATA()));
        holder.txv_hora.setText("Hora: " + String.valueOf(mList.get(position).getHORA()));
        holder.txv_status.setText("Status: " + String.valueOf(mList.get(position).getSTATUS()));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, lstSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spn_agendamento.setAdapter(adapter);

        holder.spn_agendamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selecaoSpinner = lstSpinner.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //NADA
            }
        });

        holder.btn_gerenciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                COD_FILIAL = Sessao.getCodFilial();
                COD_AGENDAMENTO = mList.get(position).getCOD_AGENDAMENTO();

                EventBus.getDefault().post(new Eventos.atualizaHolder());

                if(selecaoSpinner.equals("Selecione")){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                    dlg.setMessage("Selecione uma ação");
                    dlg.setNeutralButton("OK", null);
                    dlg.show();
                }else if(selecaoSpinner.equals("Cancelar")){
                    status = 0;
                    setStatusAgendamento();

                }else if(selecaoSpinner.equals("Confirmar")){
                    status = 1;
                    setStatusAgendamento();

                }else if(selecaoSpinner.equals("Alterar")){
                    status = 2;
                    setStatusAgendamento();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder{

        public TextView txv_codigo_agendamento1;
        public TextView txv_nome_profissional;
        public TextView txv_desc_servico;
        public TextView txv_valor;
        public TextView txv_nome_cliente;
        public TextView txv_data;
        public TextView txv_hora;
        public TextView txv_status;
        public Spinner spn_agendamento;
        public Button btn_gerenciar;

        public mViewHolder(View itemView) {
            super(itemView);

            txv_codigo_agendamento1 = (TextView) itemView.findViewById(R.id.txv_codigo_agendamento1);
            txv_nome_profissional = (TextView) itemView.findViewById(R.id.txv_nome_profissional);
            txv_desc_servico = (TextView) itemView.findViewById(R.id.txv_desc_servico);
            txv_valor = (TextView) itemView.findViewById(R.id.txv_valor);
            txv_nome_cliente = (TextView) itemView.findViewById(R.id.txv_nome_cliente);
            txv_data = (TextView) itemView.findViewById(R.id.txv_data);
            txv_hora = (TextView) itemView.findViewById(R.id.txv_hora);
            txv_status = (TextView) itemView.findViewById(R.id.txv_status);
            spn_agendamento = (Spinner) itemView.findViewById(R.id.spn_agendamento);
            btn_gerenciar = (Button) itemView.findViewById(R.id.btn_gerenciar);

        }
    }

    public void setStatusAgendamento() {
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try{

                    String SOAP_ACTION = "http://www.gestaospa.com.br/PROD/WebSrv/SET_AGENDAMENTO_STATUS_2";
                    String OPERATION_NAME = "SET_AGENDAMENTO_STATUS_2";
                    String WDSL_TARGET_NAMESPACE = "http://www.gestaospa.com.br/PROD/WebSrv/";
                    String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao.asmx";
                    //String SOAP_ADDRESS = "http://www.gestaospa.com.br/PROD/WebSrv/WebServiceGestao_2.asmx";

                    WebService objWs = new WebService();
                    objWs.setSOAP_ACTION(SOAP_ACTION);
                    objWs.setOPERATION_NAME(OPERATION_NAME);
                    objWs.setWSDL_TARGET_NAMESPACE(WDSL_TARGET_NAMESPACE);
                    objWs.setSOAP_ADDRESS(SOAP_ADDRESS);

                    resultadoStatus = objWs.setAgengamentoStatus(COD_EMPRESA, COD_FILIAL, COD_AGENDAMENTO, status);
                    res = resultadoStatus.replaceAll("\"", "");
                    Log.e("Resultado: ", resultadoStatus);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                            dlg.setMessage(res);
                            dlg.setNeutralButton("OK", null);
                            dlg.show();

                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}


