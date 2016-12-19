package br.com.brasil.spa.Utils;

public class AgendamentoDados {

    public static Integer COD_FILIAL;
    public static Integer COD_AGENDAMENTO;
    public static Integer COD_CLIENTE;
    public static Integer COD_SERVICO;
    public static Integer COD_PROFISSIONAL;
    public static String DATA_SELECIONADA;

    public static Integer getCodFilial() {
        return COD_FILIAL;
    }

    public static void setCodFilial(Integer codFilial) {
        COD_FILIAL = codFilial;
    }

    public static Integer getCodAgendamento() {
        return COD_AGENDAMENTO;
    }

    public static void setCodAgendamento(Integer codAgendamento) {
        COD_AGENDAMENTO = codAgendamento;
    }

    public static Integer getCodCliente() {
        return COD_CLIENTE;
    }

    public static void setCodCliente(Integer codCliente) {
        COD_CLIENTE = codCliente;
    }

    public static Integer getCodServico() {
        return COD_SERVICO;
    }

    public static void setCodServico(Integer codServico) {
        COD_SERVICO = codServico;
    }

    public static Integer getCodProfissional() {
        return COD_PROFISSIONAL;
    }

    public static void setCodProfissional(Integer codProfissional) {
        COD_PROFISSIONAL = codProfissional;
    }

    public static String getDataSelecionada() {
        return DATA_SELECIONADA;
    }

    public static void setDataSelecionada(String dataSelecionada) {
        DATA_SELECIONADA = dataSelecionada;
    }
}
