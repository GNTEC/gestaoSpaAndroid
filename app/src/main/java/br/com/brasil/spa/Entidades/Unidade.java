package br.com.brasil.spa.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 19/11/2016.
 */

public class Unidade {

    private int codEmpresa;
    private String nomeEmpresa;
    private List<Filial> lstfilial;

    public Unidade(int cod_empresa, String nome_empresa){

    }

    public Unidade(int codEmpresa, String nomeEmpresa, List<Filial> lstfilial){
        this.codEmpresa = codEmpresa;
        this.nomeEmpresa = nomeEmpresa;
        this.lstfilial =  lstfilial;
    }

    public int getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(Integer codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public void setCodEmpresa(int codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public List<Filial> getLstfilial() {
        return lstfilial;
    }

    public void setLstfilial(List<Filial> lstfilial) {
        this.lstfilial = lstfilial;
    }
}
