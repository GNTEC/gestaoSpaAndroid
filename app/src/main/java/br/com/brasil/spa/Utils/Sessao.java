package br.com.brasil.spa.Utils;

/**
 * Created by Ivan on 08/12/2016.
 */

public class Sessao {

    public static Integer COD_FILIAL;

    public static String ENDERECO;

    public static Integer COD_CLIENTE;

    public static String NOME_CLIENTE;

    public static Integer getCodFilial() {
        return COD_FILIAL;
    }

    public static void setCodFilial(Integer codFilial) {
        COD_FILIAL = codFilial;
    }

    public static String getENDERECO() {
        return ENDERECO;
    }

    public static void setENDERECO(String ENDERECO) {
        Sessao.ENDERECO = ENDERECO;
    }

    public static Integer getCodCliente() {
        return COD_CLIENTE;
    }

    public static void setCodCliente(Integer codCliente) {
        COD_CLIENTE = codCliente;
    }

    public static String getNomeCliente() {
        return NOME_CLIENTE;
    }

    public static void setNomeCliente(String nomeCliente) {
        NOME_CLIENTE = nomeCliente;
    }
}
