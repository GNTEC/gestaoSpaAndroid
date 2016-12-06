package br.com.brasil.spa.Entidades;


/**
 * Created by Ivan on 22/11/2016.
 */

public class ProfissionaisHorario {

    private Integer COD_PROFISSIONAL;
    private String DT_INI;
    private String DT_FIM;

    public ProfissionaisHorario(){

    }

    public ProfissionaisHorario(Integer COD_PROFISSIONAL, String DT_INI, String DT_FIM){
        this.COD_PROFISSIONAL = COD_PROFISSIONAL;
        this.DT_INI = DT_INI;
        this.DT_FIM = DT_FIM;

    }

    public Integer getCOD_PROFISSIONAL() {
        return COD_PROFISSIONAL;
    }

    public void setCOD_PROFISSIONAL(Integer COD_PROFISSIONAL) {
        this.COD_PROFISSIONAL = COD_PROFISSIONAL;
    }

    public String getDT_INI() {
        return DT_INI;
    }

    public void setDT_INI(String DT_INI) {
        this.DT_INI = DT_INI;
    }

    public String getDT_FIM() {
        return DT_FIM;
    }

    public void setDT_FIM(String DT_FIM) {
        this.DT_FIM = DT_FIM;
    }
}
