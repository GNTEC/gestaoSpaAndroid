package br.com.brasil.spa.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.brasil.spa.Entidades.Agendamento;
import br.com.brasil.spa.R;

/**
 * Created by Ivan on 09/12/2016.
 */

public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoAdapter.mViewHolder>{

    private List<Agendamento> mList;
    private LayoutInflater mLayoutInflater;
    private List<String> lstSpinner;
    private Context context;

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
    public void onBindViewHolder(mViewHolder holder, int position) {


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
}
