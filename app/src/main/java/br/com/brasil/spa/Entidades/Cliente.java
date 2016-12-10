package br.com.brasil.spa.Entidades;

import java.util.List;

/**
 * Created by Ivan on 09/12/2016.
 */

public class Cliente {

    private Integer COD_CLIENTE;
    private String NOME;
    private String MSG_RETORNO;

    public Cliente(Integer COD_CLIENTE, String NOME, String MSG_RETORNO){
        this.COD_CLIENTE = COD_CLIENTE;
        this.NOME = NOME;
        this.MSG_RETORNO = MSG_RETORNO;
    }

    public Integer getCOD_CLIENTE() {
        return COD_CLIENTE;
    }

    public void setCOD_CLIENTE(Integer COD_CLIENTE) {
        this.COD_CLIENTE = COD_CLIENTE;
    }

    public String getNOME() {
        return NOME;
    }

    public void setNOME(String NOME) {
        this.NOME = NOME;
    }

    public String getMSG_RETORNO() {
        return MSG_RETORNO;
    }

    public void setMSG_RETORNO(String MSG_RETORNO) {
        this.MSG_RETORNO = MSG_RETORNO;
    }
}
