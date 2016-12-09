package br.com.brasil.spa.Utils;

/**
 * Created by Ivan on 21/11/2016.
 */

public class Eventos {

    public static final class RecebeData{

        String data;

        public RecebeData(String data){
            this.data = data;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static final class RecebeCodFilial{

        Integer codFilial;

        public RecebeCodFilial(Integer codFilial){
            this.codFilial = codFilial;
        }

        public Integer getCodFilial() {
            return codFilial;
        }

        public void setCodFilial(Integer codFilial) {
            this.codFilial = codFilial;
        }
    }

    public static final class RecebeDadosLoginCodCliente{

        Integer COD_CLIENTE;

        public RecebeDadosLoginCodCliente(Integer COD_CLIENTE){
            this.COD_CLIENTE= COD_CLIENTE;
        }

        public Integer getCOD_CLIENTE() {
            return COD_CLIENTE;
        }

        public void setCOD_CLIENTE(Integer COD_CLIENTE) {
            this.COD_CLIENTE = COD_CLIENTE;
        }

    }

    public static final class RecebeDadosLoginNomeCliente{

        String NOME;

        public RecebeDadosLoginNomeCliente(String NOME){
            this.NOME = NOME;
        }

        public String getNOME() {
            return NOME;
        }

        public void setNOME(String NOME) {
            this.NOME = NOME;
        }
    }



    public static final class RecebeEnderecoFilial{
        String endereco;

        public RecebeEnderecoFilial(String endereco){
            this.endereco = endereco;
        }

        public String getEndereco() {
            return endereco;
        }

        public void setEndereco(String endereco) {
            this.endereco = endereco;
        }
    }


}
