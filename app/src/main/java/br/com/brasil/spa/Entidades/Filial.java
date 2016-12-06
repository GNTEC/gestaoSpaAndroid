package br.com.brasil.spa.Entidades;


/**
 * Created by Ivan on 19/11/2016.
 */

public class Filial{


    private Integer COD_EMPRESA_FILIAL;
    private Integer COD_FILIAL;
    private String NOME_FILIAL;
    private String ENDERECO;

    public Filial(Integer COD_EMPRESA_FILIAL, Integer  COD_FILIAL, String NOME_FILIAL, String ENDERECO) {
        this.COD_EMPRESA_FILIAL = COD_EMPRESA_FILIAL;
        this.COD_FILIAL = COD_FILIAL;
        this.NOME_FILIAL = NOME_FILIAL;
        this.ENDERECO = ENDERECO;
    }

    public Filial(){

    }


    public Integer getCOD_EMPRESA_FILIAL() {
        return COD_EMPRESA_FILIAL;
    }

    public void setCOD_EMPRESA_FILIAL(Integer COD_EMPRESA_FILIAL) {
        this.COD_EMPRESA_FILIAL = COD_EMPRESA_FILIAL;
    }

    public Integer getCOD_FILIAL() {
        return COD_FILIAL;
    }

    public void setCOD_FILIAL(Integer COD_FILIAL) {
        this.COD_FILIAL = COD_FILIAL;
    }

    public String getNOME_FILIAL() {
        return NOME_FILIAL;
    }

    public void setNOME_FILIAL(String NOME_FILIAL) {
        this.NOME_FILIAL = NOME_FILIAL;
    }

    public String getENDERECO() {
        return ENDERECO;
    }

    public void setENDERECO(String ENDERECO) {
        this.ENDERECO = ENDERECO;
    }
}
