package br.com.brasil.spa.Entidades;

/**
 * Created by Ivan on 09/12/2016.
 */

public class Data {

    private String DATA;
    private String HORA;
    private String STATUS;

    public Data(){

    }

    public Data (String DATA, String HORA, String STATUS){
        this.DATA = DATA;
        this.HORA = HORA;
        this.STATUS = STATUS;
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
