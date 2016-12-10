package br.com.brasil.spa.Entidades;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Ivan on 09/12/2016.
 */

public class Agendamento {

    private Integer COD_AGENDAMENTO;
    private Profissionais PROFISSIONAIS;
    private Servicos SERVICOS;
    private Cliente CLIENTE;
    private String DATA;
    private String HORA;
    private String STATUS;


    public Agendamento(){

    }

    public Agendamento(Integer COD_AGENDAMENTO){
        this.COD_AGENDAMENTO = COD_AGENDAMENTO;

    }

    public Agendamento(Integer COD_AGENDAMENTO, Profissionais PROFISSIONAIS, Servicos SERVICOS,
                       Cliente CLIENTE){
        this.COD_AGENDAMENTO = COD_AGENDAMENTO;
        this.PROFISSIONAIS = PROFISSIONAIS;
        this.SERVICOS = SERVICOS;
        this.CLIENTE = CLIENTE;

    }

    public Integer getCOD_AGENDAMENTO() {
        return COD_AGENDAMENTO;
    }

    public void setCOD_AGENDAMENTO(Integer COD_AGENDAMENTO) {
        this.COD_AGENDAMENTO = COD_AGENDAMENTO;
    }

    public Profissionais getPROFISSIONAIS() {
        return PROFISSIONAIS;
    }

    public void setPROFISSIONAIS(Profissionais PROFISSIONAIS) {
        this.PROFISSIONAIS = PROFISSIONAIS;
    }

    public Servicos getSERVICOS() {
        return SERVICOS;
    }

    public void setSERVICOS(Servicos SERVICOS) {
        this.SERVICOS = SERVICOS;
    }

    public Cliente getCLIENTE() {
        return CLIENTE;
    }

    public void setCLIENTE(Cliente CLIENTE) {
        this.CLIENTE = CLIENTE;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public String getHORA() {
        return HORA;
    }

    public void setHORA(String HORA) {
        this.HORA = HORA;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }
}
