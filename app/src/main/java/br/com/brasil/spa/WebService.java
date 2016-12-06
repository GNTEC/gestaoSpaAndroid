package br.com.brasil.spa;

import android.content.Intent;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.brasil.spa.Entidades.Unidade;

/**
 * Created by Ivan on 18/11/2016.
 */

public class WebService {

    private String SOAP_ACTION;
    private String OPERATION_NAME;
    private String WSDL_TARGET_NAMESPACE;
    private String SOAP_ADDRESS;
    private List<String> lstParametros;

    public List<String> getLstParametros() {
        return lstParametros;
    }

    public void setLstParametros(List<String> lstParametros) {
        this.lstParametros = lstParametros;
    }

    public String getSOAP_ACTION() {
        return SOAP_ACTION;
    }

    public void setSOAP_ACTION(String SOAP_ACTION) {
        this.SOAP_ACTION = SOAP_ACTION;
    }

    public String getOPERATION_NAME() {
        return OPERATION_NAME;
    }

    public void setOPERATION_NAME(String OPERATION_NAME) {
        this.OPERATION_NAME = OPERATION_NAME;
    }

    public String getWSDL_TARGET_NAMESPACE() {
        return WSDL_TARGET_NAMESPACE;
    }

    public void setWSDL_TARGET_NAMESPACE(String WSDL_TARGET_NAMESPACE) {
        this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
    }

    public String getSOAP_ADDRESS() {
        return SOAP_ADDRESS;
    }

    public void setSOAP_ADDRESS(String SOAP_ADDRESS) {
        this.SOAP_ADDRESS = SOAP_ADDRESS;
    }


    public String login(Integer cod_empresa,String user, String senha) throws IOException, XmlPullParserException {

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo COD_EMPRESA = new PropertyInfo();
        COD_EMPRESA.setName("COD_EMPRESA");
        COD_EMPRESA.setValue(cod_empresa);
        COD_EMPRESA.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_EMPRESA);

        PropertyInfo usr = new PropertyInfo();
        usr.setName("usr");
        usr.setValue(user);
        usr.type = PropertyInfo.STRING_CLASS;
        request.addProperty(usr);

        PropertyInfo password = new PropertyInfo();
        password.setName("password");
        password.setValue(senha);
        password.type = PropertyInfo.STRING_CLASS;
        request.addProperty(password);

        Log.e("REQUEST", request.toString());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transportSE = new HttpTransportSE(SOAP_ADDRESS);

        Object response = null;

        try{

            //transportSE.call(OPERATION_NAME, envelope);
            transportSE.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
            Log.e("Response: " , response.toString());

        }catch (IOException ex){
            Log.e("WebService: ", ex.toString() );
            response = ex.toString();
        }

        return response.toString();
    }

    public String getUnidades(Integer cod_empresa){

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo COD_EMPRESA = new PropertyInfo();
        COD_EMPRESA.setName("COD_EMPRESA");
        COD_EMPRESA.setValue(cod_empresa);
        COD_EMPRESA.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_EMPRESA);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transportSE = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;

        try{

            transportSE.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();

        }catch (Exception ex){
            Log.e("WebService: " , ex.toString());
            response = ex.toString();
        }
        return response.toString();
    }

    public String getServicos(Integer cod_empresa, Integer cod_filial){

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo COD_EMPRESA = new PropertyInfo();
        COD_EMPRESA.setName("COD_EMPRESA");
        COD_EMPRESA.setValue(cod_empresa);
        COD_EMPRESA.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_EMPRESA);

        PropertyInfo COD_FILIAL = new PropertyInfo();
        COD_FILIAL.setName("COD_FILIAL");
        COD_FILIAL.setValue(cod_filial);
        COD_FILIAL.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_FILIAL);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transportSE = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;

        try{

            transportSE.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();

        }catch (Exception ex){
            Log.e("WebService: " , ex.toString());
            response = ex.toString();
        }

        return response.toString();
    }

    public String getProfissionais(Integer cod_empresa, Integer cod_servico, String data_agenda){

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo COD_EMPRESA = new PropertyInfo();
        COD_EMPRESA.setName("COD_EMPRESA");
        COD_EMPRESA.setValue(cod_empresa);
        COD_EMPRESA.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_EMPRESA);

        PropertyInfo COD_SERVICO = new PropertyInfo();
        COD_SERVICO.setName("COD_SERVICO");
        COD_SERVICO.setValue(cod_servico);
        COD_SERVICO.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_SERVICO);

        PropertyInfo DATA_AGENDA = new PropertyInfo();
        DATA_AGENDA.setName("DATA_AGENDA");
        DATA_AGENDA.setValue(data_agenda);
        DATA_AGENDA.type = PropertyInfo.STRING_CLASS;
        request.addProperty(DATA_AGENDA);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transportSE = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;

        try{

            transportSE.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();

        }catch (Exception ex){
            Log.e("WebService: " , ex.toString());
            response = ex.toString();
        }

        return response.toString();
    }

    public String getHorario(Integer cod_empresa, Integer cod_filial, String data_agenda, Integer cod_profissional){

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo COD_EMPRESA = new PropertyInfo();
        COD_EMPRESA.setName("COD_EMPRESA");
        COD_EMPRESA.setValue(cod_empresa);
        COD_EMPRESA.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_EMPRESA);

        PropertyInfo COD_FILIAL = new PropertyInfo();
        COD_FILIAL.setName("COD_FILIAL");
        COD_FILIAL.setValue(cod_filial);
        COD_FILIAL.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_FILIAL);

        PropertyInfo DATA_AGENDA = new PropertyInfo();
        DATA_AGENDA.setName("DATA_AGENDA");
        DATA_AGENDA.setValue(data_agenda);
        DATA_AGENDA.type = PropertyInfo.STRING_CLASS;
        request.addProperty(DATA_AGENDA);

        PropertyInfo COD_PROFISSIONAL = new PropertyInfo();
        COD_PROFISSIONAL.setName("COD_PROFISSIONAL");
        COD_PROFISSIONAL.setValue(cod_profissional);
        COD_PROFISSIONAL.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_PROFISSIONAL);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transportSE = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;

        try{

            transportSE.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();

        }catch (Exception ex){
            Log.e("WebService: " , ex.toString());
            response = ex.toString();
        }

        return response.toString();
    }


    public String setAgendamento(Integer cod_empresa, Integer cod_filial, Integer cod_agendmento,
                                 Integer cod_cliente, Integer cod_servico, Integer cod_profissional,
                                 String data, String hora){

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo COD_EMPRESA = new PropertyInfo();
        COD_EMPRESA.setName("COD_EMPRESA");
        COD_EMPRESA.setValue(cod_empresa);
        COD_EMPRESA.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_EMPRESA);

        PropertyInfo COD_FILIAL = new PropertyInfo();
        COD_FILIAL.setName("COD_FILIAL");
        COD_FILIAL.setValue(cod_filial);
        COD_FILIAL.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_FILIAL);

        PropertyInfo COD_AGENDAMENTO = new PropertyInfo();
        COD_AGENDAMENTO.setName("COD_AGENDAMENTO");
        COD_AGENDAMENTO.setValue(cod_agendmento);
        COD_AGENDAMENTO.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_AGENDAMENTO);

        PropertyInfo COD_CLIENTE = new PropertyInfo();
        COD_CLIENTE.setName("COD_CLIENTE");
        COD_CLIENTE.setValue(cod_cliente);
        COD_CLIENTE.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_CLIENTE);

        PropertyInfo COD_SERVICO = new PropertyInfo();
        COD_SERVICO.setName("COD_SERVICO");
        COD_SERVICO.setValue(cod_servico);
        COD_SERVICO.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_SERVICO);

        PropertyInfo COD_PROFISSIONAL = new PropertyInfo();
        COD_PROFISSIONAL.setName("COD_PROFISSIONAL");
        COD_PROFISSIONAL.setValue(cod_profissional);
        COD_PROFISSIONAL.type = PropertyInfo.INTEGER_CLASS;
        request.addProperty(COD_PROFISSIONAL);

        PropertyInfo DATA = new PropertyInfo();
        DATA.setName("DATA");
        DATA.setValue(data);
        DATA.type = PropertyInfo.STRING_CLASS;
        request.addProperty(DATA);

        PropertyInfo HORA = new PropertyInfo();
        HORA.setName("HORA");
        HORA.setValue(hora);
        HORA.type = PropertyInfo.STRING_CLASS;
        request.addProperty(HORA);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transportSE = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;

        try{

            transportSE.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();

        }catch (Exception ex){
            Log.e("WebService: " , ex.toString());
            response = ex.toString();
        }

        return response.toString();
    }
}
