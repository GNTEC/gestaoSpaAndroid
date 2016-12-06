package br.com.brasil.spa.Entidades;

/**
 * Created by  Ivan on 21/11/2016.
 */

public class Profissionais {

    private Integer COD_PROFISSIONAL;
    private String NOME;

    public Profissionais(){

    }

    public Profissionais(Integer COD_PROFISSIONAL, String NOME){
        this.COD_PROFISSIONAL = COD_PROFISSIONAL;
        this.NOME = NOME;
    }

    public Integer getCOD_PROFISSIONAL() {
        return COD_PROFISSIONAL;
    }

    public void setCOD_PROFISSIONAL(Integer COD_PROFISSIONAL) {
        this.COD_PROFISSIONAL = COD_PROFISSIONAL;
    }

    public String getNOME() {
        return NOME;
    }

    public void setNOME(String NOME) {
        this.NOME = NOME;
    }
}
