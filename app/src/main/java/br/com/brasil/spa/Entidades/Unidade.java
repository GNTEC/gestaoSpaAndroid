package br.com.brasil.spa.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 19/11/2016.
 */

public class Unidade {

    private int codEmpresa;
    private String nomeEmpresa;
    private List<Filial> lstFilial;

    public Unidade(){

    }

    public Unidade(int codEmpresa, String nomeEmpresa, List<Filial> lstFilial){
        this.codEmpresa = codEmpresa;
        this.nomeEmpresa = nomeEmpresa;
        this.lstFilial = lstFilial;
    }

    public Unidade(int codEmpresa, String nomeEmpresa){
        this.codEmpresa = codEmpresa;
        this.nomeEmpresa = nomeEmpresa;
    }

    public List<Filial> getFiliais(){
        return this.lstFilial;
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

    public List<Filial> getLstFilial() {
        return lstFilial;
    }

    public void setLstFilial(List<Filial> lstFilial) {
        this.lstFilial = lstFilial;
    }

}
