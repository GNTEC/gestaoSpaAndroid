package br.com.brasil.spa.Entidades;

/**
 * Created by Ivan on 21/11/2016.
 */

public class Servicos {

    private Integer codServico;
    private String desServico;
    private Double valServico;

    public Servicos(){

    }

    public Servicos(Integer codServico, String desServico, Double valServico){
        this.codServico = codServico;
        this.desServico = desServico;
        this.valServico = valServico;
    }

    public Integer getCodServico() {
        return codServico;
    }

    public void setCodServico(Integer codServico) {
        this.codServico = codServico;
    }

    public String getDesServico() {
        return desServico;
    }

    public void setDesServico(String desServico) {
        this.desServico = desServico;
    }

    public Double getValServico() {
        return valServico;
    }

    public void setValServico(Double valServico) {
        this.valServico = valServico;
    }
}
